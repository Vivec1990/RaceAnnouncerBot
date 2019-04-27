package com.vivec.jimbot.irc;

public class CredentialManager {
	private String botUsername;
	private String botTwitchOAuth;
	private String botSRLPass;
	
	private static CredentialManager INSTANCE = new CredentialManager();
	
	public static CredentialManager getInstance() {
		return INSTANCE;
	}
	
	private CredentialManager() {
		
	}
	
	public String getBotUsername() {
		return botUsername;
	}
	public void setBotUsername(String botUsername) {
		this.botUsername = botUsername;
	}
	public String getBotTwitchOAuth() {
		return botTwitchOAuth;
	}
	public  void setBotTwitchOAuth(String botTwitchOAuth) {
		this.botTwitchOAuth = botTwitchOAuth;
	}
	public  String getBotSRLPass() {
		return botSRLPass;
	}
	public  void setBotSRLPass(String botSRLPass) {
		this.botSRLPass = botSRLPass;
	}
}
