package com.vivec.jimbot.irc.twitch;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;
import com.vivec.jimbot.announcer.RaceAnnouncer;
import com.vivec.jimbot.announcer.WatchedRace;
import com.vivec.jimbot.srl.SpeedrunsliveAPI;
import com.vivec.jimbot.srl.api.Race;

public class TwitchJim extends TwitchBot {

	private Channel botChannel;
	private SpeedrunsliveAPI api;
	
	TwitchJim(String username, String oauth) {
		this.setUsername(username);
		this.setOauth_Key(oauth);
		this.connect();
		this.botChannel  = this.joinChannel(username);
		api = new SpeedrunsliveAPI();
	}
	
	@Override
	public void onMessage(User user, Channel channel, String message) {
		// do nothing
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onCommand(User user, Channel channel, String command) {
		if(channel.equals(this.botChannel)) {

			RaceAnnouncer raceAnnouncer = RaceAnnouncer.getInstance();
			if ("race".equalsIgnoreCase(command)) {
				Race race = api.findRaceWithTwitchUser(user.toString());
				if (race == null) {
					this.sendMessage("@" + user.toString() + " You are not part of a race on speedrunslive.com right now.", channel);
				} else if (raceAnnouncer.getRaceByID(race.getId()) != null) {
					this.sendMessage("@" + user.toString() + " The race you are in is already managed, no need to initialize twice.", channel);
				} else {
						raceAnnouncer.addRace(new WatchedRace(race));
						this.sendMessage("@" + user.toString() + " The race was added and will be announced once splits are completed by all entrants. Good luck & have fun!", channel);
				}
			} else if (command.startsWith("race ")) {
				String raceId = command.substring("race ".length());
				Race race = api.getSingleRace(raceId);
				if (race == null) {
					this.sendMessage("@" + user.toString() + " No race with the offered ID has been found.", channel);
				} else {
						raceAnnouncer.addRace(new WatchedRace(race));
						this.sendMessage("@" + user.toString() + " The race was added and will be announced once splits are completed by all entrants. Good luck & have fun!", channel);
				}
			} else if (command.startsWith("addtime ")) { // e.g. !addtime Headbob "Lance" 1:49:23.00
				String data = command.substring("addtime ".length());
				String[] parts = data.split("\"");
				if (parts.length < 2) return;
				String splitRunner = parts[0].trim();
				String splitName = parts[1].trim();
				String splitTime = parts[2].trim();

				Race race = api.findRaceWithTwitchUser(splitRunner);
				if (race == null) {
					this.sendMessage(splitRunner + " is not part of a race on speedrunslive.com right now.", channel);
				} else {
					WatchedRace wr = raceAnnouncer.getRaceByID(race.getId());
					if (wr != null) {
						wr.recordSplitTime(splitName, splitRunner, splitTime);
						this.sendMessage("Recorded the SplitTime of " + splitRunner + " at " + splitName + " with a time of " + splitTime + ".", channel);
					} else {
						this.sendMessage("The race in which " + splitRunner + " is part of couldn't be found on speedrunslive.com.", channel);
					}
				}
			}
		}

		if(command.equalsIgnoreCase("raceinfo") || command.equalsIgnoreCase("racestats")) {
			Race r = api.findRaceWithTwitchUser(channel.toString().substring(1));
			if(r != null) {
				String[] raceInfo = r.getRaceInfo();
				this.sendMessage(raceInfo[0], channel);
				if(raceInfo[1] != null && !raceInfo[1].isEmpty()) {
					this.sendMessage(raceInfo[1], channel);
				}
			}
		}
	}
	
}
