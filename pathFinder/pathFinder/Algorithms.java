package pathFinder;

import java.util.*;
import java.lang.Math;

public class Algorithms {
	private Setup setup;
	private int nodesVisited;
	private double predictedCost, realCost, roadCost;
	private long executionTime;
	private ArrayList<String> path;
	private String currentAlgo;

	class Heuristic {
		int arbitrary = 100; // A random initial weight
		private ArrayList<Double> prevWeights;
		private double a = 0.15;

		public Heuristic() {
			this.prevWeights = new ArrayList<Double>();
			this.prevWeights.add((double) this.arbitrary);
		}

		public double calcStep(double curWeight) {
			// varos = proigoumena*(1-a)+a*curWeight
			double var = this.a * curWeight + (1 - this.a) * this.calcWeigthMean();
			this.prevWeights.add(curWeight);// We add real data not estimations
			System.out.println("I GOT " + curWeight + " But I Say: " + var);
			if (var > curWeight)
				var = curWeight;
			return var;
		}

		public double calcWeigthMean() {
			double mean = 0;
			for (double d : this.prevWeights) {
				mean += d;
			}
			mean = mean / this.prevWeights.size();
			return Math.floor(mean);
		}
	}

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

//	public void runUCS() {
//		reset();
//		this.currentAlgo = "UCS";
//		long tic = System.nanoTime();
//		Node srcNode = this.setup.findNodeByName(this.setup.source);
//		Node dstNode = this.setup.findNodeByName(this.setup.destination);
//		int networkSize = this.setup.network.size();
//		int parentNode[] = new int[networkSize];
//		
//		
//		PriorityQueue<Node> queue = new PriorityQueue<Node>(networkSize,srcNode.new SortbyWeight());
//		
//		srcNode.currentCost=0;
//        queue.add(srcNode);
//		boolean visited[] = new boolean[networkSize];
//		
//		for (int i = 0; i < networkSize; i++) {
//			visited[i] = false;
//			parentNode[i] = -1;
//		}
//		
//		while(!queue.isEmpty()) {
//			Node current = queue.poll();
//			int index = this.setup.network.indexOf(current);
//			visited[index]=true;
//			this.nodesVisited++;
//			
//			if(current.name.equals(dstNode.name)) {
//				System.out.println("EVRHKA");
////				parentNode[index] = this.setup.network.indexOf(srcNode);
//				this.calculatePath(parentNode);
////				this.realCost = current.currentCost;
//				long toc = System.nanoTime();
//				this.executionTime = toc - tic;
//				return;
//			}
//			for(Road r : current.roads) {
//				Node child;
//				if(r.dstNode == current) {
//					child = r.srcNode;
//				}else {
//					child = r.dstNode;
//				}
//				index =  this.setup.network.indexOf(child);
//				
//				if(!visited[index] && !queue.contains(child)) {
//					double cost = child.calculateCost(r);
//					child.currentCost = current.currentCost + cost;
//					parentNode[setup.network.indexOf(child)]=this.setup.network.indexOf(current);
//					queue.add(child);
//				}
//				else if((queue.contains(child)) && (child.currentCost<current.currentCost)){
//					parentNode[setup.network.indexOf(child)]=this.setup.network.indexOf(current);
//					current=child;
//				}
//			}
//		}
//        
//	}
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

	public void runAStar(Day dayIn) {
		reset();
		this.currentAlgo = "UCS";
		long tic = System.nanoTime();
		Node srcNode = this.setup.findNodeByName(this.setup.source);
		Node dstNode = this.setup.findNodeByName(this.setup.destination);
		int srcIndex = this.setup.network.indexOf(srcNode);
		int networkSize = this.setup.network.size();
		int parentNode[] = new int[networkSize];
		PriorityQueue<Node> queue = new PriorityQueue<Node>(networkSize, srcNode.new SortbyFringe());
		queue.add(srcNode);
		boolean visited[] = new boolean[networkSize];
		
		for (int i = 0; i < networkSize; i++) {
			visited[i] = false;
			parentNode[i] = -1;
		}
		Heuristic h = new Heuristic();
		srcNode.currentCost = 0;
		srcNode.fringeCost  = h.calcStep(1);

		while (!queue.isEmpty()) {
			Node currNode = queue.poll();
			int index = this.setup.network.indexOf(currNode);
			visited[index] = true;
			this.nodesVisited++;

			if (currNode.name.equals(dstNode.name)) {
				System.out.println("EVRHKA");
				this.calculatePath(parentNode);
//				this.realCost = current.currentCost;
				long toc = System.nanoTime();
				this.executionTime = toc - tic;
				return;
			}

			testLabel:
			for (Road r : currNode.roads) {
				Node childNode;
				if(r.dstNode == currNode)
					childNode = r.srcNode;
				else
					childNode = r.dstNode;
				
				double cost = childNode.calculateCost(dayIn, r);
				double tentativeCost = currNode.currentCost + cost;
				
				if(tentativeCost<currNode.currentCost) {
					parentNode[setup.network.indexOf(childNode)] = setup.network.indexOf(currNode);
					childNode.currentCost = tentativeCost;
					childNode.fringeCost = childNode.currentCost + h.calcStep(childNode.currentCost);
					
					if(!queue.contains(childNode))
						queue.add(childNode);
				}

			}

		}
	}

	public void printResults(Day d1) {
		calcPredictedCostFromPath(d1);
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
		executionTime = 0;
		nodesVisited = 0;
		this.path = new ArrayList<String>();
	}
}
