package de.sebmey.jimbot;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.engio.mbassy.listener.Handler;

public class MessageListener {

	@Handler
	public void listenForRaceCommand(ChannelMessageEvent event) throws IOException {
		Client jim = event.getClient();
		Channel chan = event.getChannel();
		User author = event.getActor();	
		String message = event.getMessage();
		
		if(message.equals("!race")) {
			OkHttpClient client = new OkHttpClient();
			Request currentRaces = new Request.Builder().url("http://api.speedrunslive.com:81/races").build();
			Response response = client.newCall(currentRaces).execute();
			JSONObject races = new JSONObject(response.body().string());
			
			JSONArray allRaces = races.getJSONArray("races");
			
			String raceId = findRaceWithUser(allRaces, author.getNick());
			if(raceId != null) {
				Request raceData = new Request.Builder().url("http://api.speedrunslive.com:81/races/"+raceId).build();
				JSONObject race = new JSONObject(client.newCall(raceData).execute().body().string());
				new RaceWatcher(race, jim);
			} else {
				jim.sendMessage(event.getChannel(), "@"+author.getNick()+" You are not part of a race right now.");
			}
		}
	}
	
	@NonNull
	private static Stream<Object> arrayToStream(JSONArray array) {
	    return StreamSupport.stream(array.spliterator(), false);
	}
	
	private String findRaceWithUser(JSONArray races, String user) {
		final String race = null;
		Optional<JSONObject> raceWithUser = arrayToStream(races)
			.map(JSONObject.class::cast)
			.filter(r -> (r.getInt("state") < 4))
			.filter(r -> userHasEntered(r, user))
			.findFirst();
		return raceWithUser.isPresent() ? raceWithUser.get().getString("id") : null;
	}
	
	private boolean userHasEntered(JSONObject race, String user) {
		JSONObject entrants = race.getJSONObject("entrants");
		return entrants.has(user);
	}
	
}
