import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.FileReader;

public class MLProject {
	
	static int[] numClasses = {5, 11, 9, 9, 11};;
	static String[][] trainingData;
	static String[][] testingData;
	static String[][] currentData;
	static String[] labelData;
	static int k, set;
	static Boolean fixed = true;
	
	static String[][] microData;
	
	public static void main(String[] args) {
		
		
		//String[] training = {"TrainData1.txt","TrainData2.txt", "TrainData3.txt", "TrainData4.txt","TrainData5.txt"};
		int [] trainSamples = {150, 100, 6300, 2547, 1119};
		int[] trainFeatures = {3312, 9182, 13, 112, 11};
		
		//String[] testing = {"TestData1.txt","TestData2.txt", "TestData3.txt", "TestData4.txt", "TestData5.txt"};
		int [] testSamples = {53, 74, 2693, 1092, 480};
		int[] testFeatures = {3312, 9182, 13, 112, 11};
		
		// Question 1 - Part 1: Missing value estimation using KNN algorithm.
		/*
		fixed = false;	
		for(int i = 0; i < training.length; i++) {
			// Read the data-set from file
			getData(training[i], trainSamples[i], trainFeatures[i]);
			currentData = trainingData;
			fixData();	// Estimate missing values
			writeData(training[i]); // Write the repaired data-set to new file
			System.out.println();			
		}
		for(int i = 0; i < testing.length; i++) {
			// Read the data-set from file
			getData(testing[i], testSamples[i], testFeatures[i]);
			currentData = testingData;
			fixData();	// Estimate missing values
			writeData(testing[i]); // Write the repaired data-set to new file
			System.out.println();			
		}
		fixed = true;
		*/
		
		// Question 1 - Part 2: Classification of testing data.
		
		String[] newTraining = {"NewTrainData1.txt", "NewTrainData2.txt", "NewTrainData3.txt", "NewTrainData4.txt","NewTrainData5.txt"};
		String[] newTesting = {"NewTestData1.txt", "NewTestData2.txt", "NewTestData3.txt", "NewTestData4.txt", "NewTestData5.txt"};
		String[] labels = {"TrainLabel1.txt", "TrainLabel2.txt", "TrainLabel3.txt", "TrainLabel4.txt", "TrainLabel5.txt"};
		for(int i = 0; i < newTraining.length; i++) { //newTraining.length
			set = (i+1);
			getData(newTraining[i], trainSamples[i], trainFeatures[i]);		
			getData(newTesting[i], testSamples[i], testFeatures[i]);
			getData(labels[i],trainSamples[i],0);
			// now I have 2d arrays: trainingData and testingData 
			// and vector: labelData
			// Find K nearest neighbours for each testing instance.
			classify();			
		}
		
		
		// Question 2:
		/*
		String [] micros = {"MissingData1.txt", "MissingData2.txt"};
		int [] microSamples = {14, 50};
		int [] microFeatures = {242, 758};
		
		
		fixed = false;	
		for(int i = 0; i < micros.length; i++) {
			// Read the data-set from file
			getData(micros[i], microSamples[i], microFeatures[i]);
			currentData = microData;
			fixData();	// Estimate missing values
			writeData(micros[i]); // Write the repaired data-set to new file
			System.out.println();			
		}
		fixed = true;
		*/			
	}
	
