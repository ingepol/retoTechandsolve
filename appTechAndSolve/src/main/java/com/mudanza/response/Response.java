package com.mudanza.response;

/**
 * Clase generica para manejar una respuesta estandar en todas las peticiones
 * Rest
 * 
 * @author Paul Andrés Arenas Cardona
 * @version 1.0 
 * 
 * Fecha de creación 2018-05-07
 *
 */
public class Response<T> {
	private T data;
	private int code;
	private String message;

	public Response(int code, T data) {
		this.code = code;
		this.data = data;
	}
	
	public Response(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public Response(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}



	/**
	 * @return the data
	 */
	public T getData() {
		return this.data;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
