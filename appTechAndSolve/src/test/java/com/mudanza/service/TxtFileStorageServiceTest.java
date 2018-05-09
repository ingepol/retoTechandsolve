package com.mudanza.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.mudanza.configuration.StorageProperties;
import com.mudanza.core.ProcessFile;
import com.mudanza.exception.StorageException;
import com.mudanza.exception.StorageFileNotFoundException;
import com.mudanza.utils.Constants;

public class TxtFileStorageServiceTest {
	TxtFileStorageService service = null;
	StorageProperties properties = null;

	@Before
	public void setUp() {
		properties = new StorageProperties();
		service = new TxtFileStorageService(properties, new ProcessFile(properties));
	}
	
	@Test
	public void testExistDirectory() {
		File file = new File("upload-dir");
		service.init();
		Assert.assertTrue(file.exists() && file.isDirectory());
	}

	@Test
	public void testFileOutputNotFoundException() {
		try {
			service.loadAsResource(pathAndFileString(false));
			Assert.fail();
		} catch (StorageFileNotFoundException se) {
			Assert.assertTrue(se.getMessage().contains(Constants.FILENOTFOUND));
		}
	}

	@Test
	public void testLoadAsResource() {
		String filename = "lazy_loading_example_output.txt";
		service.init();
		createFileTest(pathAndFileString(false),"");
		Resource resourse = service.loadAsResource(filename);
		Assert.assertTrue(resourse.getFilename().equals(filename));
		service.deleteAll();
	}


	@Test
	public void testBadExtencionFile() {
		try {
			service.init();
			createFileTest(pathAndFileString(true),"");
			MultipartFile file = mockMultipartFile(pathAndFileString(true).replaceAll(".txt", ".pdf"));
			service.store(file);
			Assert.fail();
		} catch (StorageException se) {
			Assert.assertTrue(se.getMessage().contains(Constants.FORMATFILE));
		} finally {
			service.deleteAll();
		}
	}

	@Test
	public void testFileIsEmpty() {
		try {
			service.init();
			createFileTest(pathAndFileString(true),"");
			MultipartFile file = mockMultipartFile(pathAndFileString(true));
			service.store(file);
			Assert.fail();
		} catch (StorageException se) {
			Assert.assertTrue(se.getMessage().contains(Constants.EMPTYFILE));
		} finally {
			service.deleteAll();
		}
	}

	@Test
	public void testDoubleDotInPath() {
		try {
			service.init();
			createFileTest(pathAndFileString(true),"1");
			MultipartFile file = mockMultipartFile(pathAndFileString(true).replaceAll(".txt", "..txt"));
			service.store(file);
			Assert.fail();
		} catch (StorageException se) {
			Assert.assertTrue(se.getMessage().contains(Constants.MALFORMEDPATH));
		} finally {
			service.deleteAll();
		}
	}

	@Test
	public void testSaveFileInput() {
		try {
			service.init();
			createFileTest(pathAndFileString(true),"1");
			MultipartFile file = mockMultipartFile(pathAndFileString(true));
			File fileExist = service.store(file);
			Assert.assertTrue(fileExist.isFile());
		} catch (StorageException se) {
			Assert.fail();
		} finally {
			service.deleteAll();
		}
	}

	private String pathAndFileString(boolean input) {
		String directory = "upload-dir\\";
		String filename = input?"lazy_loading_example_input.txt":"lazy_loading_example_output.txt";
		return directory.concat(filename);
	}

	private MultipartFile mockMultipartFile(String filename) {
		Path path = Paths.get(filename);
		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
		}
		MultipartFile multipartFile = new MockMultipartFile("file", filename.split("\\\\")[1], "text/plain", content);
		return multipartFile;
	}

	private void createFileTest(String filename, String content) {
		String ruta = filename;
		File archivo = new File(ruta);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
			if (archivo.exists()) {
				bw.write(content);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	

}
