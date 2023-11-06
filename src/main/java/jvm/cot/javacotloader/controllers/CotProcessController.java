package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.MessageResponse;
import jvm.cot.javacotloader.services.CotProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("process")
public class CotProcessController {
    private static final Logger logger = LoggerFactory.getLogger(CotProcessController.class);
    private final CotProcessingService cotProcessingService;

    @Autowired
    public CotProcessController(CotProcessingService cotProcessingService) {
        this.cotProcessingService = cotProcessingService;
    }

    @GetMapping(value = "/{fileNo}", produces = "application/json")
    public ResponseEntity<MessageResponse> writeAllRowsForFileNumber(@PathVariable int fileNo) {
        try {
            cotProcessingService.writeFileByIndex(fileNo);
            logger.info("Successfully processed file number " + fileNo);
            return ResponseEntity.ok(new MessageResponse("success"));
        } catch (Exception e) {
            logger.error("Error processing file number " + fileNo + ": " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
