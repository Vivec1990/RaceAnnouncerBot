package com.vivec.jimbot.srl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vivec.jimbot.srl.api.Race;
import com.vivec.jimbot.srl.api.adapter.RaceDeserializer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.vivec.jimbot.srl.api.PlayerState.ENTERED;
import static com.vivec.jimbot.srl.api.PlayerState.READY;
import static com.vivec.jimbot.srl.api.RaceState.COMPLETE;
import static com.vivec.jimbot.srl.api.RaceState.TERMINATED;

public class SpeedrunsliveAPI {

	private static final Logger LOG = LogManager.getLogger(SpeedrunsliveAPI.class);

	private static final String JSON_RACES_RACES = "races";

	private OkHttpClient httpClient;
	private Request allRacesRequest;
	private final Gson gson;

	public SpeedrunsliveAPI() {
		this.httpClient = new OkHttpClient();
		this.allRacesRequest = new Request.Builder().url(RequestURLs.REQ_ALL_RACES).build();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Race.class, new RaceDeserializer());
		gson = gsonBuilder.create();
	}
	
	List<Race> getAllRaces() throws IOException {
		List<Race> allRaces = new ArrayList<>();
		Response allRacesResponse = httpClient.newCall(allRacesRequest).execute();

		JsonParser parser = new JsonParser();
		JsonObject allRacesJson = parser.parse(Objects.requireNonNull(allRacesResponse.body()).string()).getAsJsonObject();


		JsonArray raceList = allRacesJson.get(JSON_RACES_RACES).getAsJsonArray();
		raceList.forEach(jsonElement -> {
			Race race = gson.fromJson(jsonElement, Race.class);
			allRaces.add(race);
		});
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
		} catch (IOException e) {
			LOG.error("Error while retrieving data from SRL API", e);
		}
		return null;
	}
	
	public Race getSingleRace(String raceId) {
		Request singleRaceRequest = new Request.Builder().url(RequestURLs.getRequestForSingleRace(raceId)).build();
		try {
			Response response = httpClient.newCall(singleRaceRequest).execute();
			return gson.fromJson(response.body() != null ? response.body().string() : null, Race.class);
		} catch (IOException e) {
			LOG.error("Error while retrieving a single race", e);
		}
		return null;
	}
	
	private static class RequestURLs {
		static final String REQ_ALL_RACES = "http://api.speedrunslive.com:81/races";
		private static final String REQ_SINGLE_RACE = "http://api.speedrunslive.com:81/races/<race_id>";
		
		static String getRequestForSingleRace(String raceId) {
			return REQ_SINGLE_RACE.replace("<race_id>", raceId);
		}
	}
}
