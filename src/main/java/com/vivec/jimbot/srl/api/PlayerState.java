package com.vivec.jimbot.srl.api;

public enum PlayerState {

	ENTERED("Entered"),
	READY("Ready"),
	FINISHED("Finished"),
	FORFEIT("Forfeit");
	
	private String stateText;
	
	private PlayerState(String stateText) {
		this.stateText = stateText;
	}
	
	public static PlayerState byStateText(String stateText) {
		for(PlayerState state : PlayerState.values()) {
			if(stateText.equals(state.getStateText())) {
				return state;
			}
		}
		return null;
	}
	
	public String getStateText() {
		return this.stateText;
	}
}
