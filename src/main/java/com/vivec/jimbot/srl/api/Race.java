package com.vivec.jimbot.srl.api;

import java.util.List;

public class Race {
	
	private String id;
	private Game game;
	private String goal;
	private long time;
	private RaceState state;
	private List<Entrant> entrants;

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
		String[] result = new String[2];
		int entrantNum = this.getEntrants().size();
		int forfeits = 0;
		StringBuilder linkBuilder = new StringBuilder("http://kadgar.net/live");
		for(Entrant e : this.getEntrants()) {
			if(e.getState() == PlayerState.FORFEIT) {
				forfeits++;
			}
			if(e.getTwitch() != null) {
				linkBuilder.append("/").append(e.getTwitch());
			}
		}
		String link = linkBuilder.toString();
		StringBuilder builder = new StringBuilder();
		builder.append("| Game: ").append(this.getGame().getName()).append(" - ").append(this.getGoal());
		switch(this.getState()) {
			case ENTRY_OPEN: 
				builder.append(" | Status: Waiting for entrants").append(" | Racers: ").append(entrantNum).append(" entrants.");
				result[1] = link;
				break;
			case IN_PROGRESS:
				builder.append(" | Status: Race in progress").append(" | Racers: ").append(entrantNum).append(" entrants, ").append(entrantNum - forfeits).append(" still running, ").append(forfeits).append(" forfeited.");
				result[1] = link;
				break;
			case TERMINATED:
			case COMPLETE:
				builder.append(" | Status: Race over").append(" | ").append(entrantNum).append(" total entrants, ").append(entrantNum - forfeits).append(" completed the race, ").append(forfeits).append(" forfeited.");
				break;
			case CURRENTLY_UNKNOWN:
				break;
		}
		result[0] = builder.toString();
		return result;
	}

}
