package pathFinder;

import java.util.ArrayList;
import java.util.List;

public class Day {
	public List<String> roadNames;
	public List<Double> traffic;
	public boolean isPrediction;
	public double normalTrafficVal;
	private double p1=0.4, p2=0.3, p3=0.3;
	//p1 makes heavy -> normal
	//p2 makes normal -> low
	//p3 makes low -> normal

	public Day() {
		super();
		normalTrafficVal = 1;
		roadNames = new ArrayList<String>();
		traffic = new ArrayList<Double>();
		this.isPrediction = false;
	}

	public void setIsPrediction(boolean b) {
		this.isPrediction = b;
		if(b) {
			recalcTraffic();
		}
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
	
	private double weightWithOdds(double tr) {
		if (this.isPrediction) {
			double posib = Math.random();
			if (tr == normalTrafficVal * 0.9) {
				if (posib <= this.p3)
					return normalTrafficVal;
			}else if(tr == normalTrafficVal) {
				if (posib > 1-this.p2)
					return normalTrafficVal * 0.9;
			}else {
				if (posib <= 1-this.p2 && posib > this.p3)
					return normalTrafficVal;
			}
		}
		return tr;
	}
	
	private void recalcTraffic() {
		for(int i = 0; i < traffic.size(); i++) {
			double newTraf = weightWithOdds(traffic.get(i));
			traffic.set(i, newTraf);
		}
	}

	public void appendTouple(String name, String tr) {
		roadNames.add(name);
		if (tr.contains("low")) {
			traffic.add(normalTrafficVal * 0.9);
		} else if (tr.contains("heavy")) {
			traffic.add(normalTrafficVal * 1.25);
		} else {
			traffic.add(normalTrafficVal);
		}
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
