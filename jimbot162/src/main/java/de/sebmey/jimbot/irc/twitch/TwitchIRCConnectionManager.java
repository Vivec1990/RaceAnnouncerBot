package de.sebmey.jimbot.irc.twitch;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.feature.twitch.TwitchListener;

import de.sebmey.jimbot.irc.CredentialManager;

public class TwitchIRCConnectionManager {

	private static TwitchIRCConnectionManager INSTANCE;
	private Client ircClient;
	
	public TwitchIRCConnectionManager() {
		ircClient = Client.builder().nick(CredentialManager.getBotUsername()).serverHost("irc.twitch.com").serverPort(6697).serverPassword(CredentialManager.getBotTwitchOAuth()).build();
		ircClient.getEventManager().registerEventListener(new TwitchListener(ircClient));
		ircClient.connect();
	}
	
	public Client getIRCClient() {
		return this.ircClient;
	}
	
	public static TwitchIRCConnectionManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new TwitchIRCConnectionManager();
		}
		return INSTANCE;
	}
	
	
	
}
