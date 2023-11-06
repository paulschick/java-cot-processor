package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.models.Cot;
import jvm.cot.javacotloader.repositories.CotRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

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
            for (Row row : sheet) {
                if (rowIdx >= numberRows) break;
                if (rowIdx == 0) {
                    rowIdx++;
                    continue;
                }

                Cell marketCell = row.getCell(0);
                Cell reportDateCell = row.getCell(2);
                Date reportDate = reportDateCell.getDateCellValue();
                String reportDateString = reportDate.toString();

                Cell openIntCell = row.getCell(7);
                Cell nonCommLongCell = row.getCell(8);
                Cell nonCommShortCell = row.getCell(9);
                Cell commLongCell = row.getCell(11);
                Cell commShortCell = row.getCell(12);
                Cell nonReptLongCell = row.getCell(15);
                Cell nonReptShortCell = row.getCell(16);

                Cot cot = new Cot();
                cot.setMarket(cellToString(marketCell));
                cot.setDate(reportDateString);
                cot.setOpenInterest(cellToString(openIntCell));
                cot.setNonCommLong(cellToString(nonCommLongCell));
                cot.setNonCommShort(cellToString(nonCommShortCell));
                cot.setCommLong(cellToString(commLongCell));
                cot.setCommShort(cellToString(commShortCell));
                cot.setNonReptLong(cellToString(nonReptLongCell));
                cot.setNonReptShort(cellToString(nonReptShortCell));

                cotRepository.save(cot);
                logger.info("Saved " + cot);

                rowIdx++;
            }

        } catch (Exception e) {
            logger.error("Error reading Excel file: " + e.getMessage());
        }
    }

    private String cellToString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
