package pathFinder;

import utils.FileParser;
import java.io.*;

public class Main {
	private static String inFile = "sampleGraph3.txt";

	public static void main(String[] args) throws FileNotFoundException {
		FileParser mParser = new FileParser();
		Setup inputData = new Setup();
	    if(args.length == 0){
	        System.out.println("No input file provided. Defaulting to: "+inFile);
	    }else {
	    	inFile = args[0];
	    }
		boolean isParseOK = mParser.parse(inFile, inputData);
		String outFile = inFile.substring(0, inFile.lastIndexOf('.'))+"_out.txt";
		
		if (isParseOK && inputData.validateData()) {
			System.out.println("Valid Parse, outputing to \"" + outFile + "\"");
			PrintStream o = new PrintStream(new File(outFile));
			System.setOut(o); // System.out now outputs to the file out.txt
			Algorithms alg = new Algorithms(inputData);
			alg.runAndPrint();
//			System.out.println("111");
//			alg.runLRTAStar(inputData.getActualTrafficDay(0));
//			System.out.println("222");
//			for(int i = 0; i < inputData.actualTraffic.size(); i++) {
//				alg.runLRTAStar(inputData.getPredictionDay(i));
//				alg.printResultsNew(inputData.getPredictionDay(i), inputData.getActualTrafficDay(i));
////				alg.runBFS();
////				alg.printResultsNew(inputData.getPredictionDay(0), inputData.getActualTrafficDay(0));
//			}
		} else {
			System.err.println("Parse check failed");
			System.exit(0);
		}
	}

}