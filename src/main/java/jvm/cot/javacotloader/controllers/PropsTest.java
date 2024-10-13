package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.PropsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("printProps")
public class PropsTest {
    private static final Logger logger = LoggerFactory.getLogger(PropsTest.class);

    @Value("${cftc.appToken}")
    private String apiKey;

    @Value("${cftc.secretToken}")
    private String apiSecret;

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<PropsResponse> getProps() {
        return ResponseEntity.ok(new PropsResponse(apiKey, apiSecret));
    }
}
