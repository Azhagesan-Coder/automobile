package com.Showroom.Files.FileUploadAndDownload.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Showroom.Files.FileUploadAndDownload.Model.FilesDocument;

@Repository
public interface FileRepo extends JpaRepository<FilesDocument, Long> {

}
