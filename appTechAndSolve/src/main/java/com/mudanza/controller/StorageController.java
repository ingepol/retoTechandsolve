package com.mudanza.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mudanza.configuration.StorageProperties;
import com.mudanza.exception.PreconditionException;
import com.mudanza.exception.StorageException;
import com.mudanza.exception.StorageFileNotFoundException;
import com.mudanza.response.Response;
import com.mudanza.service.IStorageService;

/**
 * Controlador Rest encargado de recibir la petición de carga del archivo a procesar,
 * posterior a la carga, este es procesado y finalmente se genera un archivo de 
 * respuesta.
 * 
 * 
 * @author Paul Andrés Arenas Cardona
 * @version 1.0 
 * 
 * Fecha de creación 2018-05-07
 *
 */
@RestController
@RequestMapping("/file")
public class StorageController {

    private final IStorageService storageService;
    private final String fileNameOut; 

    @Autowired
    public StorageController(IStorageService storageService, StorageProperties properties) {
        this.storageService = storageService;
        this.fileNameOut = properties.getFileNameOut();
    }

    @GetMapping(value="/{filename}", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<Object> serveFile(@PathVariable String filename) {
        return responseResource(filename);
    }

    /**
     * Punto de entrada para cargar archivo de entrada, procesarlo
     *  y generar un archivo de salida. 
     * @param file
     * @return Archivo de salida.
     */
    @PostMapping(value="/", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> handleFileUpload(@RequestParam("file") MultipartFile file) {
    	try {
    		File fileUpdated = storageService.store(file);
    		storageService.processFile(fileUpdated);
    		return responseResource(fileNameOut);
		} catch (StorageException se) {
			return ResponseEntity.badRequest().body(new Response<>(HttpStatus.BAD_REQUEST.value(), se.getMessage()));
		} catch (PreconditionException pe) {
			return ResponseEntity.badRequest().body(new Response<>(HttpStatus.BAD_REQUEST.value(), pe.getMessage()));
		} 
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<Object> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Preparar archivo de salida, para su descarga 
     * 
     * @param filename
     * @return archivo de salida
     */
    private ResponseEntity<Object> responseResource(String filename){
    	Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + file.getFilename() ).body(file);
    }

}
