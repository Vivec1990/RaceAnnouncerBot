package de.sebmey.jimbot.announcer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.sebmey.jimbot.srl.SpeedrunsliveAPI;
import de.sebmey.jimbot.srl.api.Entrant;
import de.sebmey.jimbot.srl.api.Race;
import de.sebmey.jimbot.srl.api.RaceState;

public class RaceAnnouncer {

	private List<WatchedRace> watchedRaces;
	
	private static RaceAnnouncer INSTANCE;
	
	public static synchronized RaceAnnouncer getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new RaceAnnouncer();
		}
		return INSTANCE;
	}
	
	private RaceAnnouncer() {
		this.watchedRaces = new ArrayList<WatchedRace>();
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
			System.out.println("Cleaning up finished races...");
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