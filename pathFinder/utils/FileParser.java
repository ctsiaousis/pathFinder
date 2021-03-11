package utils;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.*;
import pathFinder.*;

public class FileParser {
	
	public int chooseSectionBeggining(String line) {
		if(line.contains("<Source>")) {
			return 1;
		}
		else if(line.contains("<Destination>")) {
			return 2;
		}
		else if(line.contains("<Roads>")) {
			return 3;
		}
		else if(line.contains("<Predictions>")) {
			return 4;
		}
		else if(line.contains("<Day>")) {
			return 5;
		}
		else if(line.contains("<ActualTrafficPerDay>")) {
			return 6;
		}
		else if(line.isEmpty()) {
			return 0;
		}
		else {
			return -1;
		}
	}
	
	public int chooseSectionEnding(String line) {
		if(line.contains("</Source>")) {
			return 1;
		}
		else if(line.contains("</Destination>")) {
			return 2;
		}
		else if(line.contains("</Roads>")) {
			return 3;
		}
		else if(line.contains("</Predictions>")) {
			return 4;
		}
		else if(line.contains("</Day>")) {
			return 5;
		}
		else if(line.contains("</ActualTrafficPerDay>")) {
			return 6;
		}
		else if(line.isEmpty()) {
			return 0;
		}
		else {
			return -1;
		}
	}
	
	public String getInBetween(String s) {
		String tmp = s.substring(s.indexOf('>')+1);
		return tmp.substring(0,tmp.indexOf('<'));
	}
	public Road createRoad(String line) {
		List<String> items = Arrays.asList(line.split("\\s*;\\s*"));
		
		System.out.println(items);

		Road mRoad = new Road(items.get(0), 
				items.get(1), items.get(2), 
				Integer.parseInt(items.get(3)));
		return mRoad;
	}
	
	public void parseLineToDay(String line, Day day) {
	
		List<String> items = Arrays.asList(line.split("\\s*;\\s*"));
		day.appendTouple(items.get(0),items.get(1));
		
	}
	
	public void parse(String filePath, Setup mySetup) {
	    try {
	      File myObj = new File(filePath);
	      Scanner myReader = new Scanner(myObj);
	      int startingSymbol=0, endingSymbol=0; //source=1 , destination=2 , roads=3 , prediction=4 , day=5 , actualTrafficPerDay=6
	      int currentSection=0;
	      int outterSection=0;
	      Day tmpDay = new Day();
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        startingSymbol=chooseSectionBeggining(data);
	        endingSymbol=chooseSectionEnding(data);
	        
	        if(startingSymbol == -1 && endingSymbol == -1) {
	        	if(currentSection==0) {
	        		System.out.println("De 3erw");
	        	}
	        	else if(currentSection==3) {
//	        		System.out.println("Diabazw dromous");
	        		mySetup.addRoad(createRoad(data));
	        		outterSection = 3;
	        	}else if(currentSection==4) {
//	        		System.out.println("Diabazw Predictions");
	        		outterSection = 4;
	        	}else if(currentSection==6) {
//	        		System.out.println("Diabazw Kinisi");
	        		outterSection = 6;
	        	}else if(currentSection == 5) {
//	        		System.out.println("Diabazw Mera");
	        		parseLineToDay(data,tmpDay);
	        	}
	        	
	        }else if(startingSymbol==endingSymbol) {
	        	//<section>kati </section>  thelw na parw to kati
	        	currentSection=startingSymbol;
	        	if(currentSection == 1) {
	        		mySetup.source = getInBetween(data);
	        		System.out.println("Source: "+mySetup.source);
	        	}else if(currentSection == 2) {
	        		mySetup.destination = getInBetween(data);
	        		System.out.println("Dest: "+mySetup.destination);

	        	}
	        }else if(endingSymbol == -1 ) {
	        	if(startingSymbol==5) {
	        		tmpDay = new Day();
	        	}
	        	//allazw section
        		currentSection=startingSymbol;
        		if(startingSymbol==6 || currentSection==4) {
        			outterSection=startingSymbol;
        		}
	        }else {//I've read closing symbol
//		        System.out.println(data);
		        if(endingSymbol == 5) {
		        	if(outterSection == 4) {
		        		tmpDay.setIsPrediction(true);
		        		mySetup.addPredictionDay(tmpDay);
//		        		System.out.println("Mera Pred");
		        	}else if(outterSection == 6) {
		        		tmpDay.setIsPrediction(false);
		        		mySetup.addActualTrafficDay(tmpDay);
//		        		System.out.println("Mera Actual");
		        	}else {
		        		System.out.println("error");
		        	}
		        }
        	}
	      }
//	      System.out.println(mySetup.actualTraffic);
	      
	      myReader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	  }

	public FileParser() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
