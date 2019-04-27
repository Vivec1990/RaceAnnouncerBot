package com.vivec.jimbot.irc.srl;

import com.vivec.jimbot.irc.CredentialManager;
import org.kitteh.irc.client.library.Client;

public class SpeedrunsliveIRCConnectionManager {

	private static SpeedrunsliveIRCConnectionManager instance;
	private Client ircClient;
	
	public SpeedrunsliveIRCConnectionManager() {
		ircClient = Client.builder().nick(CredentialManager.getInstance().getBotUsername()).serverHost("irc.speedrunslive.com").serverPort(6667).secure(false).serverPassword(CredentialManager.getInstance().getBotSRLPass()).build();
		ircClient.connect();
	}
	
	public Client getIRCClient() {
		return this.ircClient;
	}
	
	public static SpeedrunsliveIRCConnectionManager getInstance() {
		if(instance == null) {
			instance = new SpeedrunsliveIRCConnectionManager();
		}
		return instance;
	}
	
}
