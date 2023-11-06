package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.mappers.XlsRowToCot;
import jvm.cot.javacotloader.models.Cot;
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
public class ExcelService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    private final CotRepository cotRepository;

    @Autowired
    public ExcelService(CotRepository cotRepository) {
        this.cotRepository = cotRepository;
    }

    public void testWriteFromExcelFile(String filePath, int numberRows) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            int rowIdx = 0;
            List<Cot> cots = new ArrayList<>();
            for (Row row : sheet) {
                if (rowIdx >= numberRows) break;
                if (rowIdx == 0) {
                    rowIdx++;
                    continue;
                }

                Cot cot = XlsRowToCot.rowToCot(row);
                cots.add(cot);
                rowIdx++;
            }
            cotRepository.saveAll(cots);
            logger.info("Saved " + rowIdx + " rows to database.");

        } catch (Exception e) {
            logger.error("Error reading Excel file: " + e.getMessage());
        }
    }
}
