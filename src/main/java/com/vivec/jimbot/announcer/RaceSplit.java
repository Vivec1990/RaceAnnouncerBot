package com.vivec.jimbot.announcer;

import com.vivec.jimbot.srl.api.Entrant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RaceSplit {

	private static final Logger LOG = LogManager.getLogger(RaceSplit.class);
	private static final String UNDO_SPLIT_MARKER = "-";

	private String splitName;
	private final List<SplitTime> splitTimes = new ArrayList<>();
	private Boolean announced;
	private Integer orderNr;
	
	RaceSplit(String splitName, Integer OrderNr) {
		this.splitName = splitName;
		this.orderNr = orderNr;
		this.clearAnnounced();
	}
	
	void addTime(Entrant runner, String time) {
		boolean undoSplit = time.equalsIgnoreCase(UNDO_SPLIT_MARKER);
		for(SplitTime sTime: splitTimes) {
			if(runner.getDisplayName().equalsIgnoreCase(sTime.getEntrantName())) {
				if (undoSplit) {
					splitTimes.remove(sTime);
					LOG.info("Runner {} undid split {}.", sTime.getEntrantName(), getSplitName());
				} else {
					sTime.updateTime(time);
					LOG.info("Runner {} already had a time recorded, updated the existing time.", sTime.getEntrantName() );
				}
				return;
			}
		}

		if (undoSplit) {
			return;
		}

		splitTimes.add(new SplitTime(runner, time, this.orderNr, this.getSplitName()));
		splitTimes.sort(new SplitTimeComparator());
		LOG.info("Time recorded for split [{}] for runner [{}]. The time was {}", getSplitName(), runner.getDisplayName(), time);
	}
	
	String getSplitName() {
		return splitName;
	}

	List<SplitTime> getSplitTimes() {
		return splitTimes;
	}
	
	SplitTime findTimeForRunner(Entrant e) {
		for(SplitTime st : this.getSplitTimes()) {
			if(st.runner.getUserName().equals(e.getUserName())) {
				return st;
			}
		}
		return null;
	}

	public void setAnnounced() { announced = true; }
	public void clearAnnounced() { announced = false; }
	public Boolean hasBeenAnnounced() { return announced; }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((splitName == null) ? 0 : splitName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RaceSplit other = (RaceSplit) obj;
		if (splitName == null) {
			return other.splitName == null;
		} else return splitName.equals(other.splitName);
	}

	public class SplitTime {
		private Entrant runner;
		private long time;
		private Integer orderNr;
		private String splitName;
			
		SplitTime(Entrant runner, String time, Integer orderNr, String splitName) {
			this.runner = runner;
			this.time = parseTime(time);
			this.orderNr = orderNr;
			this.splitName = splitName;
		}
		
		void updateTime(String time) {
			this.time = parseTime(time);
		}
		
		private long parseTime(String time) {
			String[] timeParts = time.split(":");
			int hourIndex = timeParts.length == 3 ? 0 : -1;
			int minuteIndex = timeParts.length == 3 ? 1 : 0;
			int secondIndex = timeParts.length == 3 ? 2 : 1;
			
			long timeInMillis = 0;
			if(hourIndex != -1) {
				timeInMillis += Double.parseDouble(timeParts[hourIndex]) * 60 * 60 * 1000;
			}
			timeInMillis += Double.parseDouble(timeParts[minuteIndex]) * 60 * 1000;
			timeInMillis += Double.parseDouble(timeParts[secondIndex]) * 1000;
			return timeInMillis;
		}
		
		String getEntrantName() {
			return runner.getDisplayName();
		}
		
		String getDisplayTime() {
			long second = (this.time / 1000) % 60;
			long minute = (this.time / (1000 * 60)) % 60;
			long hour = (this.time / (1000 * 60 * 60));
			long millis = this.time % 1000;
			if(hour > 0) {
				return String.format("%d:%02d:%02d.%d", hour, minute, second, millis);
			} else {
				return String.format("%02d:%02d.%d", minute, second, millis);
			}
		}

		long getTime() { return this.time; }

		Integer getOrderNr() { return this.orderNr; }

		String getSplitName() { return this.splitName; }
		
		@Override
		public String toString() {
			return this.getEntrantName() + " : " + this.getDisplayTime();
		}
		
	}

	public static class SplitTimeComparator implements Comparator<SplitTime> {
		@Override
		public int compare(SplitTime t1, SplitTime t2) {
			if(t1.getOrderNr() == t2.getOrderNr()) {
				return (int) Math.signum(t1.getTime() - t2.getTime()); // sort in increasing order
			}
			return (int) Math.signum(t2.getOrderNr() - t1.getOrderNr()); // sort in decreasing order
		}
	}
}
