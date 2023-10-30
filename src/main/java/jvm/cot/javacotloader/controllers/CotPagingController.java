package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.entities.Cot;
import jvm.cot.javacotloader.services.CotPagingSortingService;
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
@RequestMapping("/cot/paging")
public class CotPagingController {
    private final CotPagingSortingService cotPagingSortingService;

    @Autowired
    public CotPagingController(CotPagingSortingService cotPagingSortingService) {
        this.cotPagingSortingService = cotPagingSortingService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getByPageSort(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "date,desc;id") String sort
    ) {
        try {
            Map<String, Object> response = new HashMap<>();
            Page<Cot> cotPage = cotPagingSortingService.getByPageSorted(page, size, sort);
            List<Cot> cots = cotPage.getContent();
            response.put("cots", cots);
            response.put("currentPage", cotPage.getNumber());
            response.put("totalItems", cotPage.getTotalElements());
            response.put("totalPages", cotPage.getTotalPages());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred: " + e.getMessage() + "\n" + e);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
