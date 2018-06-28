package de.sebmey.jimbot.irc.srl;

import org.kitteh.irc.client.library.Client;

import de.sebmey.jimbot.irc.CredentialManager;

public class SpeedrunsliveIRCConnectionManager {

	private static SpeedrunsliveIRCConnectionManager INSTANCE;
	private Client ircClient;
	
	public SpeedrunsliveIRCConnectionManager() {
		ircClient = Client.builder().nick(CredentialManager.getInstance().getBotUsername()).serverHost("irc.speedrunslive.com").serverPort(6667).secure(false).serverPassword(CredentialManager.getInstance().getBotSRLPass()).build();
		ircClient.connect();
	}
	
	public Client getIRCClient() {
		return this.ircClient;
	}
	
	public static SpeedrunsliveIRCConnectionManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SpeedrunsliveIRCConnectionManager();
		}
		return INSTANCE;
	}
	
}
