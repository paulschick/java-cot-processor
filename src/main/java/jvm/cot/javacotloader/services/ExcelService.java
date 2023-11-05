package jvm.cot.javacotloader.services;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;

@Service
public class ExcelService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);
    public void readExcelFile(String filePath, int numberRows) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = new HSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            int rowIdx = 0;
            for (Row row : sheet) {
                if (rowIdx >= numberRows) break;

                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING -> logger.info(cell.getStringCellValue() + "\t");
                        case NUMERIC -> logger.info(cell.getNumericCellValue() + "\t");
                        case BOOLEAN -> logger.info(cell.getBooleanCellValue() + "\t");
                        case FORMULA -> logger.info(cell.getCellFormula() + "\t");
                        default -> logger.info(" - \t");
                    }
                }
                System.out.println();
                rowIdx++;
            }

        } catch (Exception e) {
            logger.error("Error reading Excel file: " + e.getMessage());
        }
    }
}
