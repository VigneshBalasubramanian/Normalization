/*
Source code file name: vb0018Normalize.java"
Date: 02/06/2017
References: 1. CS641 Class Notes, Dr. Ramazan Aygun
			2. http://www.hub4tech.com/			
			3. http://stackoverflow.com/
*/


//Header files
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;


public class vb0018_rh0059Normalize {
	public static void main(String args[]){
		
		//Variable to store the location of the file// 
		String location_File = null; 
		//Instantiation of the arraylist variable that would hold whole list of values as arraylist//
		ArrayList<ArrayList<String>> data_Array = new ArrayList<ArrayList<String>>();
		//Array to hold the calculated average values//
		Double[] mean_Value;
		//Array to hold the calculated standard deviation values//
		Double[] standard_deviated_Value;
		//2-D array to save the z-score normalized values//
		double[][] z_score_normalized_Array;		

		//Argument validation to check the availability of input file name//
		if(args.length < 2){
			System.out.println("\n Provide input file name in the format of vb0018_rh0059Normalize -i <InputFile> -c <classattribute>");
			return;
		}

		//Scraping file path name from arguments//
		for(int i=0; i<args.length; i++){
			if(args[i].equals("-i")){
				location_File = args[i+1];				
			}
		}

		//Scraping input file name from the file path//
		String[] location_File_Split = location_File.split(Pattern.quote("/"));
		String inputFileName = location_File_Split[location_File_Split.length - 1];

		//Creating output files
		File MeanStd_File = new File("vb0018_rh0059MeanStd" + inputFileName);
		File Normalized_File = new File("vb0018_rh0059Normalize" + inputFileName);

		try {
			//System.out.println(args[3]);
			//Normal write//
			FileWriter absolute_write_output1 = new FileWriter(MeanStd_File.getAbsoluteFile());
			//Buffered writing for efficiency to prevent conversion of characters into bytes.//
			BufferedWriter buffer_write_output1 = new BufferedWriter(absolute_write_output1); 

			FileWriter absolute_write_output2 = new FileWriter(Normalized_File.getAbsoluteFile());
			BufferedWriter buffer_write_output2 = new BufferedWriter(absolute_write_output2);

			BufferedReader buffer_read = new BufferedReader(new FileReader(location_File));
			String line;
			
			int Column_Count = 0 ;
			int Row_Count = 0;
			boolean read_Start = false;
			int linenum = 0;
			int temp_linenum=0;

			//Reading input file
			while ((line = buffer_read.readLine()) != null) {
				linenum++;
				if(line.contains("@attribute class")){
					temp_linenum = linenum;	
					System.out.println(temp_linenum);
					System.out.println(args.length);
				}			
				
				if(!read_Start){
					buffer_write_output1.write(line);
					buffer_write_output1.write("\n");
					buffer_write_output2.write(line);
					buffer_write_output2.write("\n");
				}

				if(line.equals("@data")){
					read_Start = true;
					continue;
				}

				if(read_Start){
					//Reading and storing the values from file
					String[] lineSplit = line.trim().split("\\s+");
					Column_Count = lineSplit.length;
					++Row_Count;
					data_Array.add(new ArrayList<String>(Arrays.asList(lineSplit)));
				}
			}
			
			//No class attribute//
			if(temp_linenum == 0 || args.length < 3){			
			mean_Value = new Double[Column_Count];
			standard_deviated_Value = new Double[Column_Count];

			z_score_normalized_Array = new double[Row_Count][Column_Count];

			for(int i=0; i<Column_Count; i++){
				ArrayList<Double> Value_Array = new ArrayList<Double>();
				double sum = 0;
				double average = 0;
				for(int j=0; j< Row_Count; j++){
					//Extracting column arrays//
					Value_Array.add(Double.parseDouble(data_Array.get(j).get(i)));
				}

				//Mean calculation//
				for(int k=0; k < Value_Array.size(); k++)
					sum = sum + Value_Array.get(k);

				average = sum / Value_Array.size();
				mean_Value[i] = average;

				//Standard deviation calculation//
				double temp3 = 0;
				for(int k=0; k < Value_Array.size(); k++ ){
					double temp1 = Math.pow((Value_Array.get(k) - mean_Value[i]), 2);
					double temp2 = temp1/(Value_Array.size());
					temp3 = temp3 + temp2;
				}
				standard_deviated_Value[i] = Math.sqrt(temp3);

				//Normalization using z-score//
				for(int k=0; k < Value_Array.size(); k++ ){
					double normalization = (Value_Array.get(k) - mean_Value[i])/standard_deviated_Value[i];
					z_score_normalized_Array[k][i] = normalization;
				}
			}
			buffer_read.close();

			//Writing to output file
			for(int i=0; i < mean_Value.length; i++ ){
				buffer_write_output1.write(Double.toString(mean_Value[i]));
				buffer_write_output1.write(" ");
			}
			
			buffer_write_output1.write("\n");

			for(int i=0; i<standard_deviated_Value.length; i++){
				buffer_write_output1.write(Double.toString(standard_deviated_Value[i]));
				buffer_write_output1.write(" ");
			}
			
			buffer_write_output1.close();
			absolute_write_output1.close();

			for(int i=0; i< Row_Count ; i++){
				for(int j=0; j<Column_Count; j++){
					buffer_write_output2.write(Double.toString(z_score_normalized_Array[i][j]));
					buffer_write_output2.write("		");
				}
				buffer_write_output2.write("\n");
			}
			buffer_write_output2.close();
			}
			
			else // if there is class attribute//
			{
			mean_Value = new Double[Column_Count -1];
			standard_deviated_Value = new Double[Column_Count -1];

			z_score_normalized_Array = new double[Row_Count][Column_Count];

			for(int i=0; i<Column_Count-1; i++){
				if(i != temp_linenum - 3){
				System.out.println(i);				
				System.out.println(temp_linenum);					
				ArrayList<Double> Value_Array = new ArrayList<Double>();
				double sum = 0;
				double average = 0;
				for(int j=0; j< Row_Count; j++){
					//Extracting column arrays//
					Value_Array.add(Double.parseDouble(data_Array.get(j).get(i)));
				}

				//Mean calculation//
				for(int k=0; k < Value_Array.size(); k++)
					sum = sum + Value_Array.get(k);

				average = sum / Value_Array.size();
				mean_Value[i] = average;

				//Standard deviation calculation//
				double temp3 = 0;
				for(int k=0; k < Value_Array.size(); k++ ){
					double temp1 = Math.pow((Value_Array.get(k) - mean_Value[i]), 2);
					double temp2 = temp1/(Value_Array.size());
					temp3 = temp3 + temp2;
				}
				standard_deviated_Value[i] = Math.sqrt(temp3);

				//Normalizing using z-score//
				for(int k=0; k < Value_Array.size(); k++ ){
					double normalization = (Value_Array.get(k) - mean_Value[i])/standard_deviated_Value[i];
					z_score_normalized_Array[k][i] = normalization;
				}
				continue;
				}
			}
			buffer_read.close();

			//Writing to output file//
			for(int i=0; i < mean_Value.length; i++ ){
				buffer_write_output1.write(Double.toString(mean_Value[i]));
				buffer_write_output1.write(" ");
			}
			
			buffer_write_output1.write("\n");

			for(int i=0; i<standard_deviated_Value.length; i++){
				buffer_write_output1.write(Double.toString(standard_deviated_Value[i]));
				buffer_write_output1.write(" ");
			}
			
			buffer_write_output1.close();
			absolute_write_output1.close();

			for(int i=0; i< Row_Count ; i++){
				for(int j=0; j<Column_Count - 1; j++){
					buffer_write_output2.write(Double.toString(z_score_normalized_Array[i][j]));
					buffer_write_output2.write("		");
				}
				buffer_write_output2.write("\n");
			}
			buffer_write_output2.close();
			}
		}
		catch(Exception e){
			//To backtrace to the standard error stream//
			e.printStackTrace();
		}
	}
}