package com.Showroom.Files.FileUploadAndDownload.Model;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "files_document")
public class FilesDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fileName;
	private String fileType;
	private String filePath;
	private LocalDateTime uploadAt;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public LocalDateTime getUploadAt() {
		return uploadAt;
	}

	public void setUploadAt(LocalDateTime uploadAt) {
		this.uploadAt = uploadAt;
	}

	public FilesDocument() {
		this.uploadAt = LocalDateTime.now();
	}
}
