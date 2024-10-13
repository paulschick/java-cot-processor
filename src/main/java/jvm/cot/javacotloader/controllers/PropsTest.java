package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.PropsResponse;
import jvm.cot.javacotloader.models.response.CftcResponse;
import jvm.cot.javacotloader.models.response.TestCotResponse;
import jvm.cot.javacotloader.services.CotClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("printProps")
public class PropsTest {
    private static final Logger logger = LoggerFactory.getLogger(PropsTest.class);

    private final String apiKey;
    private final String apiSecret;
    private final CotClient cotClient;

    public PropsTest(@Value("${cftc.appToken}") String apiKey,
                     @Value("${cftc.secretToken}") String apiSecret,
                     CotClient cotClient) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.cotClient = cotClient;
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<PropsResponse> getProps() {
        return ResponseEntity.ok(new PropsResponse(apiKey, apiSecret));
    }

    @GetMapping(value = "/cot/test", produces = "application/json")
    public ResponseEntity<List<CftcResponse>> getCots() {
        logger.info("RETRIEVING COTS");
        return ResponseEntity.ok(cotClient.getAllCots());
    }

    @GetMapping(value = "/cot/after", produces = "application/json")
    public ResponseEntity<TestCotResponse> getCotsAfterDate(
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

    @GetMapping(value = "/dt/test", produces = "application/json")
    public ResponseEntity<Map<String, String>> getDtTest() {
        var respMap = new HashMap<String, String>();
        logger.info("year: 2024, Month: 10, Day: 2");
        var resp = cotClient.getURLAfterDate(2024, 10, 2);
        respMap.put("dt", resp);
        return ResponseEntity.ok(respMap);
    }
}
