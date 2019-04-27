package com.vivec.jimbot.srl.api;

import com.google.gson.annotations.SerializedName;

public class Entrant {
	private String userName;
	@SerializedName("displayname")
	private String displayName;
	private long place;
	private long time;
	private String message;
	@SerializedName("statetext")
	private PlayerState state;
	private String twitch;
	private long trueskill; //SRL points
	
	public void updateData(Entrant entrant) {
		this.displayName = entrant.getDisplayName();
		this.userName = entrant.getUserName();
		this.trueskill = entrant.getTrueskill();
		this.twitch = entrant.getTwitch();
		this.state = entrant.getState();
		this.message = entrant.getMessage();
		this.place = entrant.getPlace();
		this.time = entrant.getTime();
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public long getPlace() {
		return place;
	}

	public void setPlace(long place) {
		this.place = place;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public String getTwitch() {
		return twitch;
	}

	public void setTwitch(String twitch) {
		this.twitch = twitch;
	}

	public long getTrueskill() {
		return trueskill;
	}

	public void setTrueskill(long trueskill) {
		this.trueskill = trueskill;
	}

	@Override
	public String toString() {
		String result = "";
		result += "[";
		result += this.getUserName() + ", ";
		result += this.getDisplayName() + ", ";
		result += this.getTwitch() + ", ";
		result += this.getState();
		result += "]";
		return result;
	}
	
}
