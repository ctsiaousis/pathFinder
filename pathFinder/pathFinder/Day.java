package pathFinder;

import java.util.ArrayList;
import java.util.List;

public class Day {
	public List<String> roadNames;
	public List<Double> traffic;
	public boolean isPrediction;
	public double normalTrafficVal;

	public Day() {
		super();
		normalTrafficVal = 1;
		roadNames = new ArrayList<String>();
		traffic = new ArrayList<Double>();
		this.isPrediction = false;
	}

	public void setIsPrediction(boolean b) {
		this.isPrediction = b;
	}

	public boolean getIsPrediction() {
		return this.isPrediction;
	}

	public int findRoadIndex(Road r) {
		String name = r.roadName;
		for (int i = 0; i < roadNames.size(); i++) {
			if (name.equals(roadNames.get(i))) {
				return roadNames.indexOf(name);
			}
		}
		return -1;
	}

	public double calculateWeight(Road r) {
		double daysTraffic = traffic.get(findRoadIndex(r));
		return r.weight * daysTraffic;
	}

	private String weightWithOdds(String tr) {
		double posib = Math.random();
		if (this.isPrediction) {
			if (posib <= 0.2) {
				if (tr.contains("low"))
					return "normal";
			} else if (posib > 0.8) {
				if (tr.contains("normal"))
					return "low";
			} else {
				if (tr.contains("heavy"))
					return "normal";
			}
		}
		return tr;
	}

	public void appendTouple(String name, String tr) {
		roadNames.add(name);
		String newTraffic = weightWithOdds(tr);
		if (newTraffic.contains("low")) {
			traffic.add(normalTrafficVal * 0.9);
		} else if (newTraffic.contains("heavy")) {
			traffic.add(normalTrafficVal * 1.25);
		} else {
			traffic.add(normalTrafficVal);
		}
		normalTrafficVal = 1;// reset for next tuple
	}

	public String getName(int i) {
		if (i < roadNames.size()) {
			return "";
		}
		return roadNames.get(i);
	}

	public double getTraffic(int i) {
		if (i < traffic.size()) {
			return traffic.get(i);
		}
		return -1;
	}
}
