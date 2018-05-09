package com.mudanza.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.mudanza.exception.StorageException;

public class Utils {

	private Utils() {
		throw new IllegalStateException("Utility class");
	}

	public static List<Integer> descendingOrder(List<Integer> list) {
		int temp;
		for (int i = 0; i < list.size(); i++) {
			for (int x = i + 1; x < list.size(); x++) {
				if (list.get(x) > list.get(i)) {
					temp = list.get(i);
					list.set(i, list.get(x));
					list.set(x, temp);
				}
			}
		}
		return list;
	}

	public static List<Integer> listOfIntegersToProcess(File file)  {

		List<Integer> lines = new ArrayList<>();

		try (
				FileReader fileReader = new FileReader(file);
				BufferedReader buffer = new BufferedReader(fileReader);
			){
			

			String currentLine;
			while ((currentLine = buffer.readLine()) != null) {
				lines.add(Integer.parseInt(currentLine));
			}

		} catch (Exception e) {
			throw new StorageException(Constants.INPUTLINEISNOTANINTEGER);
		} 
		return lines;
	}

	public static void linesToWrite(String pathFileOutName, List<Integer> listResult)  {
		int i = 1;
		File newFile = new File(pathFileOutName);
		try (
				FileWriter fileWriter = new FileWriter(newFile);
				BufferedWriter buffer = new BufferedWriter(fileWriter);
			){

			for (Integer line : listResult) {
				buffer.write(Constants.OUTPUT + i++ + ": " + line);
				buffer.newLine();
			}

		} catch (Exception e) {
			throw new StorageException(Constants.OUTPUTWRITER);
		} 

	}

}
