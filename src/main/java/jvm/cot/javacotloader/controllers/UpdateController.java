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

    @GetMapping("/seed")
    public ResponseEntity<MessageResponse> seedDatabase() {
        var resp = new MessageResponse(null, "Successfully seeded database");
        return ResponseEntity.ok(resp);
    }


    @GetMapping("/check-update")
    public ResponseEntity<MessageResponse> checkUpdate() {
        var resp = new MessageResponse(null, "Successfully checked update");
        return ResponseEntity.ok(resp);
    }

    // TODO - may be an operation in the scheduled job rather than here
    @GetMapping("/copy-database")
    public ResponseEntity<MessageResponse> copyDatabase(@RequestParam String destPath) {
        var resp = new MessageResponse(null, String.format("Successfully copied database to %s", destPath));
        return ResponseEntity.ok(resp);
    }
}
