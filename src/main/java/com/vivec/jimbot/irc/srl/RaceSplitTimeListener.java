package com.vivec.jimbot.irc.srl;

import com.vivec.jimbot.announcer.RaceAnnouncer;
import net.engio.mbassy.listener.Handler;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.substringBetween;

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

		String raceId = substringBetween(event.getChannel().getName(), "-", "-");
		
		String arguments = message.substring("!time RealTime ".length()).trim();
		SplitData splitData = message.startsWith(DONE_REAL_TIME) ? createFinalSplitData(arguments) : getStandardSplitData(arguments);

		Optional.ofNullable(RaceAnnouncer.getInstance())
				.map(ra -> ra.getRaceByID(raceId))
				.ifPresent(wr -> wr.recordSplitTime(splitData.getName(), author.getNick(), splitData.getTime()));
	}

	private SplitData getStandardSplitData(String arguments) {
		String splitName = substringBetween(arguments, "\"", "\"").trim();
		String splitTime = arguments.substring("\"\" ".length() + splitName.length()).trim();
		return new SplitData(splitName, splitTime);
	}

	private SplitData createFinalSplitData(String arguments) {
		return new SplitData(FINAL_TIME_SPLIT_NAME, arguments.trim());
	}

	private class SplitData {

		String name;
		String time;

		SplitData(String name, String time) {
			this.name = name;
			this.time = time;
		}

		String getName() {
			return name;
		}

		String getTime() {
			return time;
		}
	}
	
}
