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
		return r.weight*daysTraffic;
	}

	public void appendTouple(String name, String tr) {
		roadNames.add(name);
		if(tr.contains("low")) {
			traffic.add(normalTrafficVal * 0.9);
		}else if(tr.contains("heavy")) {
			traffic.add(normalTrafficVal * 1.25);
		}else {
			traffic.add(normalTrafficVal);
		}
		
	}
	public String getName(int i) {
		if(i < roadNames.size()) {
			return "";
		}
		return roadNames.get(i);
	}
	public double getTraffic(int i) {
		if(i < traffic.size()) {
			return traffic.get(i);
		}
		return -1;
	}
}
