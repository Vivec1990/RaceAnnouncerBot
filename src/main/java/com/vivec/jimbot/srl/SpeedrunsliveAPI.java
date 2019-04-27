package com.vivec.jimbot.srl;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.vivec.jimbot.srl.api.Race;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.vivec.jimbot.srl.api.PlayerState.ENTERED;
import static com.vivec.jimbot.srl.api.PlayerState.READY;
import static com.vivec.jimbot.srl.api.RaceState.COMPLETE;
import static com.vivec.jimbot.srl.api.RaceState.TERMINATED;

public class SpeedrunsliveAPI {

	private static final Logger LOG = LogManager.getLogger(SpeedrunsliveAPI.class);

	private static final String JSON_RACES_RACES = "races";
	private static final String JSON_RACES_COUNT = "count";
	
	private OkHttpClient httpClient;
	private Request allRacesRequest;

	public SpeedrunsliveAPI() {
		this.httpClient = new OkHttpClient();
		this.allRacesRequest = new Request.Builder().url(RequestURLs.REQ_ALL_RACES).build();
	}
	
	private List<Race> getAllRaces() throws IOException {
		List<Race> allRaces = new ArrayList<>();
		JSONObject jsonAllRaces = new JSONObject(httpClient.newCall(allRacesRequest).execute().body().string());
		
		JSONArray raceList = jsonAllRaces.getJSONArray(JSON_RACES_RACES);
		for(int i = 0; i < jsonAllRaces.getInt(JSON_RACES_COUNT); i++) {
			JSONObject jRace = raceList.getJSONObject(i);
			Race r = new Race(jRace);
			allRaces.add(r);
		}
		return allRaces;
	}
	
	public Race findRaceWithTwitchUser(String user) {
		try {
			return getAllRaces()
					.stream()
					.filter(r -> !(TERMINATED == r.getState() || COMPLETE == r.getState()))
					.filter(r -> r.getEntrants()
							.stream()
							.filter(e -> ENTERED == e.getState() || READY == e.getState())
							.anyMatch(e -> user.equalsIgnoreCase(e.getTwitch())))
					.findFirst()
					.orElse(null);
		} catch (JSONException e) {
			LOG.error("Error while parsing JSON from SRL API", e);
		} catch (IOException e) {
			LOG.error("Error while retrieving data from SRL API", e);
		}
		return null;
	}
	
	public Race getSingleRace(String raceId) {
		Request singleRaceRequest = new Request.Builder().url(RequestURLs.getRequestForSingleRace(raceId)).build();
		JSONObject jsonRace = new JSONObject();
		try {
			jsonRace = new JSONObject(httpClient.newCall(singleRaceRequest).execute().body().string());
		} catch (JSONException | IOException e) {
			LOG.error("Error while retrieving a single race", e);
		}
		return new Race(jsonRace);
	}
	
	private static class RequestURLs {
		static final String REQ_ALL_RACES = "http://api.speedrunslive.com:81/races";
		private static final String REQ_SINGLE_RACE = "http://api.speedrunslive.com:81/races/<race_id>";
		
		static String getRequestForSingleRace(String raceId) {
			return REQ_SINGLE_RACE.replace("<race_id>", raceId);
		}
	}
}
