package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.MessageResponse;
import jvm.cot.javacotloader.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * <a href="https://github.com/christine-berlin/COT-Charts/blob/master/src/UpdateExcelFiles.java">UpdateExcelFiles.java</a>
 */
@RestController
public class CotDownloadController {
    private static final Logger logger = LoggerFactory.getLogger(CotDownloadController.class);
    private final FileService fileService;

    @Autowired
    public CotDownloadController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download/{startYear}/{endYear}")
    public ResponseEntity<MessageResponse> downloadCot(@PathVariable String startYear, @PathVariable String endYear) {
        int start = Integer.parseInt(startYear);
        int end = Integer.parseInt(endYear);
        fileService.createDirectories();
        logger.info("Downloading COT for " + startYear + " to " + endYear);
        for (int year = start; year <= end; year++) {
            try {
                URL url = getUrl(year);
                String filePath = fileService.getZipFilePath(year);
                logger.info("Downloading from " + url + " to " + filePath);
                downloadZip(filePath, url);
                // TODO - this will unzip everything, update for bulk download
                fileService.unzipCots();
            } catch (Exception e) {
                logger.error("Error downloading COT for " + year + ": " + e.getMessage());
                return ResponseEntity.badRequest().body(new MessageResponse("Error downloading COT for " + year + ": " + e.getMessage()));
            }
        }
        return ResponseEntity.ok(new MessageResponse("Success"));
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
}
