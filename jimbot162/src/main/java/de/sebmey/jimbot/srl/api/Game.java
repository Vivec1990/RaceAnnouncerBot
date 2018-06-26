package de.sebmey.jimbot.srl.api;

import org.json.JSONObject;

public class Game {

	private int id;
	private String name;
	private String abbreviation;
	private int popularity;
	private int popularityRank;
	
	public Game(JSONObject gameData) {
		int id;
		String name;
		String abbreviation;
		int popularity;
		int popularityRank;
		
		if(gameData.has(GameJSONKeys.GAME_ID)) {
			id = gameData.getInt(GameJSONKeys.GAME_ID);
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain an ID. It is probably not actual game data from the SRL api.");
		}
		
		if(gameData.has(GameJSONKeys.GAME_NAME)) {
			name = gameData.getString(GameJSONKeys.GAME_NAME);
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain a name. It is probably not actual game data from the SRL api.");
		}
		
		if(gameData.has(GameJSONKeys.GAME_ABBREVIATION)) {
			abbreviation = gameData.getString(GameJSONKeys.GAME_ABBREVIATION);
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain an abbreviation. It is probably not actual game data from the SRL api.");
		}
		
		if(gameData.has(GameJSONKeys.GAME_POPULARITY)) {
			popularity = gameData.getInt(GameJSONKeys.GAME_POPULARITY);
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain a popularity. It is probably not actual game data from the SRL api.");
		}
		
		if(gameData.has(GameJSONKeys.GAME_POPULARITY_RANK)) {
			popularityRank = gameData.getInt(GameJSONKeys.GAME_POPULARITY_RANK);
		} else {
			throw new IllegalArgumentException("The passed JSON Object does not contain a popularity rank. It is probably not actual game data from the SRL api.");
		}
		
		this.id = id;
		this.name = name;
		this.abbreviation = abbreviation;
		this.popularity = popularity;
		this.popularityRank = popularityRank;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getPopularityRank() {
		return popularityRank;
	}

	public void setPopularityRank(int popularityRank) {
		this.popularityRank = popularityRank;
	}

	private class GameJSONKeys {
		public static final String GAME_ID = "id";
		public static final String GAME_NAME = "name";
		public static final String GAME_ABBREVIATION = "abbrev";
		public static final String GAME_POPULARITY = "popularity";
		public static final String GAME_POPULARITY_RANK = "popularityrank";
	}
	
}
