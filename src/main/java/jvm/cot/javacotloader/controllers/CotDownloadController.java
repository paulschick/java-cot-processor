package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.MessageResponse;
import jvm.cot.javacotloader.services.DownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<MessageResponse> downloadCot(@PathVariable int startYear, @PathVariable int endYear) {
        try {
            downloadService.downloadCots(startYear, endYear);
        } catch (Exception e) {
            logger.error("Error downloading COT for " + startYear + " to " + endYear + ": " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Error downloading COT for " + startYear +
                    " to " + endYear + ": " + e.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Success"));
    }
}
