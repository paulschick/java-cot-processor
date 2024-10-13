//package jvm.cot.javacotloader.controllers;
//
//import jvm.cot.javacotloader.services.CotProcessingService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Deprecated
////@RestController
////@RequestMapping("process")
//public class CotProcessController {
//    private static final Logger logger = LoggerFactory.getLogger(CotProcessController.class);
//    private final CotProcessingService cotProcessingService;
//
////    @Autowired
//    public CotProcessController(CotProcessingService cotProcessingService) {
//        this.cotProcessingService = cotProcessingService;
//    }
//
//    @GetMapping(value = "/{fileNo}", produces = "application/json")
//    public ResponseEntity<Map<String, Object>> writeAllRowsForFileNumber(@PathVariable int fileNo) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            String fileName = cotProcessingService.writeFileByIndex(fileNo);
//            String message = "Successfully processed file number " + fileNo + ": " + fileName;
//            logger.info(message);
//            response.put("message", message);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            String message = "Error processing file number " + fileNo + ": " + e.getMessage();
//            logger.error(message);
//            response.put("message", message);
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
////    @GetMapping(value = "/update/{year}", produces = "application/json")
////    public ResponseEntity<Map<String, Object>> updateForYear(@PathVariable int year) {
////        Map<String, Object> response = new HashMap<>();
////        int result = cotProcessingService.updateForFileYear(year);
////        response.put("count", result);
////        return ResponseEntity.ok(response);
////    }
//}
