package com.mudanza.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.mudanza.exception.StorageException;
import com.mudanza.exception.StorageFileNotFoundException;
import com.mudanza.response.Response;
import com.mudanza.service.IStorageService;


@Controller
public class StorageController {

    private final IStorageService storageService;

    @Autowired
    public StorageController(IStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(StorageController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping(value="/files/{filename}", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping(value="/", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
    	try {
    		storageService.store(file);
		} catch (StorageException ex) {
			return ResponseEntity.badRequest().body(new Response<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
		}
    	return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "El archivo "+file.getOriginalFilename()+" subió correctamente!"));
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
