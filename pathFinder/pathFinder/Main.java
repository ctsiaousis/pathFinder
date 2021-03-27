package pathFinder;

import utils.FileParser;
import java.io.*;

public class Main {
	private static String inFile = "sampleGraph2.txt";

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
		} else {
			System.err.println("Parse check failed");
			System.exit(0);
		}
	}

}