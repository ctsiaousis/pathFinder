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
//		System.out.println(inputData.actualTraffic.get(0));
//		System.out.println(inputData.actualTraffic.get(1));
//		System.out.println(inputData.actualTraffic.get(2));
//		System.out.println(inputData.actualTraffic);

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
				alg.runAStar(inputData.getPredictionDay(i));
				alg.printResults(inputData.getPredictionDay(i));
				alg.runAStar(inputData.getActualTrafficDay(i));
				alg.printResults(inputData.getActualTrafficDay(i));

//				alg.runBFS();
//				alg.printResults(inputData.getPredictionDay(i),inputData.getActualTrafficDay(i));
			}
			
		} else {
			System.err.println("Parse check failed");
			System.exit(0);
		}
	}

}