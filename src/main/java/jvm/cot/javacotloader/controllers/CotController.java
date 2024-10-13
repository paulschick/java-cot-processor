package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.response.TestCotResponse;
import jvm.cot.javacotloader.services.CotClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cot")
public class CotController {
    private static final Logger logger = LoggerFactory.getLogger(CotController.class);
    private final CotClient cotClient;

    public CotController(CotClient cotClient) {
        this.cotClient = cotClient;
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<TestCotResponse> getAllCot() {
        try {
            return ResponseEntity.ok(new TestCotResponse(cotClient.getAllCots()));
        } catch (Exception e) {
            logger.error("exception retrieving all COTs", e);
            return ResponseEntity.internalServerError().body(new TestCotResponse(e.getMessage()));
        }
    }

    @GetMapping(value = "/after", produces = "application/json")
    public ResponseEntity<TestCotResponse> getCotAfterDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        try {
            return ResponseEntity.ok(new TestCotResponse(cotClient.getCotsAfterDate(year, month, day)));
        } catch (Exception e) {
            logger.error("exception retrieving COTs", e);
            return ResponseEntity.internalServerError().body(new TestCotResponse(e.getMessage()));
        }
    }
}
