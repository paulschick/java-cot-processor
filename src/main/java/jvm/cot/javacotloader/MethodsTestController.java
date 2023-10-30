package jvm.cot.javacotloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;

/**
 * <a href="https://github.com/christine-berlin/COT-Charts/blob/master/src/UpdateExcelFiles.java">UpdateExcelFiles.java</a>
 */
@RestController
public class MethodsTestController {
    public static final String BASE_COT_DIR = "src/main/resources/cot-excel";
    public static final String ZIP_COT_DIR = BASE_COT_DIR + "/zip";
    public static final String UNZIP_COT_DIR = BASE_COT_DIR + "/unzip";
    public static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private static final Logger logger = LoggerFactory.getLogger(MethodsTestController.class);

    @GetMapping("/test")
    public String testMethod() {
        // CREATE FOLDERS
        createDirectories();
        // DOWNLOAD COT ZIP
        logger.info("Downloading COT for " + YEAR);
        try {
            URL url = getUrl(YEAR);
            String filePath = getZipFilePath(YEAR);
            logger.info("Downloading from " + url + " to " + filePath);

            // Temporary, don't need to re-download
//            downloadZip(filePath, url);
            unzipCot(new File(ZIP_COT_DIR), new File(UNZIP_COT_DIR));
        } catch (Exception e) {
            logger.error("Error downloading COT for " + YEAR + ": " + e.getMessage());
            return "Error downloading COT for " + YEAR + ": " + e.getMessage();
        }

        // EXTRACT ZIP

        return "test";
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
        return URI.create("https://www.cftc.gov/sites/default/files/files/dea/history/dea_fut_xls_" +
                year + ".zip").toURL();
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
                String unzipPath = unzipDir.getAbsolutePath() + "/" + prefix;
                // make path for windows or linux
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    unzipPath = unzipPath.replace("/", "\\");
                } else {
                    unzipPath = unzipPath.replace("\\", "/");
                }
                logger.info("Unzipping to " + unzipPath);
            }
        } catch (Exception e) {
            logger.error("Error unzipping COT: " + e.getMessage());
        }
    }
}
