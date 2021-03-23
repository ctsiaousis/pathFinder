package pathFinder;

import utils.FileParser;
import java.io.*;

public class Main {
	public final static String outfile = "out.txt";

	public static void main(String[] args) throws FileNotFoundException {
		FileParser mParser = new FileParser();
		Setup inputData = new Setup();
		mParser.parse("sampleGraph3.txt", inputData);
//		System.out.println(inputData.roads.size());
//		System.out.println(inputData.predictions.size());
//		System.out.println(inputData.actualTraffic.get(0))
//		System.out.println(inputData.actualTraffic.size());
//		System.out.println(inputData.findRoadByName("ErgotelisAve").destNodeName);
		if (inputData.validateData()) {
			System.out.println("Valid Parse, outputing to \"" + outfile + "\"");
			PrintStream o = new PrintStream(new File(outfile));
			System.setOut(o); // System.out now outputs to the file out.txt
			Algorithms alg = new Algorithms(inputData);
			for (int i = 0; i < inputData.actualTraffic.size(); i++) {
				System.out.println("---------------Day " + i + "-----------------");
				alg.runIDAStar(inputData.getPredictionDay(i));
				alg.printResults(inputData.getPredictionDay(i));
				alg.runIDAStar(inputData.getActualTrafficDay(i));
				alg.printResults(inputData.getActualTrafficDay(i));
//				alg.runUCS(inputData.getPredictionDay(i));
//				alg.printResults(inputData.getPredictionDay(i));
//				alg.runUCS(inputData.getActualTrafficDay(i));
//				alg.printResults(inputData.getActualTrafficDay(i));
//				alg.runDijkstra(inputData.getPredictionDay(i));
//				alg.printResults(inputData.getPredictionDay(i));
//				alg.runDijkstra(inputData.getActualTrafficDay(i));
//				alg.printResults(inputData.getActualTrafficDay(i));
//				alg.runUCS(inputData.getPredictionDay(i));
//				alg.printResults(inputData.getPredictionDay(i));
//				alg.runBFS();
//				alg.printResults(inputData.getPredictionDay(i),inputData.getActualTrafficDay(i));
			}
			double cost = alg.allDaysCost / inputData.actualTraffic.size();
			System.out.println("The total mean cost is: " + cost);

		} else {
			System.err.println("Parse check failed");
			System.exit(0);
		}
	}

}