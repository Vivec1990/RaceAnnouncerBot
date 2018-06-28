package de.sebmey.jimbot.srl.api;

import org.json.JSONObject;

public class Entrant {

	private String userName;
	private String displayName;
	private long place;
	private long time;
	private String message;
	private PlayerState state;
	private String twitch;
	private long trueskill; //SRL points
	
	public Entrant(JSONObject entrantData, String userName) {
		this.userName = userName;
		
		updateData(entrantData);
	}
	
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
	
	public void updateData(JSONObject entrantData) {
		if(entrantData.has(EntrantJSONKeys.ENTRANT_DISPLAYNAME)) {
			displayName = entrantData.getString(EntrantJSONKeys.ENTRANT_DISPLAYNAME);
		} else {
			throw new IllegalArgumentException("The JSON data does not contain a displayname, it probably is not a proper result from the SRL api");
		}
		
		if(entrantData.has(EntrantJSONKeys.ENTRANT_PLACE)) {
			place = entrantData.getLong(EntrantJSONKeys.ENTRANT_PLACE);
		} else {
			throw new IllegalArgumentException("The JSON data does not contain a place, it probably is not a proper result from the SRL api");
		}
		
		if(entrantData.has(EntrantJSONKeys.ENTRANT_TIME)) {
			time = entrantData.getLong(EntrantJSONKeys.ENTRANT_TIME);
		} else {
			throw new IllegalArgumentException("The JSON data does not contain a time, it probably is not a proper result from the SRL api");
		}
		
		if(entrantData.has(EntrantJSONKeys.ENTRANT_MESSAGE)) {
			Object mObj = entrantData.get(EntrantJSONKeys.ENTRANT_MESSAGE);
			if(mObj != null) {
				message = mObj.toString();
			} else {
				message = null;
			}
		} else {
			throw new IllegalArgumentException("The JSON data does not contain a message, it probably is not a proper result from the SRL api");
		}
		
		if(entrantData.has(EntrantJSONKeys.ENTRANT_STATE)) {
			state = PlayerState.byStateText(entrantData.getString(EntrantJSONKeys.ENTRANT_STATE));
		} else {
			throw new IllegalArgumentException("The JSON data does not contain a state, it probably is not a proper result from the SRL api");
		}
		
		if(entrantData.has(EntrantJSONKeys.ENTRANT_TWITCH)) {
			twitch = entrantData.getString(EntrantJSONKeys.ENTRANT_TWITCH);
		} else {
			throw new IllegalArgumentException("The JSON data does not contain a twitch, it probably is not a proper result from the SRL api");
		}
		
		if(entrantData.has(EntrantJSONKeys.ENTRANT_TRUESKILL)) {
			trueskill = entrantData.getLong(EntrantJSONKeys.ENTRANT_TRUESKILL);
		} else {
			throw new IllegalArgumentException("The JSON data does not contain a trueskill, it probably is not a proper result from the SRL api");
		}
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

	private class EntrantJSONKeys {
		public static final String ENTRANT_DISPLAYNAME = "displayname";
		public static final String ENTRANT_PLACE = "place";
		public static final String ENTRANT_TIME = "time";
		public static final String ENTRANT_MESSAGE = "message";
		public static final String ENTRANT_STATE = "statetext";
		public static final String ENTRANT_TWITCH = "twitch";
		public static final String ENTRANT_TRUESKILL = "trueskill";
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
