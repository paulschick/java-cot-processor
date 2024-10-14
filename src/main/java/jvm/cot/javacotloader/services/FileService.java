package jvm.cot.javacotloader.services;

import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileService {
    private static final String TEMP_DIR_PREFIX = "COT";

    public String getTempDirPath() throws IOException {
        return Files.createTempDirectory(TEMP_DIR_PREFIX).toFile().getAbsolutePath();
    }

    public String getFilePath(String tempDir, String fileName) {
        return Path.of(tempDir, fileName).toAbsolutePath().toString();
    }

    public String streamToTempFile(InputStreamSource source, String fileName) throws IOException {
        var fp = getFilePath(getTempDirPath(), fileName);
        try (var inputStream = source.getInputStream();
             var outputStream = new FileOutputStream(fp)) {
            var buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return fp;
    }

    public static String getTempDirPrefix() {
        return TEMP_DIR_PREFIX;
    }
}
