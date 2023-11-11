package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.models.Cot;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CotCalculationsService {
    public Map<String, Object> cotToMap(Cot cot) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", cot.getId());
        responseMap.put("date", cot.getDate());
        responseMap.put("year", cot.getYear());
        responseMap.put("market", cot.getMarket());
        responseMap.put("openInterest", stringToInt(cot.getOpenInterest()));
        responseMap.put("nonCommLong", stringToInt(cot.getNonCommLong()));
        responseMap.put("nonCommShort", stringToInt(cot.getNonCommShort()));
        responseMap.put("commLong", stringToInt(cot.getCommLong()));
        responseMap.put("commShort", stringToInt(cot.getCommShort()));
        responseMap.put("nonReptLong", stringToInt(cot.getNonReptLong()));
        responseMap.put("nonReptShort", stringToInt(cot.getNonReptShort()));

        return responseMap;
    }

    public Map<String, Object> addNetValues(Map<String, Object> cotMap, Cot cot) {
        int commNet = getNet(cot.getCommLong(), cot.getCommShort());
        int nonCommNet = getNet(cot.getNonCommLong(), cot.getNonCommShort());
        int nonReptNet = getNet(cot.getNonReptLong(), cot.getNonReptShort());
        cotMap.put("netComm", commNet);
        cotMap.put("netNonComm", nonCommNet);
        cotMap.put("netNonRept", nonReptNet);
        return cotMap;
    }

    public Map<String, Object> cotToNetMap(Cot cot) {
        Map<String, Object> map = cotToMap(cot);
        return addNetValues(map, cot);
    }

    public Map<String, Object> pageToCalculationResponse(Page<Cot> cotPage, boolean net) {
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", cotPage.getNumber());
        response.put("totalItems", cotPage.getTotalElements());
        response.put("totalPages", cotPage.getTotalPages());
        // TODO - update with additional calculation options
        response.put("cots", cotPage.getContent().stream()
                .map(net ? this::cotToNetMap : this::cotToMap).toList());
        return response;
    }

    public int getNet(String longString, String shortString) {
        return stringToInt(longString) - stringToInt(shortString);
    }

    public int stringToInt(String s) {
        return (int) Double.parseDouble(s);
    }
}
