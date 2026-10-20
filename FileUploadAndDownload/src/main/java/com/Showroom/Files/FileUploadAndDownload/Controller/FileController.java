package com.Showroom.Files.FileUploadAndDownload.Controller;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Showroom.Files.FileUploadAndDownload.Model.FilesDocument;
import com.Showroom.Files.FileUploadAndDownload.Service.FileService;

@RestController
@RequestMapping("/api/files")
public class FileController {

	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping("/upload")
	public ResponseEntity<com.Showroom.Files.FileUploadAndDownload.DTO.FileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			FilesDocument saved = fileService.saveFile(file);
			return ResponseEntity.ok(com.Showroom.Files.FileUploadAndDownload.DTO.FileResponse.from(saved));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
	    Optional<FilesDocument> optionalDoc = fileService.getFile(id);

	    if (optionalDoc.isPresent()) {
	        FilesDocument doc = optionalDoc.get();
	        try {
	            Path path = Paths.get(doc.getFilePath());
	            Resource resource = new UrlResource(path.toUri());

	            if (!resource.exists() || !resource.isReadable()) {
	                return ResponseEntity.notFound().build();
	            }

	            return ResponseEntity.ok()
	                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                    .header(HttpHeaders.CONTENT_DISPOSITION, org.springframework.http.ContentDisposition.attachment().filename(doc.getFileName(), java.nio.charset.StandardCharsets.UTF_8).build().toString())
	                    .body(resource);
	        } catch (MalformedURLException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

}
