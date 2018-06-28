package de.sebmey.jimbot.irc.twitch;

import de.sebmey.jimbot.irc.CredentialManager;

public class TwitchIRCConnectionManager {

	private static TwitchIRCConnectionManager INSTANCE;
	private TwitchJim ircClient;
	
	public TwitchIRCConnectionManager() {
		ircClient = new TwitchJim(CredentialManager.getInstance().getBotUsername(), CredentialManager.getInstance().getBotTwitchOAuth());
	}
	
	public TwitchJim getIRCClient() {
		return this.ircClient;
	}
	
	public static TwitchIRCConnectionManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new TwitchIRCConnectionManager();
		}
		return INSTANCE;
	}
	
	
	
}
