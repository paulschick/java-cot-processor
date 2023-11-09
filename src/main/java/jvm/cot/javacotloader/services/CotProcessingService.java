package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.models.Cot;
import jvm.cot.javacotloader.models.XlsRow;
import jvm.cot.javacotloader.repositories.CotRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CotProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(CotProcessingService.class);
    private final FileService fileService;
    private final CotRepository cotRepository;

    @Autowired
    public CotProcessingService(FileService fileService, CotRepository cotRepository) {
        this.fileService = fileService;
        this.cotRepository = cotRepository;
    }

    public void writeFileByIndex(int fileIndex) throws RuntimeException {
        File xlsFile = getFile(fileIndex);
        if (xlsFile.isDirectory()) {
            logger.error("File number " + fileIndex + " is a directory.");
            throw new RuntimeException("File number " + fileIndex + " is a directory.");
        }
        if (!xlsFile.getName().endsWith(".xls")) {
            logger.error("File number " + fileIndex + " is not an XLS file.");
            throw new RuntimeException("File number " + fileIndex + " is not an XLS file.");
        }
        writeAllRows(xlsFile.getAbsolutePath());
        logger.info("Saved " + xlsFile.getName() + " to database.");
    }

    private File getFile(int fileIndex) {
        File unzipDir = new File(fileService.getUnzipCotDir());
        if (!unzipDir.exists()) {
            throw new RuntimeException("Unzip directory does not exist.");
        }
        File[] unzipChildren = unzipDir.listFiles();
        if (unzipChildren == null || unzipChildren.length == 0) {
            throw new RuntimeException("No files found.");
        }
        if (fileIndex < 0 || fileIndex >= unzipChildren.length) {
            throw new RuntimeException("File number " + fileIndex + " is out of range.");
        }
        return unzipChildren[fileIndex];
    }

    public void writeAllRows(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            int rowIdx = 0;
            List<Cot> cots = new ArrayList<>();
            for (Row row : sheet) {
                if (rowIdx == 0) {
                    rowIdx++;
                    continue;
                }

                try {
                    XlsRow xlsRow = new XlsRow(row);
                    Cot cot = xlsRow.build();
                    cots.add(cot);
                } catch (Exception e) {
                    logger.error("Error parsing row: {}", row, e);
                    logger.warn("Skipping row " + rowIdx + " due to error.");
                }

                rowIdx++;
            }
            cotRepository.saveAll(cots);
            int rowCount = rowIdx + 1;
            logger.info("Saved " + rowCount + " rows to database.");
        } catch (Exception e) {
            logger.error("Error reading Excel file: " + e.getMessage());
        }
    }
}