	public static void getData(String filename, int samples, int features) {

	//reads the training data set and adds to a 2d String array called trainingData.	
	//reads the testing data set and adds to a 2d String array called testingData	
		System.out.println("Retrieving data from "+filename);
		try {
			FileReader fileReader = new FileReader("./input/"+filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
    		
            // For Labels 
            if(filename.contains("Label")) {
            	
            	labelData = new String[samples];

            	for(int i = 0; i < labelData.length; i++) {
            		line = bufferedReader.readLine().trim();
	            	labelData[i] = line;
	            }
	            System.out.println("Success!\n");
            	
            }  
            // For microArrays
            else if(filename.contains("Missing")) {
            	System.out.println(filename+" Samples: "+samples+" Features: "+features);
	            microData = new String[features][samples];
	
	            String[] lineSplit;
	            for(int i = 0; i < microData.length; i++) {
	            	line = bufferedReader.readLine().trim();
	            	lineSplit = line.split("\\s+|\\s*,\\s*");
	                for (int j = 0; j < microData[i].length; j++) {
	                		microData[i][j] = lineSplit[j];	                	
	                }
	            }
	            
	            // Calculate appropriate value of K = sqrt(number of samples) / 2
	            double d = Math.sqrt(microData.length) / 2;
	            k = (int)d;       
	            bufferedReader.close();
	            System.out.println("Success!\n");
            }
            
            // For training data
            else if(filename.contains("Train")) {
            	
            	System.out.println(filename+" Samples: "+samples+" Features: "+features);
	            trainingData = new String[samples][features];
	
	            String[] lineSplit;
	            for(int i = 0; i < trainingData.length; i++) {
	            	line = bufferedReader.readLine().trim();
	            	lineSplit = line.split("\\s+|\\s*,\\s*");
	                for (int j = 0; j < trainingData[i].length; j++) {	                		                	
	                		trainingData[i][j] = lineSplit[j];	                	
	                }
	            }
	            
	            // Calculate appropriate value of K = sqrt(number of samples) / 2
	            double d = Math.sqrt(trainingData.length) / 2;
	            k = (int)d;       
	            bufferedReader.close();
	            System.out.println("Success!\n");
            }
            // For testing data
            else if(filename.contains("Test")) {
            	
            	 // hard coded, need to change later
            	testingData = new String[samples][features];
            		        
	            String[] lineSplit;
	            for(int i = 0; i < testingData.length; i++) {
	            	line = bufferedReader.readLine().trim();
	            	lineSplit = line.split("\\s+|\\s*,\\s*");
	                for (int j = 0; j < testingData[i].length; j++) {	                		                	
	                		testingData[i][j] = lineSplit[j];	                	
	                }
	            }
	         // Calculate appropriate value of K = sqrt(number of samples) / 2
	            double d = Math.sqrt(testingData.length) / 2;
	            k = (int)d;       
	            bufferedReader.close();
	            System.out.println("Success!\n");
            }
		}
		catch(IOException io) {
			System.out.println("File: "+filename+" not found.");
			System.exit(1);
		}	
	}
	
	public static void fixData() {
		// Searches for missing values, gets the K-nearest neighbours for that instance,
		//  and replaces the missing value with the mean value for that attribute
		System.out.println("Examining data for missing values..");
		for(int i = 0; i < currentData.length; i++) {
			int missing = 0;
			System.out.print("Line "+(i+1)+"... ");
			for(int j = 0; j < currentData[i].length; j++) {
				if(currentData[i][j].contains("+99")) {
					missing++;
					String[] query = currentData[i];

					//get k nearest neighbours
					Distance[] kNearest = myKnn(i, query);					
					
					//get mean value
					double sum = 0;
					for(int n = 0; n < k; n++) {
						Double val = Double.parseDouble(currentData[kNearest[n].index][j]);
						sum += val;
					}
					 				
					String mean = Double.toString(sum / k);					
					//change value					
					currentData[i][j] = mean;
				}
			}
			System.out.println("Missing values fixed: "+missing);
			
		}
		System.out.println("All Missing Values fixed!!");
	}
	
	public static Distance[] myKnn(int index, String[] query) {
		
		Distance[] distances;
		
		if(fixed == false) {
		// finds the k-nearest neighbours to the instance with the missing attribute
		// and returns an array of 'Distance' objects made up of the distance value 
		// and index of each of the nearest k instances
			
			distances = new Distance[currentData.length-1];
			int j = 0;
			
			for(int i = 0; i < currentData.length; i++) {		
					String[] instance = currentData[i];	
					Distance d = new Distance(getDistance(instance, query), i);
					if(i != index) {
						distances[j] = d;
						j++;
					}		
			}			
		}
		else {
			distances = new Distance[trainingData.length];
			for(int i = 0; i < trainingData.length ; i++) {	
					String[] instance = trainingData[i];					
					Distance d = new Distance(getDistance(instance, query), i);	
					distances[i] = d;												
			}		
		}	
		Arrays.sort(distances);
		Distance[] kNearest = Arrays.copyOfRange(distances, 0, k);
		return kNearest;
	}
	
	public static double getDistance(String[] s1, String[] s2) {
	//Calculates the Euclidean distance between two vectors while 
	//ignoring any other missing values in the data-set
		
		double distance = 0;
		for(int i = 0; i < s2.length ; i++) {
			if(s1[i].contains("+99") || s2[i].contains("+99")) {
				distance += 0;
			}
			else {
				double d1 = Double.parseDouble(s1[i]);
				double d2 = Double.parseDouble(s2[i]);
				double d = Math.pow((d2 - d1),2);
				distance += d;
				
			}
			
		}
		return Math.sqrt(distance);
	}
	
	public static void writeData(String filename) {
	// Writes the repaired data set to a new file
		String file;
		if(filename.contains("Missing")) {
			file = "./output/BurgessNew"+filename;
		}
		else {
			file = "./input/New"+filename;
		}
		try {
			PrintWriter pw = new PrintWriter(file);
			for(int k=0; k<currentData.length; k++) {
	           	for(int l=0; l<currentData[k].length; l++) {
	           		pw.print(currentData[k][l]+"\t");
	           	}
	           	pw.print("\n");
	           }
	           pw.close();
	           System.out.println("Fixed data saved to: "+file);
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
           
           
	}
	
	public static void classify() {
		
		String[] results = new String[testingData.length];
		//for each instance in testing data;
		for(int i = 0; i < testingData.length; i++) { //testingData.length
			
			String[] query = testingData[i];			
			Distance[] kNearest = myKnn(-1, query);
			
			String[] labels = new String[kNearest.length];
			for(int j = 0; j<kNearest.length; j++) {
				labels[j] = labelData[kNearest[j].index];
			}	
			int res = getWeighted(labels, kNearest);
			
			results[i] = ""+res;			
		}

		// Write results to file
		try {
			PrintWriter pw = new PrintWriter("./output/BurgessClassification"+set+".txt");
			for(int k=0; k<results.length; k++) {
					System.out.println(results[k]);
	           		pw.print(results[k]+"\n");	           		           	
	         }
	         pw.close();
	         System.out.println("Testing labels saved to: BurgessClassification"+set+".txt\n");
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}
	
	public static int getWeighted(String[] labels, Distance[] kNearest){
		//returns the class value with the highest weights based on the K nearest neighbours
		int[] classes = new int[numClasses[set-1]];
		double[] scores = new double[classes.length];
		for(int i=0; i<classes.length; i++) {
				classes[i] = i+1;		
		}
		
		for(int j = 0; j<classes.length; j++) {
			for(int k = 0; k<labels.length; k++) {
				if(String.valueOf(classes[j]).equals(labels[k])) {
					scores[j] += 1/kNearest[k].distValue;
				}
			}
		}		
		double[] sorted = scores.clone();
		Arrays.sort(sorted);
		for(int l=0; l<scores.length; l++) {
			if(sorted[(sorted.length)-1] == scores[l]){
				return classes[l];
			}
		}
		return -1;
	}
}

class Distance implements Comparable<Distance> {
	
	// The distance object used in myKnn method,
	// it contains the instance index and distance from the query-instance.
	// Made comparable by distance value for sorting.
	double distValue;
	int index;
	
	Distance(double distValue, int index) {
		this.distValue = distValue;
		this.index = index;
	}
	
	public int compareTo(Distance d) {
		if(distValue == d.distValue) {
			return 0;
		}
		else if(distValue > d.distValue) {
			return 1;
		}
		else {
			return -1;
		}
	}
	
	public String toString() {
		return "Index: "+index+" Distance: "+distValue+"\n";
	}
	
}