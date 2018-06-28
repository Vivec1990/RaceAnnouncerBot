package de.sebmey.jimbot.irc.twitch;

import java.io.IOException;

import org.json.JSONException;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;

import de.sebmey.jimbot.announcer.RaceAnnouncer;
import de.sebmey.jimbot.announcer.WatchedRace;
import de.sebmey.jimbot.srl.SpeedrunsliveAPI;
import de.sebmey.jimbot.srl.api.Race;

public class TwitchJim extends TwitchBot {

	private Channel botChannel;
	private SpeedrunsliveAPI api;
	
	public TwitchJim(String username, String oauth) {
		this.setUsername(username);
		this.setOauth_Key(oauth);
		this.connect();
		this.botChannel  = this.joinChannel(username);
		api = new SpeedrunsliveAPI();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onCommand(User user, Channel channel, String command) {
		if(channel.equals(this.botChannel)) {
			if("race".equalsIgnoreCase(command)) {
				
				Race race = api.findRaceWithTwitchUser(user.toString());
				if(race == null) {
					this.sendMessage("@"+user.toString() + " You are not part of a race on speedrunslive.com right now.", channel);
				} else if(RaceAnnouncer.getInstance().getRaceByID(race.getId()) != null) {
					this.sendMessage("@"+user.toString() + " The race you are in is already managed, no need to initialize twice.", channel);
				} else {
					try {
						RaceAnnouncer.getInstance().addRace(new WatchedRace(race));
						this.sendMessage("@"+user.toString() + " The race was added and will be announced once splits are completed by all entrants. Good luck & have fun!", channel);
					} catch (JSONException | IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else if(command.startsWith("race ")) {
				String raceId = command.substring("race ".length());
				Race race = api.getSingleRace(raceId);
				if(race == null) {
					this.sendMessage("@"+user.toString() + " No race with the offered ID has been found.", channel);
				} else {
					try {
						RaceAnnouncer.getInstance().addRace(new WatchedRace(race));
						this.sendMessage("@"+user.toString() + " The race was added and will be announced once splits are completed by all entrants. Good luck & have fun!", channel);
					} catch (JSONException | IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
