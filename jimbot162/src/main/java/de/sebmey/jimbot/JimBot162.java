package de.sebmey.jimbot;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.feature.twitch.TwitchDelaySender;
import org.kitteh.irc.client.library.feature.twitch.TwitchListener;

public class JimBot162 {

	private static String srlPass;
	private static String botName;
	
	public JimBot162(String ownerChannel, String botName, String oauth, String srlPass) {
//		Client jim = Client.builder().nick("JimBot162").serverHost("irc.speedrunslive.com").buildAndConnect();
		Client twitchJim = Client.builder()
                .serverHost("irc.chat.twitch.tv").serverPort(443)
                .serverPassword(oauth)
                .nick(botName)
                .messageSendingQueueSupplier(TwitchDelaySender.getSupplier(false))
                .build();
		twitchJim.getEventManager().registerEventListener(new TwitchListener(twitchJim));
		twitchJim.getEventManager().registerEventListener(new MessageListener());
		twitchJim.connect();
		twitchJim.addChannel("#"+botName.toLowerCase());
		twitchJim.sendMessage("#"+botName.toLowerCase(), "Up and ready");
	}
		
	public static void main(String[] args) {
		//BotOwner, botName, twitch oauth, srl password
		new JimBot162(args[0], args[1], args[2], args[3]);
		JimBot162.srlPass = args[3];
		JimBot162.botName = args[1];
	}
	
	public static String getSrlPass() {
		return JimBot162.srlPass;
	}
	
	public static String getBotName() {
		return JimBot162.botName;
	}
	
	
}
