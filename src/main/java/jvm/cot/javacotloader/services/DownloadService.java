package jvm.cot.javacotloader.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

@Service
public class DownloadService {
    private static final Logger logger = LoggerFactory.getLogger(DownloadService.class);
    private final FileService fileService;

    @Autowired
    public DownloadService(FileService fileService) {
        this.fileService = fileService;
    }

    public void downloadCots(int startYear, int endYear) throws Exception {
        fileService.createDirectories();
        logger.info("Downloading COT for " + startYear + " to " + endYear);
        for (int year = startYear; year <= endYear; year++) {
            URL url = getUrl(year);
            String filePath = fileService.getZipFilePath(year);
            logger.info("Downloading from " + url + " to " + filePath);
            downloadZip(filePath, url);
        }
        fileService.unzipCots(true);
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
