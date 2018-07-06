package de.sebmey.jimbot;

import de.sebmey.jimbot.irc.CredentialManager;
import de.sebmey.jimbot.irc.twitch.TwitchIRCConnectionManager;
import de.sebmey.jimbot.irc.twitch.TwitchJim;

public class JimBot162v2 {

	public static void main(String[] args) {
		CredentialManager man = CredentialManager.getInstance();
		man.setBotUsername(args[0]);
		man.setBotTwitchOAuth(args[1]);
		man.setBotSRLPass(args[2]);
		TwitchJim jim = TwitchIRCConnectionManager.getInstance().getIRCClient();
		jim.start();
	}
}
