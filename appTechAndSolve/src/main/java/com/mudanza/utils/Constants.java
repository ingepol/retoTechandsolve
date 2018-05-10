package com.mudanza.utils;
/**
* Clase de constantes, con los posibles mensajes de respuesta.
* 
* 
* @author Paul Andrés Arenas Cardona
* @version 1.0 
* 
* Fecha de creación 2018-05-07
* 
*/
public class Constants {

	private Constants() {
	   throw new IllegalStateException("Constants class");
	}

	public static final String FORMATFILE = "Solo se permiten archivos planos (.txt)";
	public static final String MALFORMEDPATH = "No se puede almacenar el archivo con una ruta relativa fuera del directorio actual.";
	public static final String INPUTLINEISNOTANINTEGER = "Error leyendo los datos de entrada";
	public static final String EMPTYFILE = "El archivo está vacío";
	public static final String SAVEFILE = "Error al almacenar el archivo: ";
	public static final String READFILESAVED = "Error al leer los archivos almacenados";
	public static final String FILENOTFOUND = "No existe o no se puedo leer el archivo: ";
	public static final String ACCESSFILE = "No se pudo acceder al archivo: ";
	public static final String CREATINGDIRECTORY = "No se pudo crear el directorio para guardar los archivos";
	public static final String LISTNULLOREMPTY = "No hay suficientes datos en el archivo de entrada para procesar la solicitud";
	public static final String WORKEDAYSNOTVALID = "Los días trabajados deben estar entre 1 y 500";
	public static final String WEIGHTSOUTOFPARAMETERS = "El peso de cada elemento debe estar entre 0 y 100";
	public static final String ELEMENTSOUTOFPARAMETERS = "La cantidad de elementos debe estar entre 1 y 100";
	public static final String OUTPUTWRITER = "Error escribiendo el archivo de salida";

	public static final String OUTPUT = "Case #";

}
