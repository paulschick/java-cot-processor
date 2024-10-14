package jvm.cot.javacotloader.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class TestFileService {
    private FileService fileService;

    @BeforeEach
    public void beforeEach() {
        fileService = new FileService();
    }

    @Test
    public void testGetTempDirPath() throws IOException {
        var tempDirPath = fileService.getTempDirPath();
        assertNotNull(tempDirPath, "The temporary path should not be null");
        var tempDir = Path.of(tempDirPath);
        assertTrue(Files.exists(tempDir), "The temporary path should exist");
        assertTrue(Files.isDirectory(tempDir), "The temporary path should be a directory");
        var dirName = tempDir.getFileName().toString();
        assertTrue(dirName.startsWith(FileService.getTempDirPrefix()),
                String.format("the directory should start with %s", FileService.getTempDirPrefix()));
        Files.deleteIfExists(tempDir);
    }

    @Test
    public void testGetFilePath(@TempDir Path tempDir) {
        var tempDirPath = tempDir.toAbsolutePath().toString();
        var fileName = "test.txt";
        var filePath = fileService.getFilePath(tempDirPath, fileName);
        assertNotNull(filePath, "file path should not be null");
        var expectedPath = tempDir.resolve(fileName).toAbsolutePath();
        assertEquals(expectedPath.toString(), filePath, "the filepath should match the expected path");
    }

    @Test
    public void testStreamToTempFile() throws IOException {
        var content = "test content";
        var testData = content.getBytes();
        var source = new ByteArrayResource(testData);
        var fileName = "test.txt";
        var filePath = fileService.streamToTempFile(source, fileName);
        var tempFilePath = Path.of(filePath);
        assertTrue(Files.exists(tempFilePath), "temp file must exist");
        var fileData = Files.readAllBytes(tempFilePath);
        var fileDataContent = new String(fileData, StandardCharsets.UTF_8);
        assertEquals(content, fileDataContent, "content should be equal");

        Files.deleteIfExists(tempFilePath);
        Files.deleteIfExists(tempFilePath.getParent());
    }
}
