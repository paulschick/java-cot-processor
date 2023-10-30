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
    private static final Logger logger = LoggerFactory.getLogger(MethodsTestController.class);

    @GetMapping("/test")
    public String testMethod() {
        logger.info("Test Method called");

        // CREATE FOLDER
        logger.info("Creating folder: " + BASE_COT_DIR);
        File dir = new File(BASE_COT_DIR);
        boolean result = dir.mkdir();
        logger.info("Folder created: " + result);

        int year = Calendar.getInstance().get(Calendar.YEAR);

        // DOWNLOAD COT ZIP
        logger.info("Downloading COT for " + year);
        try {
            URL url = getUrl(year);
            String filePath = getFilePath(year);
            logger.info("Downloading from " + url + " to " + filePath);
            downloadZip(filePath, url);
        } catch (Exception e) {
            logger.error("Error downloading COT for " + year + ": " + e.getMessage());
            return "Error downloading COT for " + year + ": " + e.getMessage();
        }

        // EXTRACT ZIP

        return "test";
    }

    private String getFilePath(int year) {
        return BASE_COT_DIR + "/" + "dea_fut_xls_" + year + ".zip";
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
}
