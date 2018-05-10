package com.mudanza.exception;

/**
 * Exception generada cuando no se encuentra un archivo o ruta de directorio.
 * 
 * @author Paul Andrés Arenas Cardona
 * @version 1.0 
 * 
 * Fecha de creación 2018-05-07
 *
 */
public class StorageFileNotFoundException extends StorageException {


	private static final long serialVersionUID = 1L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}