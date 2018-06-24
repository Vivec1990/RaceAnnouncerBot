package de.sebmey.jimbot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import net.engio.mbassy.listener.Handler;

public class RaceWatcher {
	
	private static Client srlJim;
	private Client twitchJim;
	private String raceId;
	
	private WaitForGoListener wfgl;
	OkHttpClient httpClient = new OkHttpClient();
	Request raceDataRequest;
	
	private Map<String, Map<String, String>> splitTimes = new LinkedHashMap<String, Map<String, String>>();
	
	List<String> announcedSplits = new ArrayList<String>();
	List<String> forfeits = new ArrayList<String>();
	
	public RaceWatcher(JSONObject raceData, Client twitchJim) {
		this.raceDataRequest = new Request.Builder().url("http://api.speedrunslive.com:81/races/"+this.raceId).build();
		this.raceId = raceData.getString(SRLApiKeys.KEYS_ID);
		if(srlJim == null) {
			srlJim = Client.builder().nick(JimBot162.getBotName()).serverHost("irc.speedrunslive.com").serverPort(6667).secure(false).serverPassword(JimBot162.getSrlPass()).buildAndConnect();
		}
		srlJim.addChannel("#srl-"+this.raceId+"-livesplit");
		srlJim.addChannel("#"+this.raceId);
//		srlJim.getEventManager().registerEventListener(this);
		this.wfgl = new WaitForGoListener(this);
		srlJim.getEventManager().registerEventListener(wfgl);
		this.twitchJim = twitchJim;
	}
	
	private void initialize() throws JSONException, IOException {
		srlJim.getEventManager().unregisterEventListener(wfgl);
		srlJim.getEventManager().registerEventListener(this);
		JSONObject raceData = new JSONObject(httpClient.newCall(this.raceDataRequest).execute().body().string());
		JSONObject jsonEntrants = raceData.getJSONObject(SRLApiKeys.KEYS_ENTRANTS);
		for(String entrant : jsonEntrants.keySet()) {
			String channelName = "#" + jsonEntrants.getJSONObject(entrant).getString(SRLApiKeys.KEYS_ENTRANTS_TWITCH);
			twitchJim.addChannel(channelName);
			twitchJim.sendMessage(channelName, "Hi, I am JimBot and I will announce the times of the other racers here.");
		}
	}
	
	private class WaitForGoListener {
		
		private RaceWatcher rw;
		public WaitForGoListener(RaceWatcher rw) {
			this.rw = rw;
		}
		
		@Handler
		private void ircMessageReceived(ChannelMessageEvent event) throws JSONException, IOException {
			User author = event.getActor();
			String message = event.getMessage();
			if(author.getNick().equalsIgnoreCase("RaceBot")) return;
			if(!message.contains("GO!")) return;
			rw.initialize();
		}
		
	}
	
	@Handler
	private void livesplitUpdateReceived(ChannelMessageEvent event) {
		String message = event.getMessage();
		if(!message.startsWith("!time RealTime ")) {
			return;
		}
		String user = event.getActor().getNick();
		String data = message.substring("!time RealTime ".length()).trim();
		String[] parts = data.split("\"");
		if(parts.length < 2) return;
		String splitName = parts[1].trim();
		String splitTime = parts[2].trim();
		
		if(!splitTimes.containsKey(splitName)) {
			splitTimes.put(splitName, new LinkedHashMap<String, String>());
		}
		System.out.println("Spit data collected for user " + user + " and split " + splitName + ". The time was " + splitTime);
		if(splitTime.trim().equals("-") && splitTimes.get(splitName).containsKey(user)) {
			splitTimes.get(splitName).remove(user);
		} else if(!splitTime.trim().equals("-")){
			splitTimes.get(splitName).put(user, splitTime);
		}
		
		checkAndAnnounce(splitName);
	}
	
	private void checkAndAnnounce(String splitName) {
		System.out.println("Checking " + splitName);
		if(announcedSplits.contains(splitName)) return;
		Map<String, String> splitData = splitTimes.get(splitName);
		System.out.println(splitData.size() + " people have completed the split " + splitName);
		try {
			JSONObject race = new JSONObject(httpClient.newCall(this.raceDataRequest).execute().body().string());
			boolean raceComplete = race.getInt(SRLApiKeys.KEYS_STATE) >= 4;
			JSONObject entrants = race.getJSONObject(SRLApiKeys.KEYS_ENTRANTS);
			
			for(String entrant : entrants.keySet()) {
				JSONObject entr = entrants.getJSONObject(entrant);
				String status = entr.getString(SRLApiKeys.KEYS_ENTRANTS_STATUS);
				System.out.println("Entrant " + entrant + ", Status: " + status);
				if("Forfeit".equals(status)) {
					System.out.println(entrant + " has forfeit, no longer needed to check.");
					if(!forfeits.contains(entrant)) {
						twitchJim.sendMessage("#"+entrant, "You forfeit from the race, the times won't be announced here anymore.");
					}
					forfeits.add(entrant);
					continue;
				} else if(forfeits.contains(entrant)){
					forfeits.remove(entrant);
					twitchJim.sendMessage("#"+entrant, "Welcome back in the race. Announcements are enabled again.");
				}
				if(!splitData.containsKey(entrant)) {
					System.out.println("Not everyone has completed the split, not announcing yet");
					return;
				}
			}
			announceSplit(splitName, raceComplete);
			if(raceComplete) {
				twitchJim.getEventManager().unregisterEventListener(this);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void announceSplit(String splitName, boolean raceComplete) {
		System.out.println("Announcing split " + splitName + " in all twitch chats.");
		Map<String, String> splitData = splitTimes.get(splitName);
		StringBuilder message = new StringBuilder();
//		message.append("Users still in the race: " + splitData.keySet().size() + " - ");
		message.append(splitName + " split completed: ");
		int position = 0;
		for(String user : splitData.keySet()) {
			message.append(++position + ". " + user + ": " + splitData.get(user) + " | ");
		}
		message.deleteCharAt(message.length()-2);
		message.deleteCharAt(message.length()-1);
		
		for(Channel chan : twitchJim.getChannels()) {
			if(forfeits.contains(chan.getName().substring(1))) {
				continue;
			}
			chan.sendMultiLineMessage(message.toString());
			System.out.println("Announced split " + splitName + " in " + chan.getName());
			if(raceComplete) twitchJim.removeChannel(chan.getName());
		}
		announcedSplits.add(splitName);
	}
	
}
