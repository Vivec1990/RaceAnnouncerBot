package com.vivec.jimbot.announcer;

import com.cavariux.twitchirc.Chat.Channel;
import com.vivec.jimbot.announcer.gamesplits.PKMNREDBLUE;
import com.vivec.jimbot.irc.srl.RaceSplitTimeListener;
import com.vivec.jimbot.irc.srl.SpeedrunsliveIRCConnectionManager;
import com.vivec.jimbot.irc.twitch.TwitchIRCConnectionManager;
import com.vivec.jimbot.irc.twitch.TwitchJim;
import com.vivec.jimbot.srl.SpeedrunsliveAPI;
import com.vivec.jimbot.srl.api.Entrant;
import com.vivec.jimbot.srl.api.Game;
import com.vivec.jimbot.srl.api.PlayerState;
import com.vivec.jimbot.srl.api.Race;
import com.vivec.jimbot.srl.api.RaceState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kitteh.irc.client.library.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.vivec.jimbot.announcer.gamesplits.PKMNREDBLUE.getSplitDataByNameOrAlias;
import static com.vivec.jimbot.announcer.gamesplits.PKMNREDBLUE.getSplitNameByNameOrAlias;

public class WatchedRace {

	private static final Logger LOG = LogManager.getLogger(WatchedRace.class);
	private final List<Entrant> runnersConnectedThroughLiveSplit = new ArrayList<>();
	private final List<String> announcedSplits = new ArrayList<>();
	private Game game;
	private List<RaceSplit> splits;
	private String raceId;
	private Client srlClient;
	private TwitchJim twitchClient;
	private SpeedrunsliveAPI api;
	private String srlLiveSplitChannelName;
	private org.kitteh.irc.client.library.element.Channel srlLiveSplitChannel;
	
