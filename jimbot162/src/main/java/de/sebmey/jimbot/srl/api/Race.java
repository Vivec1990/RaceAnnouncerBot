package de.sebmey.jimbot.srl.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Race {
	
	private String id;
	private Game game;
	private String goal;
	private long time;
	private RaceState state;
	private List<Entrant> entrants;
	
	public Race(JSONObject raceData) {
		String id;
		Game game;
		String goal;
		long time;
		RaceState state = null;
		List<Entrant> entrants;
		
		if(raceData.has(RaceJSONKeys.RACE_ID)) {
			id = raceData.getString(RaceJSONKeys.RACE_ID);
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain a race id. It is probably not actual race data from the SRL api.");
		}
		
		if(raceData.has(RaceJSONKeys.RACE_GAME)) {
			game = new Game(raceData.getJSONObject(RaceJSONKeys.RACE_GAME));
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain a game. It is probably not actual race data from the SRL api.");
		}
		
		if(raceData.has(RaceJSONKeys.RACE_GOAL)) {
			goal = raceData.getString(RaceJSONKeys.RACE_GOAL);
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain a goal. It is probably not actual race data from the SRL api.");
		}
		
		if(raceData.has(RaceJSONKeys.RACE_TIME)) {
			time = raceData.getLong(RaceJSONKeys.RACE_TIME);
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain a time. It is probably not actual race data from the SRL api.");
		}
		
		if(raceData.has(RaceJSONKeys.RACE_STATE)) {
			state = RaceState.byID(raceData.getInt(RaceJSONKeys.RACE_STATE));
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain a race state. It is probably not actual race data from the SRL api.");
		}
		
		if(raceData.has(RaceJSONKeys.RACE_ENTRANTS)) {
			entrants = new ArrayList<Entrant>();
			for(String entrantName : raceData.getJSONObject(RaceJSONKeys.RACE_ENTRANTS).keySet()) {
				Entrant entrant = new Entrant(raceData.getJSONObject(RaceJSONKeys.RACE_ENTRANTS).getJSONObject(entrantName), entrantName);
				entrants.add(entrant);
			}
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain any entrants, the bot should not have initialized like this at all. It is probably not actual race data from the SRL api.");
		}
		
		this.id = id;
		this.game = game;
		this.goal = goal;
		this.time = time;
		this.state = state;
		this.entrants = entrants;
	}
	
	public Race(String id, Game game, String goal, long time, RaceState state, List<Entrant> entrants) {
		this.id = id;
		this.game = game;
		this.goal = goal;
		this.time = time;
		this.state = state;
		this.entrants = entrants;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public RaceState getState() {
		return state;
	}

	public void setState(RaceState state) {
		this.state = state;
	}

	public List<Entrant> getEntrants() {
		return entrants;
	}

	public void setEntrants(List<Entrant> entrants) {
		this.entrants = entrants;
	}
	
	public String[] getRaceInfo() {
		String[] result = {};
		int entrantNum = this.getEntrants().size();
		int forfeits = 0;
		String link = "http://kadgar.net/live";
		for(Entrant e : this.getEntrants()) {
			if(e.getState() == PlayerState.FORFEIT) {
				forfeits++;
			}
			if(e.getTwitch() != null) {
				link += "/"+e.getTwitch();
			}
		}
		StringBuilder builder = new StringBuilder();
		builder.append("| Game: " + this.getGame().getName() + " - " + this.getGoal());
		switch(this.getState()) {
			case ENTRY_OPEN: 
				builder.append(" | Status: Waiting for entrants")
				.append(" | Racers: " + entrantNum + " entrants.");
				result[1] = link;
				break;
			case IN_PROGRESS:
				builder.append(" | Status: Race in progress")
				.append(" | Racers: " + entrantNum + " entrants, " + (entrantNum-forfeits) + " still running, " + forfeits + " forfeited.");
				result[1] = link;
				break;
			case TERMINATED:
			case COMPLETE:
				builder.append(" | Status: Race over")
				.append(" | " + entrantNum + " total entrants, " + (entrantNum-forfeits) + " completed the race, " + forfeits + " forfeited.");
				break;
			case CURRENTLY_UNKNOWN:
				break;
		}
		result[0] = builder.toString();
		return result;
	}

	private class RaceJSONKeys {
		public static final String RACE_ID = "id";
		public static final String RACE_GAME = "game";
		public static final String RACE_GOAL = "goal";
		public static final String RACE_TIME = "time";
		public static final String RACE_STATE = "state";
		public static final String RACE_ENTRANTS = "entrants";
	}
	
}
