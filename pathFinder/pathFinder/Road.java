package pathFinder;

import java.util.Comparator;

public class Road {
	public String roadName = "", sourceNodeName = "", destNodeName = "";
	public int weight = 0;
	public Node srcNode;
	public Node dstNode;

	public Road(String roadName, String sourceNodeName, String destNodeName, int weight) {
		super();
		this.roadName = roadName;
		this.sourceNodeName = sourceNodeName;
		this.destNodeName = destNodeName;
		this.weight = weight;
	}

	class SortbyWeight implements Comparator<Road> {
		// Used for sorting in ascending order of weight
		public int compare(Road i, Road j) {
			if (i.weight > j.weight) {
				return 1;
			} else if (i.weight < j.weight) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
