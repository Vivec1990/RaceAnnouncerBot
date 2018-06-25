package de.sebmey.jimbot.srl.api;

public enum RaceState {

	ENTRY_OPEN(1, "Entry Open"),
	CURRENTLY_UNKNOWN(2, "???"),
	IN_PROGRESS(3, "In Progress"),
	COMPLETE(4, "Complete"),
	TERMINATED(5, "Terminated");
	
	private int stateId;
	private String stateText;
	
	private RaceState(int stateId, String stateText) {
		this.stateId = stateId;
		this.stateText = stateText;
	}
	
	public int getStateID() {
		return this.stateId;
	}
	
	public String getStateText() {
		return this.stateText;
	}
	
	public static RaceState byID(int stateID) {
		for(RaceState state : RaceState.values()) {
			if(state.getStateID() == stateID) {
				return state;
			}
		}
		return null;
	}
}
