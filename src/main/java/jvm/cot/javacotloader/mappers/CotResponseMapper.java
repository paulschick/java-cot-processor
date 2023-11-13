package jvm.cot.javacotloader.mappers;

import jvm.cot.javacotloader.models.Cot;
import jvm.cot.javacotloader.models.CotResponse;

import java.util.HashMap;

public class CotResponseMapper {
    public static CotResponse cotToResponse(Cot cot) {
        CotResponse response = new CotResponse();
        response.setId(cot.getId());
        response.setDate(cot.getDate());
        response.setYear(cot.getYear());
        response.setMarket(cot.getMarket());
        response.setOpenInterest(stringToInt(cot.getOpenInterest()));
        response.setNonCommLong(stringToInt(cot.getNonCommLong()));
        response.setNonCommShort(stringToInt(cot.getNonCommShort()));
        response.setCommLong(stringToInt(cot.getCommLong()));
        response.setCommShort(stringToInt(cot.getCommShort()));
        response.setNonReptLong(stringToInt(cot.getNonReptLong()));
        response.setNonReptShort(stringToInt(cot.getNonReptShort()));
        response.setCalculatedFields(new HashMap<>());
        return response;
    }

    public static CotResponse cotToResponseWithNet(Cot cot) {
        CotResponse response = cotToResponse(cot);
        int netComm = response.getCommLong() - response.getCommShort();
        int netNonComm = response.getNonCommLong() - response.getNonCommShort();
        int netNonRept = response.getNonReptLong() - response.getNonReptShort();
        response.getCalculatedFields().put("netComm", netComm);
        response.getCalculatedFields().put("netNonComm", netNonComm);
        response.getCalculatedFields().put("netNonRept", netNonRept);
        return response;
    }

    private static int stringToInt(String s) {
        return (int) Double.parseDouble(s);
    }
}
