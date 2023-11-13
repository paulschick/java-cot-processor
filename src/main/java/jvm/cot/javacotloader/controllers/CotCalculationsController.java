package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.*;
import jvm.cot.javacotloader.models.entities.Cot;
import jvm.cot.javacotloader.models.request.PaginatedMarketRequest;
import jvm.cot.javacotloader.models.request.PaginatedSingleSmaRequest;
import jvm.cot.javacotloader.models.request.PaginationRequest;
import jvm.cot.javacotloader.models.request.SingleSmaRequest;
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

    @PostMapping(value = "/net", produces = "application/json")
    public ResponseEntity<Map<String, Object>> postGetCotsWithNet(
            @RequestBody PaginationRequest paginationRequest
    ) {
        try {
            paginationRequest = cleanPaginationRequest(paginationRequest);
            logger.info("Processing request: " + paginationRequest);
            int page = paginationRequest.getPage();
            int size = paginationRequest.getSize();
            String sort = paginationRequest.getSort();
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
            @RequestBody SingleSmaRequest request
            ) {
        String market = request.getMarket();
        Integer period = request.getPeriod();
        Optional<ResponseEntity<Map<String, Object>>> invalidMap = tryValidMarketPeriod(market, period, "Invalid request: " + request);
        if (invalidMap.isPresent()) return invalidMap.get();
        logger.info("Processing request: " + request);
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
            @RequestBody PaginatedSingleSmaRequest request
            ) {
        String market = request.getMarket();
        Integer period = request.getPeriod();
        PaginationRequest paginationRequest = cleanPaginationRequest(request.getPagination());
        String sort = paginationRequest.getSort();
        int page = paginationRequest.getPage();
        int size = paginationRequest.getSize();
        Optional<ResponseEntity<Map<String, Object>>> invalidMap = tryValidMarketPeriod(market, period, "Invalid request: " + request);
        if (invalidMap.isPresent()) return invalidMap.get();
        logger.info("Processing request: " + request);
        try {
            CotPaginatedResponse cotResponse = smaService.getCotResponseWithSma(period, market, page, size, sort);
            return ResponseEntity.ok(cotResponse.toMap());
        } catch (Exception e) {
            return unknownErrorResponse(e);
        }
    }

    @PostMapping(value = "/sma-all", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> getCotsWithAllSmas(
            @RequestBody PaginatedMarketRequest request
    ) {
        String market = request.getMarket();
        PaginationRequest paginationRequest = cleanPaginationRequest(request.getPagination());
        String sort = paginationRequest.getSort();
        int page = paginationRequest.getPage();
        int size = paginationRequest.getSize();
        Optional<ResponseEntity<Map<String, Object>>> invalidMap = tryValidMarket(market, "Invalid request: " + request);
        if (invalidMap.isPresent()) return invalidMap.get();
        logger.info("Processing request: " + request);
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

    private PaginationRequest cleanPaginationRequest(PaginationRequest req) {
        if (req == null) return new PaginationRequest();
        if (req.getPage() == null) req.setPage(0);
        if (req.getSize() == null) req.setSize(20);
        if (req.getSort() == null) req.setSort("desc");
        return req;
    }
}
