package cs4600;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Helper class to read and write files
public class FileReaderWriter {
	
	public static void writeToFile(String fileName, String key) throws IOException {
		try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + File.separator + fileName))) {
		    bufferedWriter.write(key);
		} catch (IOException e) {
			System.out.println("Error with writing the file!");
		}
	}
	
	public static String readFile(String fileName) {
		
		String str = "";
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + fileName))) {
		    String line = bufferedReader.readLine();
		    while(line != null) {
		    	str = str + line + "\n";
		        line = bufferedReader.readLine();
		    }
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("Error with reading the file!");
		}
		return str;
	}
	
	
}
