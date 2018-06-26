package de.sebmey.jimbot.irc.twitch;

import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import net.engio.mbassy.listener.Handler;

public class RaceInitializationListener {

	@Handler
	public void messageInBotChannelReceived(ChannelMessageEvent event) {
		User author = event.getActor();
		String message = event.getMessage();
		
		if(author.getNick().equals(event.getClient().getNick())) {
			return;
		}
		if(!message.startsWith("!race")) {
			return;
		}
		
		
	}
	
}
