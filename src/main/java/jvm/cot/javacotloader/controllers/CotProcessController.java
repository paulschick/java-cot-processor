package jvm.cot.javacotloader.controllers;

import jvm.cot.javacotloader.models.MessageResponse;
import jvm.cot.javacotloader.services.ExcelService;
import jvm.cot.javacotloader.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("process")
public class CotProcessController {
    private static final Logger logger = LoggerFactory.getLogger(CotProcessController.class);
    private final ExcelService excelService;
    private final FileService fileService;

    @Autowired
    public CotProcessController(ExcelService excelService, FileService fileService) {
        this.excelService = excelService;
        this.fileService = fileService;
    }

    @GetMapping(value = "/{fileNo}", produces = "application/json")
    public ResponseEntity<MessageResponse> writeAllRowsForFileNumber(@PathVariable int fileNo) {
        File unzipDir = new File(fileService.getUnzipCotDir());
        if (!unzipDir.exists()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Unzip directory does not exist."));
        }
        File[] unzipChildren = unzipDir.listFiles();
        if (unzipChildren == null || unzipChildren.length == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("No files found."));
        }
        if (fileNo < 0 || fileNo >= unzipChildren.length) {
            return ResponseEntity.badRequest().body(new MessageResponse("File number " + fileNo + " is out of range."));
        }
        File unzipChild = unzipChildren[fileNo];
        logger.info("Processing " + unzipChild.getName());
        if (unzipChild.isDirectory()) {
            logger.info("Entering directory " + unzipChild.getName());
            File[] files = unzipChild.listFiles();
            if (files == null || files.length == 0) {
                logger.info("Skipping directory " + unzipChild.getName() + " because it is empty.");
                return ResponseEntity.badRequest().body(new MessageResponse("Skipping directory " + unzipChild.getName() + " because it is empty."));
            }
            for (File file : files) {
                logger.info("Processing file " + file.getName());
                if (file.getName().endsWith(".xls")) {
                    logger.info("Process XLS File test for " + file.getName());
                    excelService.writeAllRows(file.getAbsolutePath());
                }
            }
        }
        return ResponseEntity.ok(new MessageResponse("success"));
    }
}
