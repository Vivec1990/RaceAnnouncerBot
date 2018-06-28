package de.sebmey.jimbot.irc.srl;

import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import de.sebmey.jimbot.announcer.RaceAnnouncer;
import de.sebmey.jimbot.announcer.WatchedRace;
import net.engio.mbassy.listener.Handler;

public class RaceSplitTimeListener {

	@Handler
	public void messageInLiveSplitChannelReceived(ChannelMessageEvent event) {
		User author = event.getActor();
		String message = event.getMessage();
		if(!message.startsWith("!time RealTime ")) return;
		
		Channel channel = event.getChannel();
		String channelName = channel.getName();
		String raceId = channelName.substring("#srl-".length());
		raceId = raceId.substring(0, 5);
		
		String data = message.substring("!time RealTime ".length()).trim();
		String[] parts = data.split("\"");
		if(parts.length < 2) return;
		String splitName = parts[1].trim();
		String splitTime = parts[2].trim();
		
		WatchedRace wr = RaceAnnouncer.getInstance().getRaceByID(raceId);
		if(wr != null) {
			wr.recordSplitTime(splitName, author.getNick(), splitTime);
			System.out.println("Time recorded for split " + splitName + " and user " + author.getNick() + ". The time was " + splitTime);
		}
	}
	
}
