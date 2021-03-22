package pathFinder;

import java.util.*;
import java.lang.Math;


public class Algorithms {
	private static final int FOUND = -3;
	private Setup setup;
	private int nodesVisited;
	private double predictedCost, realCost, roadCost, totalCost;
	private long executionTime;
	private ArrayList<String> path;
	private String currentAlgo;
	
	
	
	private void reset() {
		this.currentAlgo = "";
		this.executionTime = 0;
		this.nodesVisited = 0;
		this.path = new ArrayList<String>();
		this.realCost = 0;
		this.predictedCost = 0;
		this.roadCost = 0;
		for (Node n : this.setup.network)
			n.resetCurrentCost();
	}

	private void calcRealCostFromPath(Day d) {
		this.realCost = 0;
		for (int i = 1; i < this.path.size(); i++) {
			Road tmpRoad = this.setup.findConnectorRoad(this.path.get(i - 1), this.path.get(i));
			this.realCost += (tmpRoad.weight) * d.getTraffic(d.findRoadIndex(tmpRoad));
		}
	}

	private void calcPredictedCostFromPath(Day d) {
		this.predictedCost = 0;
		for (int i = 1; i < this.path.size(); i++) {
			Road tmpRoad = this.setup.findConnectorRoad(this.path.get(i - 1), this.path.get(i));
			this.predictedCost += (tmpRoad.weight) * d.getTraffic(d.findRoadIndex(tmpRoad));
		}
	}
	
	private double printWeightForEachRoad(Day d, int i) {
		this.roadCost = 0;
		Road tmpRoad = this.setup.findConnectorRoad(this.path.get(i - 1), this.path.get(i));
		this.roadCost = (tmpRoad.weight) * d.getTraffic(d.findRoadIndex(tmpRoad));

		return this.roadCost;
	}

	private void calculatePath(int[] parentNodes) {
		int src = this.setup.network.indexOf(this.setup.findNodeByName(this.setup.source));
		int dst = this.setup.network.indexOf(this.setup.findNodeByName(this.setup.destination));
		this.path.add(this.setup.network.get(dst).name);
		while (true) {
			Node tmp = this.setup.network.get(parentNodes[dst]);
			this.path.add(tmp.name);
			dst = parentNodes[dst];
			if (dst == src)
				break;
		}

	}
	public void runBFS() {
		reset();
		this.currentAlgo = "BFS";
		long tic = System.nanoTime();
		int networkSize = this.setup.network.size();
		boolean visited[] = new boolean[networkSize];
		int parentNode[] = new int[networkSize];
		for (int i = 0; i < networkSize; i++) {
			visited[i] = false;
			parentNode[i] = -1;
		}
		LinkedList<Node> queue = new LinkedList<Node>();

		Node sourceNode = this.setup.findNodeByName(this.setup.source);
		visited[this.setup.network.indexOf(sourceNode)] = true;
		parentNode[this.setup.network.indexOf(sourceNode)] = 0;
		queue.add(sourceNode);

		while (queue.size() != 0) {
			sourceNode = queue.poll();
			this.nodesVisited++;

//			System.out.println("Just arrived at " + sourceNode.name);
//			System.out.println(sourceNode.connections.size() + " connections are available");

			Iterator<Node> i = sourceNode.connections.listIterator();

			while (i.hasNext()) {
				Node n = i.next();
//				System.out.println("Sundesi: " + n.name);
				int index = this.setup.network.indexOf(this.setup.findNodeByName(n.name));
				if (n.name.equals(this.setup.destination)) {
					parentNode[index] = this.setup.network.indexOf(sourceNode);
					this.calculatePath(parentNode);
					long toc = System.nanoTime();
					this.executionTime = toc - tic;
					return;
				}
				if (!visited[index]) {
					visited[index] = true;
					parentNode[index] = this.setup.network.indexOf(sourceNode);
					queue.add(this.setup.findNodeByName(n.name));
				}
			}
		}

		long toc = System.currentTimeMillis();
		this.executionTime = toc - tic;
	}

	

	public void runUCS(Day dd) {
		reset();
//		System.out.println(setup.actualTraffic);
		this.currentAlgo = "UCS";
		long tic = System.nanoTime();
		Node srcNode = this.setup.findNodeByName(this.setup.source);
		Node dstNode = this.setup.findNodeByName(this.setup.destination);
		int networkSize = this.setup.network.size();
		int parentNode[] = new int[networkSize];

		PriorityQueue<Node> queue = new PriorityQueue<Node>(networkSize, srcNode.new SortbyWeight());

		srcNode.currentCost = 0;
		queue.add(srcNode);
		boolean visited[] = new boolean[networkSize];

		for (int i = 0; i < networkSize; i++) {
			visited[i] = false;
			parentNode[i] = -1;
		}

		while (!queue.isEmpty()) {
			Node current = queue.poll();
			int index = this.setup.network.indexOf(current);
			visited[index] = true;
			this.nodesVisited++;

			if (current.name.equals(dstNode.name)) {
				System.out.println("EVRHKA");
				this.calculatePath(parentNode);
//				this.realCost = current.currentCost;
				long toc = System.nanoTime();
				this.executionTime = toc - tic;
				return;
			}
			testLabel: for (Road r : current.roads) {
				Node child;
				if (r.dstNode == current) {
					child = r.srcNode;
				} else {
					child = r.dstNode;
				}
				index = this.setup.network.indexOf(child);

				if (!visited[index] && !queue.contains(child)) {
					double cost = child.calculateCost(dd, r);
					child.currentCost = current.currentCost + cost;
					parentNode[setup.network.indexOf(child)] = this.setup.network.indexOf(current);
					queue.add(child);
				} else if ((queue.contains(child)) && (child.currentCost < current.currentCost)) {
					parentNode[setup.network.indexOf(child)] = this.setup.network.indexOf(current);
					current = child; // does this changes the for?
					continue testLabel;
				}
			}
		}

	}

