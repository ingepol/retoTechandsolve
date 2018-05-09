package com.mudanza.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mudanza.configuration.StorageProperties;
import com.mudanza.exception.StorageException;
import com.mudanza.exception.StorageFileNotFoundException;

@Service
public class txtFileStorageService implements IStorageService {
	private final Path rootLocation;

	@Autowired
	public txtFileStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void store(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (!filename.endsWith(".txt")){
				throw new StorageException("Solo se permiten archivos planos (.txt)");
			}
			if (file.isEmpty()) {
				throw new StorageException("El archivo esta vacío");
			}
			if (filename.contains("..")) {
				throw new StorageException(
						"No se puede almacenar el archivo con una ruta relativa fuera del directorio actual.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("Error al almacenar el archivo: " + filename, e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
					.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Error al leer los archivos almacenados", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("No existe o no se puedo leer el archivo: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("No se pudo acceder al archivo: " + filename, e);
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
			throw new StorageException("No se pudo crear el directorio para guardar los archivos", e);
		}
	}
}
