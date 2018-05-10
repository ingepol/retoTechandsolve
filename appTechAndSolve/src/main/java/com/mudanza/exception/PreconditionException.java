package com.mudanza.exception;


/**
 * Exception generada cuando se cumplen las restricciones del negocio. 
 * Validar el número de días a trabjar 1 ≤​ T​ ≤ 500 
 * Validar cantidad de elementos a transportar por día 1 ≤ ​N​ ≤ 100 
 * Validar el peso de cada elemento a transportar 1 ≤ ​Wi​ ≤ 100  
 * 
 * @author Paul Andrés Arenas Cardona
 * @version 1.0 
 * 
 * Fecha de creación 2018-05-07
 *
 */


public class PreconditionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PreconditionException(String message) {
        super(message);
    }

    public PreconditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
