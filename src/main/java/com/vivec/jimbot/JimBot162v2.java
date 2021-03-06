package com.vivec.jimbot;

import com.vivec.jimbot.irc.CredentialManager;
import com.vivec.jimbot.irc.twitch.TwitchIRCConnectionManager;
import com.vivec.jimbot.irc.twitch.TwitchJim;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JimBot162v2 {

	private static final Logger LOG = LogManager.getLogger(JimBot162v2.class);
	public static boolean DEBUG = false;

	public static void main(String[] args) {
		if((args.length < 3) || (args.length > 4)) {
			LOG.info("usage: jimbot162 BotUsername TwitchOAuth SRLPass [-d]");
			return;
		}

		CredentialManager man = CredentialManager.getInstance();
		man.setBotUsername(args[0]);
		man.setBotTwitchOAuth(args[1]);
		man.setBotSRLPass(args[2]);
		if(args.length > 3) {
			if (args[3].equalsIgnoreCase("-d")) {
				DEBUG = true;
				LOG.info("Started in Debug mode");
			}
		}
		TwitchJim jim = TwitchIRCConnectionManager.getInstance().getIRCClient();
		jim.start();
	}
}
