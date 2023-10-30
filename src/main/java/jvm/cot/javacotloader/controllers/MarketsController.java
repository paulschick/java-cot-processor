package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.MarketsPaginatedResponse;
import jvm.cot.javacotloader.models.request.PaginationRequest;
import jvm.cot.javacotloader.services.MarketsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("markets")
public class MarketsController {
    private static final Logger logger = LoggerFactory.getLogger(MarketsController.class);
    private final MarketsService marketsService;
    @Autowired
    public MarketsController(MarketsService marketsService) {
        this.marketsService = marketsService;
    }
    @PostMapping(value = "/get-markets", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> getMarketsPaginated(
            @RequestBody PaginationRequest request
            ) {
        request = cleanPaginationRequest(request);
        logger.info("Processing request: " + request);
        int page = request.getPage();
        int size = request.getSize();
        String sort = request.getSort();
        try {
            MarketsPaginatedResponse response = marketsService.getMarketsResponse(page, size, sort);
            return ResponseEntity.ok(response.toMap());
        } catch (Exception e) {
            return unknownErrorResponse(e);
        }
    }

    @PostMapping(value = "/insert-all-markets", produces = "application/json")
    public ResponseEntity<Map<String, Object>> insertAllMarkets() {
        try {
            marketsService.insertAllMarkets();
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Successfully inserted all markets.");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            return unknownErrorResponse(e);
        }
    }

    // TODO - extract
    private ResponseEntity<Map<String, Object>> unknownErrorResponse(Exception e) {
        String message = "Unexpected error occurred: " + e.getMessage() + "\n" + e;
        logger.error(message);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);
        return ResponseEntity.badRequest().body(responseMap);
    }

    // TODO - extract
    private PaginationRequest cleanPaginationRequest(PaginationRequest req) {
        if (req == null) return new PaginationRequest();
        if (req.getPage() == null) req.setPage(0);
        if (req.getSize() == null) req.setSize(20);
        if (req.getSort() == null) req.setSort("desc");
        return req;
    }
}
