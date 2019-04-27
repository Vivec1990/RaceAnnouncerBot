package com.vivec.jimbot.srl.api;

import com.google.gson.annotations.SerializedName;

public enum PlayerState {

	@SerializedName("Entered")
	ENTERED("Entered"),
	@SerializedName("Ready")
	READY("Ready"),
	@SerializedName("Finished")
	FINISHED("Finished"),
	@SerializedName("Forfeit")
	FORFEIT("Forfeit");
	
	private String stateText;
	
	PlayerState(String stateText) {
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
