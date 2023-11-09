package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.CalculatedValues;
import jvm.cot.javacotloader.models.Cot;
import jvm.cot.javacotloader.models.CotCalculatedDto;
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
            @RequestParam(defaultValue = "id,desc") String[] sort
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

    @GetMapping(value = "/calc-test", produces = "application/json")
    public ResponseEntity<Map<String, Object>> calcTest() {
        try {
            Map<String, Object> response = new HashMap<>();
            String[] sort = {"date", "desc"};
            Page<Cot> cotPage = cotPagingSortingService.getByPageSorted(0, 20, sort);
            List<CotCalculatedDto> cots = cotPage.getContent().stream().map(cot -> {
                CalculatedValues calculatedValues = new CalculatedValues();
                int commLongInt = stringToInt(cot.getCommLong());
                int commShortInt = stringToInt(cot.getCommShort());
                int commNet = commLongInt - commShortInt;
                calculatedValues.setNetComm(String.valueOf(commNet));
                int nonCommLongInt = stringToInt(cot.getNonCommLong());
                int nonCommShortInt = stringToInt(cot.getNonCommShort());
                int nonCommNet = nonCommLongInt - nonCommShortInt;
                calculatedValues.setNetNonComm(String.valueOf(nonCommNet));
                int nonReptLongInt = stringToInt(cot.getNonReptLong());
                int nonReptShortInt = stringToInt(cot.getNonReptShort());
                int nonReptNet = nonReptLongInt - nonReptShortInt;
                calculatedValues.setNetNonRept(String.valueOf(nonReptNet));

                return new CotCalculatedDto(cot, calculatedValues);
            }).toList();
            response.put("cots", cots);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred: " + e.getMessage() + "\n" + e);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private int stringToInt(String s) {
        return (int) Double.parseDouble(s);
    }
}
