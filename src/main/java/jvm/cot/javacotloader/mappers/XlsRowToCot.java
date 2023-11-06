package jvm.cot.javacotloader.mappers;

import jvm.cot.javacotloader.models.Cot;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Date;

public class XlsRowToCot {
    public static Cot rowToCot(Row row) {
        Cell marketCell = row.getCell(0);
        Cell reportDateCell = row.getCell(2);
        Date reportDate = reportDateCell.getDateCellValue();

        Cell openIntCell = row.getCell(7);
        Cell nonCommLongCell = row.getCell(8);
        Cell nonCommShortCell = row.getCell(9);
        Cell commLongCell = row.getCell(11);
        Cell commShortCell = row.getCell(12);
        Cell nonReptLongCell = row.getCell(15);
        Cell nonReptShortCell = row.getCell(16);

        Cot cot = new Cot();
        cot.setMarket(cellToString(marketCell));
        cot.setDate(reportDate);
        cot.setOpenInterest(cellToString(openIntCell));
        cot.setNonCommLong(cellToString(nonCommLongCell));
        cot.setNonCommShort(cellToString(nonCommShortCell));
        cot.setCommLong(cellToString(commLongCell));
        cot.setCommShort(cellToString(commShortCell));
        cot.setNonReptLong(cellToString(nonReptLongCell));
        cot.setNonReptShort(cellToString(nonReptShortCell));

        return cot;
    }

    private static String cellToString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
