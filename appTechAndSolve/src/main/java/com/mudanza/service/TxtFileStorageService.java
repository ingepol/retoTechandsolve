package com.mudanza.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mudanza.configuration.StorageProperties;
import com.mudanza.core.ProcessFile;
import com.mudanza.exception.StorageException;
import com.mudanza.exception.StorageFileNotFoundException;
import com.mudanza.utils.Constants;

/**
 * Implementación del servicio que procesara las tareas relacionadas con la
 * manipulación de un archivo plano (txt).
 * 
 * 
 * @author Paul Andrés Arenas Cardona
 * @version 1.0
 * 
 *          Fecha de creación 2018-05-07
 * 
 */
@Service
public class TxtFileStorageService implements IStorageService {

	private final Path rootLocation;
	private final ProcessFile processFile;

	@Autowired
	public TxtFileStorageService(StorageProperties properties, ProcessFile processFile) {
		this.rootLocation = Paths.get(properties.getLocation());
		this.processFile = processFile;
	}

	/**
	 * Se encarga de realizar validacion referen al archivo que se desea cargar.
	 * Crea una copia del archivo en el storage
	 * 
	 * @param file
	 *            Archivo recibido desde un cliente.
	 * @return El archivo almacenado en el storage
	 * @exception StorageException
	 *                si el archivo no tiene extensión txt, esta vació o tiene
	 *                doble punto ..
	 */
	@Override
	public File store(MultipartFile file) {
		String filename = "";
		try (InputStream inputStream = file.getInputStream()) {
			/*
			 * Report SpotBug (2018-05/09 13:30)
			 * 
			 * Possible null pointer dereference in
			 * com.mudanza.service.TxtFileStorageService.store(MultipartFile)
			 * due to return value of called method [Troubling(13), Normal
			 * confidence]
			 */
			filename = StringUtils.cleanPath(file.getOriginalFilename());

			if (!filename.endsWith(".txt")) {
				throw new StorageException(Constants.FORMATFILE);
			}

			if (filename.contains("..")) {
				throw new StorageException(Constants.MALFORMEDPATH);
			}

			if (file.isEmpty()) {
				throw new StorageException(Constants.EMPTYFILE);
			}

			Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			throw new StorageException(Constants.SAVEFILE + filename, e);
		} catch (NullPointerException ne) {
			/*
			 * Solución de posible null pointer exception reportado por SpotBug
			 * al llamar el método file.getOriginalFilename()
			 */
			throw new StorageException(Constants.FILENOTFOUND + filename, ne);
		}
		return new File(rootLocation.toString(), filename);
	}

	/**
	 * @return retorna el path de un archivo.
	 */
	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	/**
	 * @return retorna el resource de un archivo
	 * @exception StorageFileNotFoundException
	 *                cuando no se encuentra un archivo
	 * @MalformedURLException La url no tiene un formato correcto.
	 */
	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException(Constants.FILENOTFOUND + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException(Constants.ACCESSFILE + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {

		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException(Constants.CREATINGDIRECTORY, e);
		}

	}

	/**
	 * Se encarga de realizar el llamado al core del negocio para procesar la
	 * información y generar un archivo de salida con el resultado.
	 */
	@Override
	public void processFile(File fileUpdated) {
		processFile.generateResponse(fileUpdated);
	}
}
