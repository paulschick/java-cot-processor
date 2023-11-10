package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.Cot;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("calculations")
public class CotCalculationsController {
    private static final Logger logger = LoggerFactory.getLogger(CotCalculationsController.class);
    private final CotPagingSortingService service;

    @Autowired
    public CotCalculationsController(CotPagingSortingService service) {
        this.service = service;
    }

    @GetMapping(value = "/net", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getWithNetValues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date,desc;id") String sort
    ) {
        try {
            Map<String, Object> response = new HashMap<>();
            Page<Cot> cotPage = service.getByPageSorted(page, size, sort);
            List<Map<String, Object>> responseMap = cotPage.getContent().stream().map(cot -> {
                int commLongInt = stringToInt(cot.getCommLong());
                int commShortInt = stringToInt(cot.getCommShort());
                int commNet = commLongInt - commShortInt;
                int nonCommLongInt = stringToInt(cot.getNonCommLong());
                int nonCommShortInt = stringToInt(cot.getNonCommShort());
                int nonCommNet = nonCommLongInt - nonCommShortInt;
                int nonReptLongInt = stringToInt(cot.getNonReptLong());
                int nonReptShortInt = stringToInt(cot.getNonReptShort());
                int nonReptNet = nonReptLongInt - nonReptShortInt;
                Map<String, Object> cotMap = cot.toMap();
                cotMap.replace("nonCommLong", nonCommLongInt);
                cotMap.replace("nonCommShort", nonCommShortInt);
                cotMap.replace("nonReptLong", nonReptLongInt);
                cotMap.replace("nonReptShort", nonReptShortInt);
                cotMap.replace("commLong", commLongInt);
                cotMap.replace("commShort", commShortInt);
                cotMap.replace("openInterest", stringToInt(cot.getOpenInterest()));
                cotMap.put("netComm", commNet);
                cotMap.put("netNonComm", nonCommNet);
                cotMap.put("netNonRept", nonReptNet);
                return cotMap;
            }).toList();
            response.put("cots", responseMap);
            response.put("currentPage", cotPage.getNumber());
            response.put("totalItems", cotPage.getTotalElements());
            response.put("totalPages", cotPage.getTotalPages());
            logger.info("Retrieved " + responseMap.size() + " records");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            String msg = "Error occurred: " + e.getMessage() + "\n" + e;
            logger.error(msg);
            errorResponse.put("message", msg);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private int stringToInt(String s) {
        return (int) Double.parseDouble(s);
    }
}
