package pathFinder;

import java.util.*;
//import pathFinder.Road;
//import pathFinder.Day;

public class Setup {
	public String source;
	public String destination;
	public List<Road> roads;
	public List<Day> predictions;
	public List<Day> actualTraffic;
	public List<Node> network;

	public void addPredictionDay(Day d) {
		predictions.add(d);
	}

	public void addActualTrafficDay(Day d) {
		actualTraffic.add(d);
	}

	public void addRoad(Road r) {
		roads.add(r);
	}

	public Day getPredictionDay(int i) {
		if (i <= predictions.size()-1) {
			return predictions.get(i);
		}
		return null;
	}

	public Day getActualTrafficDay(int i) {
		if (i <= actualTraffic.size()-1) {
			return actualTraffic.get(i);
		}
		return null;
	}

	public Road findRoadByName(String name) {
		Road found;
		for (int i = 0; i < roads.size(); i++) {
			found = roads.get(i);
			if (name.equals(found.roadName)) {
				return found;
			}
		}
		return null;
	}

	public Node findNodeByName(String name) {
		Node found;
		for (int i = 0; i < network.size(); i++) {
			found = network.get(i);
			if (name.equals(found.name)) {
				return found;
			}
		}
		return null;
	}

	public Road getRoad(int i) {
		if (i < roads.size()) {
			return null;
		}
		return roads.get(i);
	}
	public Road findConnectorRoad(String n1, String n2 ) {
		for (Road r : this.roads){
//			System.out.println("findConnectorRoad::"+r.roadName+", "+r.srcNode.name+", "+r.dstNode.name);
			if(r.sourceNodeName.equals(n1) && r.destNodeName.equals(n2)) {
				return r;
			}else if(r.sourceNodeName.equals(n2) && r.destNodeName.equals(n1)) {
				return r;
			}
		}
		return null;
	}
	public void setRoadNodes() {
		Road tempRoad;
		for(int i = 0; i < roads.size(); i++) {
			tempRoad = roads.get(i);
			tempRoad.srcNode = this.findNodeByName(tempRoad.sourceNodeName);
			tempRoad.dstNode = this.findNodeByName(tempRoad.destNodeName);
			if(tempRoad.srcNode.findRoadByName(tempRoad.roadName) == null) {
				tempRoad.srcNode.roads.add(tempRoad);
			}
			if(tempRoad.dstNode.findRoadByName(tempRoad.roadName) == null) {
				tempRoad.dstNode.roads.add(tempRoad);
			}
		}
	}
	public void setNetworkConnections() {
		Road tempRoad;
		Node tempNode;
		for (int i = 0; i < network.size(); i++) {
			tempNode = network.get(i);
			for (int j = 0; j < roads.size(); j++) {
				tempRoad = roads.get(j);				
				if(tempNode.name.equals(tempRoad.destNodeName)) {
					if(tempNode.findNodeByName(tempRoad.sourceNodeName) == null) {
						tempNode.connections.add(this.findNodeByName(tempRoad.sourceNodeName));
					}
				}
				if(tempNode.name.equals(tempRoad.sourceNodeName)) {
					if(tempNode.findNodeByName(tempRoad.destNodeName) == null) {
						tempNode.connections.add(this.findNodeByName(tempRoad.destNodeName));
					}
				}
			}
		}
	}

	public void createNetwork() {
		Road tmpRoad;
		for (int i = 0; i < roads.size(); i++) {
			tmpRoad = roads.get(i);
			if (this.findNodeByName(tmpRoad.sourceNodeName) == null) {
				this.network.add(new Node(tmpRoad.sourceNodeName));
			}
			if (this.findNodeByName(tmpRoad.destNodeName) == null) {
				this.network.add(new Node(tmpRoad.destNodeName));
			}
		}
		setNetworkConnections();
		setRoadNodes();
	}

	public boolean validateData() {
		int roads = this.roads.size();
		if (predictions.size() != actualTraffic.size())
			return false;
		for (int i = 0; i < predictions.size(); i++) {
			Day tmp = predictions.get(i);
			if (tmp.roadNames.size() != roads)
				return false;

			tmp = actualTraffic.get(i);
			if (tmp.roadNames.size() != roads)
				return false;
		}
		createNetwork();
		return true;
	}

	public Setup() {
		super();
		// TODO Auto-generated constructor stub
		roads = new ArrayList<Road>();
		predictions = new ArrayList<Day>();
		actualTraffic = new ArrayList<Day>();
		network = new ArrayList<Node>();
	}

}
