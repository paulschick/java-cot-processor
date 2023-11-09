package jvm.cot.javacotloader.models;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

@Getter
public class XlsRow {
    private static final Logger logger = LoggerFactory.getLogger(XlsRow.class);

    private final Row row;
    private final String market;
    private final Date reportDate;
    private final String openInterest;
    private final String nonCommLong;
    private final String nonCommShort;
    private final String commLong;
    private final String commShort;
    private final String nonReptLong;
    private final String nonReptShort;
    public XlsRow(Row row) {
        try {
            this.row = row;
            this.market = cellToString(row.getCell(0));
            this.reportDate = row.getCell(2).getDateCellValue();
            this.openInterest = cellToString(row.getCell(7));
            this.nonCommLong = cellToString(row.getCell(8));
            this.nonCommShort = cellToString(row.getCell(9));
            this.commLong = cellToString(row.getCell(11));
            this.commShort = cellToString(row.getCell(12));
            this.nonReptLong = cellToString(row.getCell(15));
            this.nonReptShort = cellToString(row.getCell(16));
        } catch (Exception e) {
            logger.error("Error parsing row: {}", row, e);
            throw e;
        }
    }

    public Cot build() {
        Cot cot = new Cot();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reportDate);
        cot.setMarket(market);
        cot.setDate(reportDate);
        cot.setYear(calendar.get(Calendar.YEAR));
        cot.setOpenInterest(openInterest);
        cot.setNonCommLong(nonCommLong);
        cot.setNonCommShort(nonCommShort);
        cot.setCommLong(commLong);
        cot.setCommShort(commShort);
        cot.setNonReptLong(nonReptLong);
        cot.setNonReptShort(nonReptShort);
        return cot;
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
