package com.mudanza.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Setear las propiedades de configuración para la creación de archivos
 * 
 * @author Paul Andrés Arenas Cardona
 * @version 1.0 
 * 
 * Fecha de creación 2018-05-08
 *
 */

@ConfigurationProperties("storage")
public class StorageProperties {
	/**
     * Directorio donde se guardara los archivos.
     */
    private String location = "upload-dir";
    
    /**
     * Nombre del archivo de salida y se escribe el resultado del proceso.
     */
    private String fileNameOut = "lazy_loading_example_output.txt";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

	public String getFileNameOut() {
		return fileNameOut;
	}

	public void setFileNameOut(String fileNameOut) {
		this.fileNameOut = fileNameOut;
	}
	
	public String getFilePathOut(){
		return getLocation().concat("//"+getFileNameOut());
	}
    
    
}
