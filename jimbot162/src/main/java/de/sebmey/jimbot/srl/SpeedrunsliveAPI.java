package de.sebmey.jimbot.srl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import de.sebmey.jimbot.srl.api.Race;

public class SpeedrunsliveAPI {

	public static final String JSON_RACES_RACES = "races";
	
	private OkHttpClient httpClient;
	private Request allRacesRequest;
	private Request singleRaceRequest;

	public SpeedrunsliveAPI() {
		this.httpClient = new OkHttpClient();
		this.allRacesRequest = new Request.Builder().url(RequestURLs.REQ_ALL_RACES).build();
	}
	
	public List<Race> getAllRaces() throws JSONException, IOException {
		List<Race> allRaces = new ArrayList<Race>();
		JSONObject jsonAllRaces = new JSONObject(httpClient.newCall(allRacesRequest).execute().body().string());
		
		JSONObject raceList = jsonAllRaces.getJSONObject(JSON_RACES_RACES);
		for(String raceIndex : raceList.keySet()) {
			JSONObject jsonRace = raceList.getJSONObject(raceIndex);
			allRaces.add(new Race(jsonRace));
		}
		return allRaces;
	}
	
	public Race getSingleRace(String raceId) throws JSONException, IOException {
		this.singleRaceRequest = new Request.Builder().url(RequestURLs.getRequestForSingleRace(raceId)).build();
		JSONObject jsonRace = new JSONObject(httpClient.newCall(singleRaceRequest).execute().body().string());
		return new Race(jsonRace);
	}
	
	private static class RequestURLs {
		public static final String REQ_ALL_RACES = "http://api.speedrunslive.com:81/races";
		private static final String REQ_SINGLE_RACE = "http://api.speedrunslive.com:81/races/<race_id>";
		
		public static final String getRequestForSingleRace(String raceId) {
			return REQ_SINGLE_RACE.replace("<race_id>", raceId);
		}
	}
	
	public static void main(String[] args) throws JSONException, IOException {
		SpeedrunsliveAPI api = new SpeedrunsliveAPI();
		String raceID = "4azqq";
		Race foundRace = api.getSingleRace(raceID);
		System.out.println(Arrays.toString(foundRace.getEntrants().toArray()));
	}
	
}
