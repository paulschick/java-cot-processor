package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.Cot;
import jvm.cot.javacotloader.services.CotCalculationsService;
import jvm.cot.javacotloader.services.CotPagingSortingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("calculations")
public class CotCalculationsController {
    private static final Logger logger = LoggerFactory.getLogger(CotCalculationsController.class);
    private final CotPagingSortingService pagingSortingService;
    private final CotCalculationsService cotCalculationsService;

    @Autowired
    public CotCalculationsController(CotPagingSortingService pagingSortingService, CotCalculationsService cotCalculationsService) {
        this.pagingSortingService = pagingSortingService;
        this.cotCalculationsService = cotCalculationsService;
    }

    @GetMapping(value = "/net", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getWithNetValues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date,desc;id") String sort
    ) {
        try {
            Page<Cot> cotPage = pagingSortingService.getByPageSorted(page, size, sort);
            Map<String, Object> responseMap = cotCalculationsService.pageToCalculationResponse(cotPage, true);
            logger.info("Retrieved " + responseMap.size() + " records");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            String msg = "Error occurred: " + e.getMessage() + "\n" + e;
            logger.error(msg);
            errorResponse.put("message", msg);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
