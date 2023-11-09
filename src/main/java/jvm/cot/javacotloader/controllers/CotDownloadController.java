package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.services.DownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://github.com/christine-berlin/COT-Charts/blob/master/src/UpdateExcelFiles.java">UpdateExcelFiles.java</a>
 */
@RestController
public class CotDownloadController {
    private static final Logger logger = LoggerFactory.getLogger(CotDownloadController.class);
    private final DownloadService downloadService;

    @Autowired
    public CotDownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @GetMapping("/download/{startYear}/{endYear}")
    public ResponseEntity<Map<String, Object>> downloadCot(@PathVariable int startYear, @PathVariable int endYear) {
        Map<String, Object> response = new HashMap<>();
        try {
            downloadService.downloadCots(startYear, endYear);
        } catch (Exception e) {
            String message = "Error downloading COT for " + startYear +
                    " to " + endYear + ": " + e.getMessage();
            logger.error(message);
            response.put("message", message);
            return ResponseEntity.badRequest().body(response);
        }
        String message = "Successfully downloaded COT for " + startYear + " to " + endYear;
        logger.info(message);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
}
