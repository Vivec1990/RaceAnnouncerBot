package de.sebmey.jimbot.announcer;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import de.sebmey.jimbot.srl.api.Entrant;

public class RaceSplit {

	private String splitName;
	private List<SplitTime> splitTimes = new ArrayList<SplitTime>();
	
	public RaceSplit(String splitName) {
		this.splitName = splitName;
	}
	
	public void addTime(Entrant runner, String time) {
		for(SplitTime sTime: splitTimes) {
			if(runner.getDisplayName().equalsIgnoreCase(sTime.getEntrantName())) {
				sTime.updateTime(time);
				System.out.println("Runner " + sTime.getEntrantName() + " already had a time recorded, updated the existing time.");
				return;
			}
		}
		splitTimes.add(new SplitTime(runner, time));
		Collections.sort(splitTimes, new SplitTimeComparator());
		System.out.println("Time recorded for split [" + this.splitName + "] for runner [" + runner.getDisplayName() + "]. The time was " + time);
	}
	
	public String getSplitName() {
		return splitName;
	}

	public List<SplitTime> getSplitTimes() {
		return splitTimes;
	}
	
	public SplitTime findTimeForRunner(Entrant e) {
		for(SplitTime st : this.getSplitTimes()) {
			if(st.runner.getUserName().equals(e.getUserName())) {
				return st;
			}
		}
		return null;
	}
	
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
			if (other.splitName != null)
				return false;
		} else if (!splitName.equals(other.splitName))
			return false;
		return true;
	}

	public class SplitTime {
		private Entrant runner;
		private long time;
			
		public SplitTime(Entrant runner, String time) {
			this.runner = runner;
			this.time = parseTime(time);
		}
		
		public void updateTime(String time) {
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
		
		public String getEntrantName() {
			return runner.getDisplayName();
		}
		
		public String getDisplayTime() {
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

		public long getTime() { return this.time; }
		
		@Override
		public String toString() {
			return this.getEntrantName() + " : " + this.getDisplayTime();
		}
		
	}

	public class SplitTimeComparator implements Comparator<SplitTime> {
		@Override
		public int compare(SplitTime t1, SplitTime t2) {
			return (int) Math.signum(t1.getTime() - t2.getTime());
		}
	}
}
