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
import java.util.Optional;

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
            return unknownErrorResponse(e);
        }
    }

    @PostMapping(value = "/sma-calculate", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> insertSmas(
            @RequestBody InsertSmaRequestBody insertSmaRequest
    ) {
        String market = insertSmaRequest.getMarket();
        Integer period = insertSmaRequest.getPeriod();
        Optional<ResponseEntity<Map<String, Object>>> invalidMap = tryValidMarketPeriod(market, period, "Invalid request: " + insertSmaRequest);
        if (invalidMap.isPresent()) return invalidMap.get();
        Map<String, Object> response = new HashMap<>();
        try {
            smaService.insertSmaForPeriodMarket(period, market);
            response.put("message", "Successfully inserted SMA for period " + period + " and market " + market);
            logger.info("Successfully inserted SMA for period " + period + " and market " + market);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return unknownErrorResponse(e);
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
        Optional<ResponseEntity<Map<String, Object>>> invalidMap = tryValidMarketPeriod(market, period, "Invalid request: " + smaRequest);
        if (invalidMap.isPresent()) return invalidMap.get();
        try {
            CotPaginatedResponse cotResponse = smaService.getCotResponseWithSma(period, market, page, size, sort);
            return ResponseEntity.ok(cotResponse.toMap());
        } catch (Exception e) {
            return unknownErrorResponse(e);
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
        Optional<ResponseEntity<Map<String, Object>>> invalidMap = tryValidMarket(market, "Invalid request: " + smaRequest);
        if (invalidMap.isPresent()) return invalidMap.get();
        try {
            CotPaginatedResponse cotResponse = smaService.getCotResponseWithAllSmas(market, page, size, sort);
            return ResponseEntity.ok(cotResponse.toMap());
        } catch (Exception e) {
            return unknownErrorResponse(e);
        }
    }

    private Optional<ResponseEntity<Map<String, Object>>> tryValidMarketPeriod(String market, Integer period, String s) {
        if (market == null || market.isBlank() || period == null || period <= 0) {
            logger.error(s);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", s);
            return Optional.of(ResponseEntity.badRequest().body(responseMap));
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity<Map<String, Object>>> tryValidMarket(String market, String badRequestMessage) {
        if (market == null || market.isBlank()) {
            logger.error(badRequestMessage);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", badRequestMessage);
            return Optional.of(ResponseEntity.badRequest().body(responseMap));
        }
        return Optional.empty();
    }

    private ResponseEntity<Map<String, Object>> unknownErrorResponse(Exception e) {
        String message = "Unexpected error occurred: " + e.getMessage() + "\n" + e;
        logger.error(message);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);
        return ResponseEntity.badRequest().body(responseMap);
    }
}