	private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1); 
	
	public WatchedRace(Race race) throws InterruptedException {
		this.splits = new ArrayList<>();
		this.raceId = race.getId();
		this.game = race.getGame();
		this.api = new SpeedrunsliveAPI();
		this.srlClient = SpeedrunsliveIRCConnectionManager.getInstance().getIRCClient();
		this.srlLiveSplitChannelName = "#srl-" + this.raceId + "-livesplit";
		srlClient.addChannel(this.srlLiveSplitChannelName);
		do {
			TimeUnit.SECONDS.sleep(5);
		} while(srlClient.getChannel(this.srlLiveSplitChannelName).isPresent());

		srlLiveSplitChannel = srlClient.getChannel(srlLiveSplitChannelName)
				.orElseThrow(() -> new IllegalArgumentException("LiveSplit channel could not be found"));

		this.twitchClient = TwitchIRCConnectionManager.getInstance().getIRCClient();
		exec.scheduleAtFixedRate(new RaceStateChecker(this), 0, 1, TimeUnit.MINUTES);
	}
	
	private void initialize() {
		Race race = this.api.getSingleRace(this.raceId);
		this.srlClient.getEventManager().registerEventListener(new RaceSplitTimeListener());
		List<String> usersInLiveSplitChannel = this.srlLiveSplitChannel.getNicknames();
		for(Entrant e : race.getEntrants()) {
			if(usersInLiveSplitChannel.contains(e.getUserName())) {
				this.runnersConnectedThroughLiveSplit.add(e);
				this.twitchClient.joinChannel(e.getTwitch().toLowerCase());
				this.twitchClient.sendMessage(CommonMessages.CHANNEL_ANNOUNCEMENT_JOIN, Channel.getChannel(e.getTwitch().toLowerCase(), this.twitchClient));
			}
		}
	}
	
	public void recordSplitTime(String splitName, String user, String time) {
		Entrant e = Optional.ofNullable(findEntrantByUsername(user))
				.orElse(getRunnerInRaceFromAPI(user));
		if(e != null) {
			if(this.getGame().getId() == 6) {
				String standardSplitName = getSplitNameByNameOrAlias(splitName);
				if(standardSplitName != null) {
					LOG.info("Found standardized name for {}, using {}", splitName, standardSplitName);
					splitName = standardSplitName;
				}
			}
			RaceSplit raceSplit = Optional.ofNullable(findRaceSplitByName(splitName))
					.orElse(new RaceSplit(splitName));
			raceSplit.addTime(e, time);
			splits.add(raceSplit);

			// check all splits in case someone dropped as last runner
			splits.forEach(this::announceSplitIfComplete);
		}
	}
	
	private Entrant getRunnerInRaceFromAPI(String user) {
		Race race = api.getSingleRace(this.raceId);
		for(Entrant e : race.getEntrants()) {
			if(e.getUserName().equalsIgnoreCase(user)) {
				return e;
			}
		}
		return null;
	}
	
	private void announceSplitIfComplete(RaceSplit rs) {
		if(this.announcedSplits.contains(rs.getSplitName())) {
			LOG.warn("Split {} has already been announced, not announcing again.", rs.getSplitName());
			return;
		}
		if(this.getGame().getId() == 6) {
			PKMNREDBLUE split = getSplitDataByNameOrAlias(rs.getSplitName());
			if(split != null && !split.isAnnounce()) {
				LOG.debug("Split {} is configured to not be announced.", split.getName());
				return;
			}
		}
		Race race = this.api.getSingleRace(this.raceId);
		for(Entrant e : this.runnersConnectedThroughLiveSplit) {
            Optional.ofNullable(findEntrantByUsername(e.getUserName(), race.getEntrants()))
                    .ifPresent(e::updateData);
			if(e.getState() == PlayerState.FORFEIT) {
				continue;
			}
			if(rs.findTimeForRunner(e) == null) {
				LOG.info(" {}is not finished with the split {} yet, not announcning.", e.getDisplayName(), rs.getSplitName());
				return;
			}
		}
		
		LOG.info("Announcing split {}", rs.getSplitName());
		
		StringBuilder message = new StringBuilder("Split " + rs.getSplitName() + " completed. Times: ");
		int position = 0;
		for(RaceSplit.SplitTime st : rs.getSplitTimes()) {
			message.append(++position)
					.append(". ")
					.append(st.getEntrantName())
					.append(" : ")
					.append(st.getDisplayTime())
					.append(" | ");
		}
		
		for(Entrant e : this.getRunnersConnectedThroughLiveSplit()) {
			if(e.getState() != PlayerState.FORFEIT) {
				LOG.info("Sending times to {}", e.getTwitch());
				this.twitchClient.sendMessage(message, Channel.getChannel(e.getTwitch().toLowerCase(), this.twitchClient));
			}
		}
		this.announcedSplits.add(rs.getSplitName());
	}
	
	private Entrant findEntrantByUsername(String user) {
		for(Entrant e : this.runnersConnectedThroughLiveSplit) {
			if(e.getUserName().equalsIgnoreCase(user)) {
				return e;
			}
		}
		return null;
	}
	
	private Entrant findEntrantByUsername(String user, List<Entrant> entrants) {
		for(Entrant e : entrants) {
			if(e.getUserName().equalsIgnoreCase(user)) {
				return e;
			}
		}
		return null;
	}
	
	private RaceSplit findRaceSplitByName(String splitName) {
		for(RaceSplit rs : this.splits) {
			if(rs.getSplitName().equalsIgnoreCase(splitName)) {
				return rs;
			}
		}
		return null;
	}
	
	void finishRace() {
		LOG.info("Finishing up race, parting all channels associated with this race.");
		for(Entrant e : this.getRunnersConnectedThroughLiveSplit()) {
			this.twitchClient.partChannel(e.getTwitch());
			LOG.debug("Left channel {}", e.getTwitch());
		}
		this.srlClient.removeChannel(srlLiveSplitChannelName);
	}
	
	List<Entrant> getRunnersConnectedThroughLiveSplit() {
		return runnersConnectedThroughLiveSplit;
	}
	
	private Game getGame() {
		return game;
	}

	public List<RaceSplit> getSplits() {
		return splits;
	}

	String getRaceId() {
		return raceId;
	}

	public SpeedrunsliveAPI getApi() {
		return api;
	}

	public ScheduledThreadPoolExecutor getExec() {
		return exec;
	}

	private class RaceStateChecker implements Runnable{

		private WatchedRace wr;
		RaceStateChecker(WatchedRace wr) {
			this.wr = wr;
		}
		
		@Override
		public void run() {
			Race race = wr.api.getSingleRace(wr.raceId);
			if(race.getState() == RaceState.IN_PROGRESS) {
				LOG.info("Initializing the race");
				wr.initialize();
				exec.shutdown();
			} else {
				LOG.info("Race {} has not started yet, waiting a minute.", wr.raceId);
			}
		}
		
	}
	
}
