package pathFinder;

import java.util.Comparator;
import java.util.LinkedList;

public class Node {
	public String name;
	public LinkedList<Node> connections;
	public LinkedList<Road> roads;
	public double currentCost;
	public double fringeCost;

	public Node() {
		connections = new LinkedList<Node>();
		roads = new LinkedList<Road>();
		this.currentCost = Double.POSITIVE_INFINITY;
		this.fringeCost = Double.POSITIVE_INFINITY;
	}

	public Node(String nam) {
		name = nam;
		connections = new LinkedList<Node>();
		roads = new LinkedList<Road>();
		this.currentCost = Double.POSITIVE_INFINITY;
		this.fringeCost = Double.POSITIVE_INFINITY;
	}

	public void resetCurrentCost() {
		this.currentCost = Double.POSITIVE_INFINITY;
		this.fringeCost = Double.POSITIVE_INFINITY;
	}

	public Node findNodeByName(String name) {
		Node found;
		for (int i = 0; i < connections.size(); i++) {
			found = connections.get(i);
			if (name.equals(found.name)) {
				return found;
			}
		}
		return null;
	}

	public double calculateCost(Road r) {
		this.currentCost = r.weight;
		return this.currentCost;
	}

	public double getCurrentCost() {
		return currentCost;
	}

	public double calculateCost(Day d, Road r) {
		this.currentCost = d.calculateWeight(r);
		return this.currentCost;
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

	class SortbyWeight implements Comparator<Node> {
		// Used for sorting in ascending order of curCost
		public int compare(Node i, Node j) {
			if (i.currentCost > j.currentCost) {
				return 1;
			} else if (i.currentCost < j.currentCost) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	class SortbyFringe implements Comparator<Node> {
		// Used for sorting in ascending order of fringe
		public int compare(Node i, Node j) {
			if (i.fringeCost > j.fringeCost) {
				return 1;
			} else if (i.fringeCost < j.fringeCost) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
