package com.vivec.jimbot.irc.srl;

import com.vivec.jimbot.announcer.RaceAnnouncer;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.lang3.StringUtils;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RaceSplitTimeListener {

	private static final String TIME_REAL_TIME = "!time RealTime ";
	private static final String DONE_REAL_TIME = "!done RealTime ";
	private static final List<String> acceptedPrefixes = Arrays.asList(TIME_REAL_TIME, DONE_REAL_TIME);
	private static final String FINAL_TIME_SPLIT_NAME = "Done";

	@Handler
	public void messageInLiveSplitChannelReceived(ChannelMessageEvent event) {
		User author = event.getActor();
		String message = event.getMessage();
		if(acceptedPrefixes.stream().noneMatch(message::startsWith)) return;
		boolean isFinalSplit = message.startsWith(DONE_REAL_TIME);

		//TODO: move this to a method that can be reused, maybe make it even more abstract
		Channel channel = event.getChannel();
		String raceId = StringUtils.substringBetween(channel.getName(), "-", "-");

		String data = message.substring("!time RealTime ".length()).trim();
		String datadone = message.substring("!done RealTime ".length()).trim();
		String splitName = isFinalSplit ? FINAL_TIME_SPLIT_NAME : StringUtils.substringBetween(message, "\"", "\"").trim();
		String splitTime = isFinalSplit ? datadone.substring("\"\" ".length() + splitName.length()).trim() : data.substring("\"\" ".length() + splitName.length()).trim();

		Optional.ofNullable(RaceAnnouncer.getInstance())
				.map(ra -> ra.getRaceByID(raceId))
				.ifPresent(wr -> wr.recordSplitTime(splitName, author.getNick(), splitTime));
	}
	
}
