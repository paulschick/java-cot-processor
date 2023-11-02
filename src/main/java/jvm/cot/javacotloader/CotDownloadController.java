package jvm.cot.javacotloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <a href="https://github.com/christine-berlin/COT-Charts/blob/master/src/UpdateExcelFiles.java">UpdateExcelFiles.java</a>
 */
@RestController
public class CotDownloadController {
    public static final String BASE_COT_DIR = "src/main/resources/cot-excel";
    public static final String ZIP_COT_DIR = BASE_COT_DIR + "/zip";
    public static final String UNZIP_COT_DIR = BASE_COT_DIR + "/unzip";
    private static final Logger logger = LoggerFactory.getLogger(CotDownloadController.class);

    @GetMapping("/download/{startYear}/{endYear}")
    public String downloadCot(@PathVariable String startYear, @PathVariable String endYear) {
        int start = Integer.parseInt(startYear);
        int end = Integer.parseInt(endYear);
        createDirectories();
        logger.info("Downloading COT for " + startYear + " to " + endYear);
        for (int year = start; year <= end; year++) {
            try {
                URL url = getUrl(year);
                String filePath = getZipFilePath(year);
                logger.info("Downloading from " + url + " to " + filePath);
                downloadZip(filePath, url);
                unzipCot(new File(ZIP_COT_DIR), new File(UNZIP_COT_DIR));
            } catch (Exception e) {
                logger.error("Error downloading COT for " + year + ": " + e.getMessage());
                return "Error downloading COT for " + year + ": " + e.getMessage();
            }
        }
        return "Success";
    }

    private String getZipFilePath(int year) {
        return ZIP_COT_DIR + "/dea_fut_xls_" + year + ".zip";
    }

    private void createDirectories() {
        File dir = new File(BASE_COT_DIR);
        checkCreateDir(dir);
        File zipDir = new File(ZIP_COT_DIR);
        checkCreateDir(zipDir);
        File unzipDir = new File(UNZIP_COT_DIR);
        checkCreateDir(unzipDir);
    }

    private void checkCreateDir(File dir) {
        if (!dir.exists()) {
            boolean result = dir.mkdir();
            logger.debug("Created directory " + BASE_COT_DIR + ": " + result);
        }
    }

    private URL getUrl(int year) throws MalformedURLException {
        String urlString;
        if (year > 2003) {
            urlString = "https://www.cftc.gov/sites/default/files/files/dea/history/dea_fut_xls_" +
                    year + ".zip";
        } else {
            urlString = "https://www.cftc.gov/sites/default/files/files/dea/history/deafut_xls_" +
                    year + ".zip";
        }
        return URI.create(urlString).toURL();
    }

    private void downloadZip(String filePath, URL url) throws Exception {
        try (BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
             FileOutputStream fileOs = new FileOutputStream(filePath)) {
            byte[] data = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOs.write(data, 0, byteContent);
            }
        }
    }

    private void unzipCot(File zipDir, File unzipDir) {
        // Expect these folder to have been created
        if (zipDir == null || unzipDir == null || !zipDir.exists() || !unzipDir.exists()) {
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
                String unzipPath = unzipDir.getAbsolutePath() + File.separator + prefix;
                logger.info("Unzipping to " + unzipPath);
                unzip(file.getAbsolutePath(), unzipPath, prefix);
            }
        } catch (Exception e) {
            logger.error("Error unzipping COT: " + e.getMessage());
        }
    }

    private void unzip(String zipFilePath, String destDirPath, String prefix) {
        try (FileInputStream fis = new FileInputStream(zipFilePath)) {
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(destDirPath + File.separator + prefix + ".xls");
                // Create all non exists folders
                new File(newFile.getParent()).mkdirs();
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
}
