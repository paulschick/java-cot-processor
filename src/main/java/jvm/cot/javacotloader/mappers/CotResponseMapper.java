package jvm.cot.javacotloader.mappers;

import jvm.cot.javacotloader.models.entities.Cot;
import jvm.cot.javacotloader.models.response.CotResponse;

public class CotResponseMapper {
    public static CotResponse cotToResponse(Cot cot) {
        CotResponse response = new CotResponse();
        response.setId(cot.getId());
        response.setDate(cot.getDate());
        response.setMarket(cot.getMarket());
        response.setOpenInterest(stringToInt(cot.getOpenInterest()));
        response.setNonCommLong(stringToInt(cot.getNonCommLong()));
        response.setNonCommShort(stringToInt(cot.getNonCommShort()));
        response.setNonCommNet(stringToInt(cot.getNonCommNet()));
        response.setCommLong(stringToInt(cot.getCommLong()));
        response.setCommShort(stringToInt(cot.getCommShort()));
        response.setCommNet(stringToInt(cot.getCommNet()));
        response.setNonReptLong(stringToInt(cot.getNonReptLong()));
        response.setNonReptShort(stringToInt(cot.getNonReptShort()));
        response.setNonReptNet(stringToInt(cot.getNonReptNet()));
        return response;
    }

    private static int stringToInt(String s) {
        return (int) Double.parseDouble(s);
    }
}
