package com.vivec.jimbot.srl.api;


public class Game {

	private int id;
	private String name;
	private String abbreviation;
	private int popularity;
	private int popularityRank;
	
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

}
