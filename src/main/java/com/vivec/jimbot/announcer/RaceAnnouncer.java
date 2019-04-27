package com.vivec.jimbot.announcer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.vivec.jimbot.srl.SpeedrunsliveAPI;
import com.vivec.jimbot.srl.api.Entrant;
import com.vivec.jimbot.srl.api.Race;
import com.vivec.jimbot.srl.api.RaceState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RaceAnnouncer {

	private static final Logger LOG = LogManager.getLogger(RaceAnnouncer.class);
	private static RaceAnnouncer instance;

	private List<WatchedRace> watchedRaces;

	public static synchronized RaceAnnouncer getInstance() {
		if(instance == null) {
			instance = new RaceAnnouncer();
		}
		return instance;
	}
	
	private RaceAnnouncer() {
		this.watchedRaces = new ArrayList<>();
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
		exec.scheduleAtFixedRate(new RaceWatcherCleanup(), 5, 5, TimeUnit.MINUTES);
	}
	
	public WatchedRace getRaceByID(String raceId) {
		for(WatchedRace r : this.watchedRaces) {
			if(r.getRaceId().equals(raceId)) {
				return r;
			}
		}
		return null;
	}
	
	public WatchedRace getRaceByParticipant(String username) {
		for(WatchedRace r : this.watchedRaces) {
			for(Entrant e : r.getRunnersConnectedThroughLiveSplit()) {
				if(username != null && username.equalsIgnoreCase(e.getTwitch())) {
					return r;
				}
			}
		}
		return null;
	}
	
	public void addRace(WatchedRace wr)  {
		this.watchedRaces.add(wr);
	}
	
	private class RaceWatcherCleanup implements Runnable {
		
		@Override
		public void run() {
			LOG.info("Cleaning up finished races...");
			SpeedrunsliveAPI api = new SpeedrunsliveAPI();
			for(WatchedRace wr : watchedRaces) {
				Race jRace = api.getSingleRace(wr.getRaceId());
				if(jRace.getState() == RaceState.COMPLETE || jRace.getState() == RaceState.TERMINATED) {
					wr.finishRace();
					RaceAnnouncer.getInstance().watchedRaces.remove(wr);
				}
			}
		}
	}

	
	
}
