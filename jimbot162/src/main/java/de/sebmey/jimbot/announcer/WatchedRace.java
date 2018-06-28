package de.sebmey.jimbot.announcer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.kitteh.irc.client.library.Client;

import com.cavariux.twitchirc.Chat.Channel;

import de.sebmey.jimbot.announcer.RaceSplit.SplitTime;
import de.sebmey.jimbot.irc.srl.RaceSplitTimeListener;
import de.sebmey.jimbot.irc.srl.SpeedrunsliveIRCConnectionManager;
import de.sebmey.jimbot.irc.twitch.TwitchIRCConnectionManager;
import de.sebmey.jimbot.irc.twitch.TwitchJim;
import de.sebmey.jimbot.srl.SpeedrunsliveAPI;
import de.sebmey.jimbot.srl.api.Entrant;
import de.sebmey.jimbot.srl.api.Game;
import de.sebmey.jimbot.srl.api.PlayerState;
import de.sebmey.jimbot.srl.api.Race;
import de.sebmey.jimbot.srl.api.RaceState;

public class WatchedRace {
	
	private List<Entrant> runnersConnectedThroughLiveSplit = new ArrayList<Entrant>();
	private Game game;
	private List<RaceSplit> splits;
	private String raceId;
	private Client srlClient;
	private TwitchJim twitchClient;
	private SpeedrunsliveAPI api;
	private String srlLiveSplitChannelName;
	private org.kitteh.irc.client.library.element.Channel srlLiveSplitChannel;
	
	private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1); 
	
	public WatchedRace(Race race) throws JSONException, IOException, InterruptedException {
		this.splits = new ArrayList<RaceSplit>();
		this.raceId = race.getId();
		this.api = new SpeedrunsliveAPI();
		this.srlClient = SpeedrunsliveIRCConnectionManager.getInstance().getIRCClient();
		this.srlLiveSplitChannelName = "#srl-" + this.raceId + "-livesplit";
		srlClient.addChannel(this.srlLiveSplitChannelName);
		do {
			TimeUnit.SECONDS.sleep(5);
		} while(!srlClient.getChannel(this.srlLiveSplitChannelName).isPresent());
		if(this.srlClient.getChannel(this.srlLiveSplitChannelName).isPresent()) {
			this.srlLiveSplitChannel = this.srlClient.getChannel(this.srlLiveSplitChannelName).get();
		} else {
			throw new IllegalArgumentException("LiveSplit channel could not be found");
		}
		this.twitchClient = TwitchIRCConnectionManager.getInstance().getIRCClient();
		exec.scheduleAtFixedRate(new RaceStateChecker(this), 0, 1, TimeUnit.MINUTES);
	}
	
	private void initalize() {
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
	
	public List<Entrant> getRunnersConnectedThroughLiveSplit() {
		return runnersConnectedThroughLiveSplit;
	}

	public Game getGame() {
		return game;
	}

	public List<RaceSplit> getSplits() {
		return splits;
	}

	public String getRaceId() {
		return raceId;
	}

	public SpeedrunsliveAPI getApi() {
		return api;
	}

	public ScheduledThreadPoolExecutor getExec() {
		return exec;
	}

	public void recordSplitTime(String splitName, String user, String time) {
		Entrant e = findEntrantByUsername(user);
		if(e != null) {
			RaceSplit rs = findRaceSplitByName(splitName);
			if(rs == null) {
				rs = new RaceSplit(splitName);
				this.splits.add(rs);
			}
			rs.addTime(e, time);
			this.announceSplitIfComplete(rs);
		}
	}
	
	private void announceSplitIfComplete(RaceSplit rs) {
		Race race = this.api.getSingleRace(this.raceId);
		for(Entrant e : this.runnersConnectedThroughLiveSplit) {
			e.updateData(findEntrantByUsername(e.getUserName(), race.getEntrants()));
			if(e.getState() == PlayerState.FORFEIT) {
				continue;
			}
			if(rs.findTimeForRunner(e) == null) {
				System.out.println(e.getDisplayName() + " is not finished with the split " + rs.getSplitName() + " yet, not announcning.");
				return;
			}
		}
		
		System.out.println("Announcing split " + rs.getSplitName());
		
		String message = "Split " + rs.getSplitName() + " completed. Times: ";
		int position = 0;
		for(SplitTime st : rs.getSplitTimes()) {
			message += ++position + ". " + st.getEntrantName() + " : " + st.getDisplayTime() + " | ";
		}
		
		for(Entrant e : this.getRunnersConnectedThroughLiveSplit()) {
			System.out.println("Sending times to " + e.getTwitch());
			this.twitchClient.sendMessage(message, Channel.getChannel(e.getTwitch().toLowerCase(), this.twitchClient));
		}
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
			if(rs.getSplitName().equals(splitName)) {
				return rs;
			}
		}
		return null;
	}
	
	public void finishRace() {
		System.out.println("Finishing up race, parting all channels associated with this race.");
		for(Entrant e : this.getRunnersConnectedThroughLiveSplit()) {
			this.twitchClient.partChannel(e.getTwitch());
			System.out.println("Left channel " + e.getTwitch());
		}
		this.srlClient.removeChannel(srlLiveSplitChannelName);
	}
	
	private class RaceStateChecker implements Runnable{

		private WatchedRace wr;
		public RaceStateChecker(WatchedRace wr) {
			this.wr = wr;
		}
		
		@Override
		public void run() {
			Race race = wr.api.getSingleRace(wr.raceId);
			if(race.getState() == RaceState.IN_PROGRESS) {
				System.out.println("Initalizing the race");
				wr.initalize();
				exec.shutdown();
			} else {
				System.out.println("Race " + wr.raceId + " has not started yet, waiting a minute. " + LocalDateTime.now().toString());
			}
		}
		
	}
	
}
