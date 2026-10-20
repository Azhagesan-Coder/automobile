package com.Showroom.Files.FileUploadAndDownload;
import java.nio.file.*;
import com.Showroom.Files.FileUploadAndDownload.Repository.FileRepo;
import com.Showroom.Files.FileUploadAndDownload.Service.FileService;
import com.Showroom.Files.FileUploadAndDownload.DTO.FileResponse;
import com.Showroom.Files.FileUploadAndDownload.Model.FilesDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class FileServiceTest {
    @TempDir Path folder;
    @Test void rejectsPathComponents() {
        FileService service = new FileService(mock(FileRepo.class));
        ReflectionTestUtils.setField(service, "upload", folder.toString());
        assertThrows(ResponseStatusException.class, () -> service.saveFile(
            new MockMultipartFile("file", "../escape.txt", "text/plain", new byte[]{1})));
    }
    @Test void savesFileAndReturnsMetadataWithoutPath() throws Exception {
        FileRepo repo = mock(FileRepo.class);
        when(repo.save(any(FilesDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));
        FileService service = new FileService(repo);
        ReflectionTestUtils.setField(service, "upload", folder.toString());
        FilesDocument doc = service.saveFile(new MockMultipartFile("file", "receipt.txt", "text/plain", "receipt".getBytes()));
        assertEquals("receipt", Files.readString(Path.of(doc.getFilePath())));
        assertEquals(doc.getFileName(), FileResponse.from(doc).fileName());
        assertNotNull(doc.getUploadAt());
    }
    @Test void databaseFailureRemovesOrphanFile() throws Exception {
        FileRepo repo = mock(FileRepo.class);
        when(repo.save(any(FilesDocument.class))).thenThrow(new IllegalStateException("database unavailable"));
        FileService service = new FileService(repo);
        ReflectionTestUtils.setField(service, "upload", folder.toString());
        assertThrows(IllegalStateException.class, () -> service.saveFile(new MockMultipartFile("file", "receipt.txt", "text/plain", new byte[]{1})));
        try (var files = Files.list(folder)) { assertEquals(0, files.count()); }
    }
}
