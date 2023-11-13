package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.*;
import jvm.cot.javacotloader.services.CotPagingSortingService;
import jvm.cot.javacotloader.services.SmaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("calculations")
public class CotCalculationsController {
    private static final Logger logger = LoggerFactory.getLogger(CotCalculationsController.class);
    private final CotPagingSortingService pagingSortingService;
    private final SmaService smaService;

    @Autowired
    public CotCalculationsController(
            CotPagingSortingService pagingSortingService,
            SmaService smaService
    ) {
        this.pagingSortingService = pagingSortingService;
        this.smaService = smaService;
    }

    @GetMapping(value = "/net", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getWithNetValues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date,desc;id") String sort
    ) {
        try {
            Page<Cot> cotPage = pagingSortingService.getByPageSorted(page, size, sort);
            CotBuilder cotBuilder = new CotBuilder(cotPage).withNetValues(true);
            CotPaginatedResponse cotResponse = cotBuilder.build();
            logger.info("Retrieved " + cotResponse.getCots().size() + " records");
            return ResponseEntity.ok(cotResponse.toMap());
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            String msg = "Error occurred: " + e.getMessage() + "\n" + e;
            logger.error(msg);
            errorResponse.put("message", msg);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping(value = "/sma-calculate", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> insertSmas(
            @RequestBody InsertSmaRequestBody insertSmaRequest
            ) {
        String market = insertSmaRequest.getMarket();
        Integer period = insertSmaRequest.getPeriod();
        if (market == null || market.isBlank() || period == null || period <= 0) {
            logger.error("Invalid request: " + insertSmaRequest);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Invalid request: " + insertSmaRequest);
            return ResponseEntity.badRequest().body(responseMap);
        }
        Map<String, Object> response = new HashMap<>();
        try {
            smaService.insertSmaForPeriodMarket(period, market);
            response.put("message", "Successfully inserted SMA for period " + period + " and market " + market);
            logger.info("Successfully inserted SMA for period " + period + " and market " + market);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage() + "\n" + e);
            response.put("message", "Error occurred: " + e.getMessage() + "\n" + e);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping(value = "/sma", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> getCotsWithSma(
            @RequestBody SmaRequestBody smaRequest
            ) {
        String market = smaRequest.getMarket();
        Integer period = smaRequest.getPeriod();
        String sort = smaRequest.getSort();
        Integer page = smaRequest.getPage();
        Integer size = smaRequest.getSize();
        if (market == null || market.isBlank() || period == null || period <= 0) {
            logger.error("Invalid request: " + smaRequest);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Invalid request: " + smaRequest);
            return ResponseEntity.badRequest().body(responseMap);
        }
        try {
            CotPaginatedResponse cotResponse = smaService.getCotResponseWithSma(period, market, page, size, sort);
            return ResponseEntity.ok(cotResponse.toMap());
        } catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage() + "\n" + e);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Error occurred: " + e.getMessage() + "\n" + e);
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @PostMapping(value = "/sma-all", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> getCotsWithAllSmas(
            @RequestBody SmaAllRequestBody smaRequest
    ) {
        String market = smaRequest.getMarket();
        String sort = smaRequest.getSort();
        Integer page = smaRequest.getPage();
        Integer size = smaRequest.getSize();
        if (market == null || market.isBlank()) {
            logger.error("Invalid request: " + smaRequest);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Invalid request: " + smaRequest);
            return ResponseEntity.badRequest().body(responseMap);
        }
        try {
            CotPaginatedResponse cotResponse = smaService.getCotResponseWithAllSmas(market, page, size, sort);
            return ResponseEntity.ok(cotResponse.toMap());
        } catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage() + "\n" + e);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Error occurred: " + e.getMessage() + "\n" + e);
            return ResponseEntity.badRequest().body(responseMap);
        }
    }
}
