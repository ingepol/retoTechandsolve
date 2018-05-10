package com.mudanza.service;

import java.io.File;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
/**
* Interface del servicio que procesara las tareas
* relacionadas con la manipulación de un archivo.
* 
* 
* @author Paul Andrés Arenas Cardona
* @version 1.0 
* 
* Fecha de creación 2018-05-07
* 
*/


public interface IStorageService {

	void init();

    File store(MultipartFile file);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

	void processFile(File fileUpdated);

}
