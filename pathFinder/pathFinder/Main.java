package pathFinder;

import utils.FileParser;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileParser mParser = new FileParser();
		Setup inputData = new Setup();
		mParser.parse("sampleGraph2.txt", inputData);
		System.out.println(inputData.roads.size());
		System.out.println(inputData.predictions.size());
//		System.out.println(inputData.actualTraffic.get(0))

		System.out.println(inputData.actualTraffic.size());
//		System.out.println(inputData.findRoadByName("ErgotelisAve").destNodeName);
		if(inputData.validateData()) {
			System.out.println("Valid Parse, continue");
			System.out.println(inputData.network.size());
//			System.out.println(inputData.findConnectorRoad("10Node1e","10Node2e").roadName);
			Algorithms alg = new Algorithms(inputData);
//			System.out.println(inputData.getActualTrafficDay(20).traffic);
			for(int i=0 ; i<inputData.actualTraffic.size(); i++) {
				System.out.println("---------------Day " +i+ "-----------------");
				alg.runIDAStar(inputData.getPredictionDay(i));
				alg.printResults(inputData.getPredictionDay(i));
				alg.runIDAStar(inputData.getActualTrafficDay(i));
				alg.printResults(inputData.getActualTrafficDay(i));
//////				
//				alg.runDijkstra(inputData.getPredictionDay(i));
//				alg.printResults(inputData.getPredictionDay(i));
//				alg.runDijkstra(inputData.getActualTrafficDay(i));
//				alg.printResults(inputData.getActualTrafficDay(i));
//				alg.runUCS(inputData.getPredictionDay(i));
//				alg.printResults(inputData.getPredictionDay(i));
//				alg.runBFS();
//				alg.printResults(inputData.getPredictionDay(i),inputData.getActualTrafficDay(i));
			}
			double cost=alg.printMeanCost()/80;
			System.out.println("The total mean cost is: "+cost);
			
		} else {
			System.err.println("Parse check failed");
			System.exit(0);
		}
	}

}