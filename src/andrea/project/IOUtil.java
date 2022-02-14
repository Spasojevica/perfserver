package andrea.project;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IOUtil {
	public static void toJSON(List<Measurements> array, String filename) {
		Gson gson =  new GsonBuilder().setPrettyPrinting().create();
		 try (FileWriter writer = new FileWriter(filename)) {
	            gson.toJson(array, writer);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}
	
	public static List<Measurements> fromJSON(String filename) {
		Gson gson =  new Gson();
		   try (Reader reader = new FileReader(filename)) {

	            // Convert JSON File to Java Object
			   
			     Measurements[] array= gson.fromJson(reader, Measurements[].class);
				
				// print staff object
	            System.out.println(array);
	            return new ArrayList<Measurements>(Arrays.asList(array));

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		   return new ArrayList<Measurements>();

	}

}
