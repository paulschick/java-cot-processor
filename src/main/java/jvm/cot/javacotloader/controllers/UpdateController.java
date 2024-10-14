package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO Authorize the endpoints for this controller
// API Key / Secret is good enough. The scheduler job will hold the credentials. Will be on the same server.
@RestController
@RequestMapping("update")
public class UpdateController {
    private static final Logger logger = LoggerFactory.getLogger(UpdateController.class);

    @GetMapping(value = "/seed", produces = "application/json")
    public ResponseEntity<MessageResponse> seedDatabase(
            @RequestParam(name = "api-key", required = false) String apiKey
    ) {
        var resp = new MessageResponse(null, "Successfully seeded database");
        return ResponseEntity.ok(resp);
    }


    @GetMapping(value = "/check-update", produces = "application/json")
    public ResponseEntity<MessageResponse> checkUpdate(
            @RequestParam(name = "api-key", required = false) String apiKey
    ) {
        var resp = new MessageResponse(null, "Successfully checked update");
        return ResponseEntity.ok(resp);
    }

    // TODO - may be an operation in the scheduled job rather than here
    @GetMapping(value = "/copy-database", produces = "application/json")
    public ResponseEntity<MessageResponse> copyDatabase(
            @RequestParam String destPath,
            @RequestParam(name = "api-key", required = false) String apiKey
    ) {
        var resp = new MessageResponse(null, String.format("Successfully copied database to %s", destPath));
        return ResponseEntity.ok(resp);
    }
}
