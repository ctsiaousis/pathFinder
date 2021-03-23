package pathFinder;

import java.util.ArrayList;
import java.util.List;

//import pathFinder.Node.SortbyWeight;

class Heuristic2 {
	List<String> path;
	List<Node> pqueue;
	private Setup setup;
	int V;
	int parent[];
	Node startingNode;
	double finalCost;
	
	public Heuristic2(Setup sin, Day din) {
		this.setup = sin;
		this.V = sin.network.size();
		this.path = new ArrayList<String>();
		pqueue = new ArrayList<Node>(V); 
		parent = new int[this.V];
		this.finalCost=0;
		runDijkstra(din);
	}
	
	public double giveTheWeight(Node nodeIn, Day dayIn) {
		double total=0;
		boolean l=false;
		int k=0;
		for (int i = path.size() - 1; i > 0; i--) {
			l=false;
//			System.out.println("monopati "+this.path.get(i));
//			if(i<this.path.size()-1) {
				if(this.path.get(i).equals(nodeIn.name)) {
					l=true;
					 k=i;
					break;
					
					
				}
//			}
		}
		if (l=true) {
		for (int j= k; j>0;j--) {
			Road tmpRoad = this.setup.findConnectorRoad(this.path.get(j), this.path.get(j-1));
			//System.out.println("from "+this.path.get(i) + " to " + this.path.get(i-1) );
			total=total+dayIn.calculateWeight(tmpRoad);
			}
		return total;
		
		}
		
	return Double.POSITIVE_INFINITY;
		
		
	}
	
	public double calcStep2(Node startingNode, Day dayIn) {
//		reset();
		finalCost=0;
		Node dstNode = setup.findNodeByName(setup.destination);
//		System.out.println(dstNode.name+" proorismos");
		for(Node v : setup.network) {
			v.fringeCost=Double.POSITIVE_INFINITY;
			int index = setup.network.indexOf(v);
			this.parent[index]=-1;
			pqueue.add(v);
		}
		
		startingNode.fringeCost = 0;
		
		while(!pqueue.isEmpty()) {
//			for(int i=0;i<pqueue.size();i++) {
				pqueue.sort(pqueue.get(0).new SortbyFringe());
//			}
//				for(int i=0;i<pqueue.size();i++) {
//					System.out.println(pqueue.get(i).fringeCost+"----------");
//				}
//				System.out.println("+++++++++++++++++++");
			Node currentNode=pqueue.get(0);
//			System.out.println("cur index"+setup.network.indexOf(currentNode));
			pqueue.remove(currentNode);
			if (currentNode.name.equals(dstNode.name)) {
//				parent[setup.network.indexOf(currentNode)]=setup.network.indexOf(currentNode);
				calculateDijkstraPath(parent,startingNode);
				System.out.println(this.path+"++++");
				finalCost=calcCostForHeuristic(dayIn);
				System.out.println(finalCost);
				return finalCost;
			}
			
			for(Road r: currentNode.roads) {
				Node childNode;
				if(r.dstNode == currentNode) {
					childNode = r.srcNode;
				}else {
					childNode = r.dstNode;
				}
				if(pqueue.contains(childNode)) {
					double cost = dayIn.calculateWeight(r);
					double alt=currentNode.fringeCost + cost;
					if(alt < childNode.fringeCost) {
						parent[setup.network.indexOf(childNode)]=setup.network.indexOf(currentNode);
						childNode.fringeCost=alt;
						}
					}
				}	
			}
		return finalCost;
	}
	
	private void calculateDijkstraPath(int[] parent,Node startingNode) {
		int src = this.setup.network.indexOf(this.setup.findNodeByName(startingNode.name));
		int dst = this.setup.network.indexOf(this.setup.findNodeByName(this.setup.destination));
//		System.out.println("--------------s: "+src+"----------d: "+dst+"------------si :   "+parent[dst]);
//		for(int i = 0; i < this.V; i++) {
//			System.out.println("index: "+i+" value:"+parent[i]);
//		}
		this.path.add(this.setup.network.get(dst).name);
		while (true) {
			Node tmp = this.setup.network.get(parent[dst]);
			this.path.add(tmp.name);
			dst = parent[dst];
			if (dst == src)
				break;
		}

	}

	private double calcCostForHeuristic(Day d) {
		int predictedCost = 0;
//		System.out.println(path);
		for (int i = 1; i < this.path.size(); i++) {
//			System.out.println("PRWTOPAPAS  -- "+this.path.get(i - 1)+" -- "+ this.path.get(i));
			Road tmpRoad = this.setup.findConnectorRoad(this.path.get(i - 1), this.path.get(i));
//			System.out.println("from "+this.path.get(i - 1)+" to "+ this.path.get(i) + " road : "+tmpRoad);
			predictedCost += (tmpRoad.weight);
		}
		return predictedCost;
	}
	
	public void runDijkstra(Day dayIn) {
		Node srcNode = this.setup.findNodeByName(this.setup.source);
		double t = calcStep2(srcNode, dayIn);
//		System.out.println(t);
	}
}