	public double searchIDA(List<Node> mPath, Day dayIn, double fringeCost, Heuristic2 h) {
		this.nodesVisited++;
		Node dstNode = this.setup.findNodeByName(this.setup.destination);
		Node currNode = mPath.get(mPath.size() - 1);
		if (currNode.fringeCost > fringeCost)
			return currNode.fringeCost;
		if (currNode.name.equals(dstNode.name))
			return FOUND;
		double minimum = Double.POSITIVE_INFINITY;
		for (Road road : currNode.roads) {
			Node childNode;
			if (road.dstNode == currNode)
				childNode = road.srcNode;
			else
				childNode = road.dstNode;
			
			if (!mPath.contains(childNode)) {
				double cost = currNode.calculateCost(dayIn, road);
				double tentativeCost = currNode.currentCost + cost;
				childNode.currentCost = tentativeCost;
				childNode.fringeCost = tentativeCost + h.giveTheWeight(childNode,dayIn);
				mPath.add(childNode);
				double t = searchIDA(mPath, dayIn, fringeCost, h);
				if (t == FOUND)
					return FOUND;
				if (t < minimum)
					minimum = t;
				mPath.remove(childNode);
			}
		}
		return minimum;
	}

	public void runIDAStar(Day dayIn) {
		reset();
		this.currentAlgo = "IDA*";
		long tic = System.nanoTime();
		Node srcNode = this.setup.findNodeByName(this.setup.source);

////		h2.calcStep2(srcNode,dayIn);
		int networkSize = this.setup.network.size();

		List<Node> queue = new ArrayList<Node>(networkSize);
		queue.add(srcNode);

		Heuristic2 h = new Heuristic2(this.setup, dayIn);
		srcNode.currentCost = 0;
		srcNode.fringeCost = h.giveTheWeight(srcNode,dayIn);
//		System.out.println("---------------------------------------------"+srcNode.fringeCost);
		double fringe = srcNode.fringeCost;
		System.out.println(fringe);
////		double fringe1 = 2000;
		
		boolean found = false;
		while (true) {
//			reset();
//			srcNode.currentCost = 0;
//			srcNode.fringeCost = h.calcStep2(srcNode,dayIn);
			double cost = this.searchIDA(queue, dayIn, fringe, h);
//			reset();
			if (cost == FOUND) {
				System.out.println('1');
				found = true;
				break;
			}
			if (cost == Double.POSITIVE_INFINITY) {
				System.out.println('2');
				break;
			}
			fringe = cost;
		}
		if (found) {
			System.out.println("EVRHKA");
			for(Node n: queue) {
				this.path.add(n.name);
			}
////			this.calculatePath(parentNode);
////			this.realCost = current.currentCost;
			long toc = System.nanoTime();
			this.executionTime = toc - tic;
			return;
		}
	}
	

	public double printMeanCost() {
		for (int i = 0; i < this.setup.actualTraffic.size(); i++) {
			calcRealCostFromPath(this.setup.getActualTrafficDay(i));
			totalCost += this.realCost;
		}
		return totalCost;
	}

	public void printResults(Day d1) {
		if (d1.isPrediction) {
			calcPredictedCostFromPath(d1);
		} else {
			calcRealCostFromPath(d1);
		}
		System.out.println("------------" + currentAlgo + " Results: -------------");
		System.out.println("Execution Time: " + this.executionTime + " ns");
		System.out.println("Visited Nodes number: " + this.nodesVisited);
		System.out.println("Path: ");

		for (int i = path.size() - 1; i >= 0; i--) {
			System.out.printf(this.path.get(i) + (i == 0 ? "\n" : " -> "));
		}
		if (d1.getIsPrediction()) {
			System.out.println("Prediction Cost for each road: ");
			for (int i = path.size() - 1; i > 0; i--) {
				String name = this.setup.findConnectorRoad(this.path.get(i - 1), this.path.get(i)).roadName;
				double roadCost = this.printWeightForEachRoad(d1, i);
				System.out.printf(name + " cost: " + roadCost + "\t");
			}
		} else {
			System.out.println("Actual Cost for each road: ");
			for (int i = path.size() - 1; i > 0; i--) {
				String name = this.setup.findConnectorRoad(this.path.get(i - 1), this.path.get(i)).roadName;
				double roadCost = this.printWeightForEachRoad(d1, i);
				System.out.printf(name + " cost: " + roadCost + "\t");
			}
		}

		System.out.println("\nTotal Predicted Cost: " + this.predictedCost);
		System.out.println("Total Real Cost: " + this.realCost);
		System.out.println("--------------------------------------");

	}

	public Algorithms(Setup setup) {
		this.setup = setup;
		predictedCost = 0;
		realCost = 0;
		totalCost = 0;
		executionTime = 0;
		nodesVisited = 0;
		this.path = new ArrayList<String>();
	}
}
