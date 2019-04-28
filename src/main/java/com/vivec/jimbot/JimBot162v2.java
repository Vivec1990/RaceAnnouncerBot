package com.vivec.jimbot;

import com.vivec.jimbot.irc.CredentialManager;
import com.vivec.jimbot.irc.twitch.TwitchIRCConnectionManager;
import com.vivec.jimbot.irc.twitch.TwitchJim;

public class JimBot162v2 {

	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("usage: jimbot162 BotUsername TwitchOAuth SRLPass");
			return;
		}

		CredentialManager man = CredentialManager.getInstance();
		man.setBotUsername(args[0]);
		man.setBotTwitchOAuth(args[1]);
		man.setBotSRLPass(args[2]);
		TwitchJim jim = TwitchIRCConnectionManager.getInstance().getIRCClient();
		jim.start();
	}
}
