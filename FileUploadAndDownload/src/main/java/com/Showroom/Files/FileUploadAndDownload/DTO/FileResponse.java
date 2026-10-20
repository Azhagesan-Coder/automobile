package com.Showroom.Files.FileUploadAndDownload.DTO;
import java.time.LocalDateTime;
import com.Showroom.Files.FileUploadAndDownload.Model.FilesDocument;
public record FileResponse(Long id, String fileName, String fileType, LocalDateTime uploadAt) {
    public static FileResponse from(FilesDocument doc) {
        return new FileResponse(doc.getId(), doc.getFileName(), doc.getFileType(), doc.getUploadAt());
    }
}