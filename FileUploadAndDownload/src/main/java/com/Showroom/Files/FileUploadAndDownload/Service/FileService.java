package com.Showroom.Files.FileUploadAndDownload.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Showroom.Files.FileUploadAndDownload.Model.FilesDocument;
import com.Showroom.Files.FileUploadAndDownload.Repository.FileRepo;

@Service
public class FileService {

	@Value("${file.upload-dir}")
	private String upload;
	
	private final FileRepo repository;
	
	public FileService(FileRepo repository) {
		this.repository=repository;
	}

	public FilesDocument saveFile(MultipartFile file) throws IOException {
		Path folder = Paths.get(upload).toAbsolutePath().normalize();
		Files.createDirectories(folder);
		
		if (file.isEmpty()) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "File is empty");
        String original = file.getOriginalFilename();
        if (original == null || original.isBlank() || original.contains("/") || original.contains("\\") || original.contains("\r") || original.contains("\n")) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid filename");
        }
        String fName = java.util.UUID.randomUUID() + "_" + original;
		Path filePath = folder.resolve(fName);
		try (java.io.InputStream input = file.getInputStream()) {
			Files.copy(input, filePath);
		}
		
		FilesDocument doc = new FilesDocument();
		doc.setFileName(fName);
		doc.setFileType(file.getContentType());
		doc.setFilePath(filePath.toString());
		
		try { return repository.save(doc); }
        catch (RuntimeException e) { Files.deleteIfExists(filePath); throw e; }
	}
	
	public Optional<FilesDocument> getFile(Long id){
		return repository.findById(id);
	}
	
}
