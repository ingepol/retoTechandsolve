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
import com.mudanza.utils.file.FileSystemImpl;
import com.mudanza.utils.file.IFileSystem;

@Service
public class TxtFileStorageService implements IStorageService {
	
	private final Path rootLocation;
	private final ProcessFile processFile;

	@Autowired
	public TxtFileStorageService(StorageProperties properties, ProcessFile processFile) {
		this.rootLocation = Paths.get(properties.getLocation());
		this.processFile = processFile;
	}

	@Override
	public File store(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (!filename.endsWith(".txt")){
				throw new StorageException(Constants.FORMATFILE);
			}
			
			if (filename.contains("..")) {
				throw new StorageException(Constants.MALFORMEDPATH);
			}
			
			if (file.isEmpty()) {
				throw new StorageException(Constants.EMPTYFILE);
			}
			
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException(Constants.SAVEFILE + filename, e);
		}
		return new File(rootLocation.toString(), filename);
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
		IFileSystem fileSystem = new FileSystemImpl();

		try {
			fileSystem.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException(Constants.CREATINGDIRECTORY, e);
		}
	}

	@Override
	public void processFile(File fileUpdated) {
		processFile.generateResponse(fileUpdated);		
	}
}
