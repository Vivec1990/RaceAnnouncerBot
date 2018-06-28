package de.sebmey.jimbot.srl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import de.sebmey.jimbot.srl.api.Entrant;
import de.sebmey.jimbot.srl.api.PlayerState;
import de.sebmey.jimbot.srl.api.Race;
import de.sebmey.jimbot.srl.api.RaceState;

public class SpeedrunsliveAPI {

	public static final String JSON_RACES_RACES = "races";
	public static final String JSON_RACES_COUNT = "count";
	
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
			List<Race> races = this.getAllRaces();
			for(Race r : races) {
				if(r.getState() == RaceState.TERMINATED || r.getState() == RaceState.COMPLETE) {
					//do not check finished/terminated races
					continue;
				}
				
				for(Entrant e : r.getEntrants()) {
					if(e.getState() == PlayerState.ENTERED || e.getState() == PlayerState.READY) {
						if(user.equalsIgnoreCase(e.getTwitch())) {
							return r;
						}
					}
				}
				
			}
		} catch (JSONException e) {
			System.err.println("Error while parsing JSON from SRL API");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error while retrieving data from SRL API");
			e.printStackTrace();
		}
		return null;
	}
	
	public Race getSingleRace(String raceId) {
		this.singleRaceRequest = new Request.Builder().url(RequestURLs.getRequestForSingleRace(raceId)).build();
		JSONObject jsonRace = new JSONObject();
		try {
			jsonRace = new JSONObject(httpClient.newCall(singleRaceRequest).execute().body().string());
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
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
//		String raceID = "4azqq";
//		Race foundRace = api.getSingleRace(raceID);
//		System.out.println(Arrays.toString(foundRace.getEntrants().toArray()));
		api.getAllRaces();
	}
	
}
