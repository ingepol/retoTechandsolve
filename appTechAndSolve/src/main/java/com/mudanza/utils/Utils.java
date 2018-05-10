package com.mudanza.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.mudanza.exception.StorageException;

/**
* Clase de utilidades, con métodos para:
*  Leer el archivo de entrada y retornar una lista de enteros.
*  Escribir los resultados y generar un archivo de salida. 
* 
* @author Paul Andrés Arenas Cardona
* @version 1.0 
* 
* Fecha de creación 2018-05-07
* 
*/
public class Utils {

	private Utils() {
		throw new IllegalStateException("Utility class");
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
