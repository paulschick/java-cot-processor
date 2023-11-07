package jvm.cot.javacotloader.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private final String baseCotDir = "data";

    public String getBaseCotDir() {
        return baseCotDir;
    }

    public String getZipCotDir() {
        return baseCotDir + "/zip";
    }

    public String getUnzipCotDir() {
        return baseCotDir + "/unzip";
    }

    public String getZipFilePath(int year) {
        return getZipCotDir() + "/dea_fut_xls_" + year + ".zip";
    }

    public void createDirectories() {
        File dir = new File(baseCotDir);
        checkCreateDir(dir);
        File zipDir = new File(getZipCotDir());
        checkCreateDir(zipDir);
        File unzipDir = new File(getUnzipCotDir());
        checkCreateDir(unzipDir);
    }

    public void unzipCots() {
        File zipDir = new File(getZipCotDir());
        File unzipDir = new File(getUnzipCotDir());

        if (!zipDir.exists() || !unzipDir.exists()) {
            logger.warn("zipDir or unzipDir is null or does not exist.");
            return;
        }
        File[] files = zipDir.listFiles();
        try {
            if (files == null) {
                logger.warn("No files found.");
                return;
            }
            for (File file : files) {
                logger.info("Unzipping " + file.getName());
                String prefix = file.getName().substring(0, file.getName().lastIndexOf('.'));
                unzip(file.getAbsolutePath(), prefix);
            }
        } catch (Exception e) {
            logger.error("Error unzipping COT: " + e.getMessage());
        }
    }

    private void unzip(String zipFilePath, String prefix) {
        try (FileInputStream fis = new FileInputStream(zipFilePath)) {
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(getUnzipCotDir() + File.separator + prefix + ".xls");
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    zis.closeEntry();
                    zipEntry = zis.getNextEntry();
                } catch (Exception e) {
                    logger.error("Error unzipping " + zipFilePath + ": " + e.getMessage());
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Error unzipping " + zipFilePath + ": " + e.getMessage());
        }
    }

    private void checkCreateDir(File dir) {
        if (!dir.exists()) {
            boolean result = dir.mkdir();
            logger.debug("Created directory " + baseCotDir + ": " + result);
        }
    }
}
