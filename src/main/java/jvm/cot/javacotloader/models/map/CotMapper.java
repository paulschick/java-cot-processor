package jvm.cot.javacotloader.models.map;

import jvm.cot.javacotloader.models.entities.CotCsv;
import jvm.cot.javacotloader.models.entities.Cot;
import jvm.cot.javacotloader.models.response.CftcResponse;
import jvm.cot.javacotloader.models.response.CotResponse;
import jvm.cot.javacotloader.services.CotClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CotMapper {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern(CotClient.DATE_FMT);
    public static final DateTimeFormatter CSV_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    public static CotResponseMapProvider getResponseMapper() {
        return Cot::mapToCotResponse;
    }

    public static CotEntityMapProvider getEntityMapper() {
        return (CftcResponse cftcResponse) -> {
            try {
                return cftcResponse.mapToCot();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static CotCsvMapProvider getCsvMapper() {
        return (CotCsv cotCsv) -> {
            try {
                return cotCsv.mapToCot();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static void mapNumericFields(Cot cot, NumericCotDataSource source) {
        cot.setOpenInterest(source.getOpenInterest());
        cot.setNonCommLong(source.getNonCommLong());
        cot.setNonCommShort(source.getNonCommShort());
        cot.setNonCommNet(getNetString(cot.getNonCommLong(), cot.getNonCommShort()));
        cot.setCommLong(source.getCommLong());
        cot.setCommShort(source.getCommShort());
        cot.setCommNet(getNetString(cot.getCommLong(), cot.getCommShort()));
        cot.setNonReptLong(source.getNonReptLong());
        cot.setNonReptShort(source.getNonReptShort());
        cot.setNonReptNet(getNetString(cot.getNonReptLong(), cot.getNonReptShort()));
    }

    public static void mapNumericFields(CotResponse cot, NumericCotDataSource source) {
        cot.setOpenInterest(parseInteger(source.getOpenInterest()));
        cot.setNonCommLong(parseInteger(source.getNonCommLong()));
        cot.setNonCommShort(parseInteger(source.getNonCommShort()));
        cot.setNonCommNet(cot.getNonCommLong() - cot.getNonCommShort());
        cot.setCommLong(parseInteger(source.getCommLong()));
        cot.setCommShort(parseInteger(source.getCommShort()));
        cot.setCommNet(cot.getCommLong() - cot.getCommShort());
        cot.setNonReptLong(parseInteger(source.getNonReptLong()));
        cot.setNonReptShort(parseInteger(source.getNonReptShort()));
        cot.setNonReptNet(cot.getNonReptLong() - cot.getNonReptShort());
    }

    public static String reFormatDate(String date, DateTimeFormatter formatter) {
        return LocalDate.parse(date, formatter).format(DATE_FORMAT);
    }

    private static int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid integer value: " + value, e);
        }
    }

    private static String getNetString(String longValue, String shortValue) {
        return String.valueOf(parseInteger(longValue) - parseInteger(shortValue));
    }
}
