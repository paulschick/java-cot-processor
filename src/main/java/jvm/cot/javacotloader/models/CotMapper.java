package jvm.cot.javacotloader.models;

import jvm.cot.javacotloader.models.entities.Cot;
import jvm.cot.javacotloader.models.response.CftcResponse;
import jvm.cot.javacotloader.models.response.CotResponse;
import jvm.cot.javacotloader.services.CotClient;

import java.text.SimpleDateFormat;

public class CotMapper {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat API_DATE_FORMAT = new SimpleDateFormat(CotClient.DATE_FMT);

    public static CotResponseMapProvider getResponseMapper() {
        return (Cot cot) -> {
            var cotResponse = new CotResponse();
            cotResponse.setId(cot.getId());
            cotResponse.setDate(cot.getDate());
            cotResponse.setMarket(cot.getMarket());
            cotResponse.setContractName(cot.getContractName());
            cotResponse.setCommodityName(cot.getCommodityName());
            cotResponse.setCommoditySubgroup(cot.getCommoditySubgroup());
            cotResponse.setCommodityGroup(cot.getCommodityGroup());
            cotResponse.setOpenInterest(Integer.parseInt(cot.getOpenInterest()));
            cotResponse.setNonCommLong(Integer.parseInt(cot.getNonCommLong()));
            cotResponse.setNonCommShort(Integer.parseInt(cot.getNonCommShort()));
            cotResponse.setNonCommNet(cotResponse.getNonCommLong() - cotResponse.getNonCommShort());
            cotResponse.setCommLong(Integer.parseInt(cot.getCommLong()));
            cotResponse.setCommShort(Integer.parseInt(cot.getCommShort()));
            cotResponse.setCommNet(cotResponse.getCommLong() - cotResponse.getCommShort());
            cotResponse.setNonReptLong(Integer.parseInt(cot.getNonReptLong()));
            cotResponse.setNonReptShort(Integer.parseInt(cot.getNonReptShort()));
            cotResponse.setNonReptNet(cotResponse.getNonReptLong() - cotResponse.getNonReptShort());
            return cotResponse;
        };
    }

    public static CotEntityMapProvider getEntityMapper() {
        return (CftcResponse cftcResponse) -> {
            try {
                var cot = new Cot();
                var date = API_DATE_FORMAT.parse(cftcResponse.getReportDate());
                var dateFmt = DATE_FORMAT.format(date);
                cot.setDate(dateFmt);
                cot.setMarket(cftcResponse.getMarket());
                cot.setMarketDate(cot.getMarket() + " " + dateFmt);
                cot.setContractName(cftcResponse.getContractName());
                cot.setCommodityName(cftcResponse.getCommodityName());
                cot.setCommoditySubgroup(cftcResponse.getCommoditySubgroup());
                cot.setCommodityGroup(cftcResponse.getCommodityGroup());
                cot.setOpenInterest(cftcResponse.getOpenInterest());
                cot.setNonCommLong(cftcResponse.getNonCommLong());
                cot.setNonCommShort(cftcResponse.getNonCommShort());
                cot.setNonCommNet(getNetString(cot.getNonCommLong(), cot.getNonCommShort()));
                cot.setCommLong(cftcResponse.getCommLong());
                cot.setCommShort(cftcResponse.getCommShort());
                cot.setCommNet(getNetString(cot.getCommLong(), cot.getCommShort()));
                cot.setNonReptLong(cftcResponse.getNonReptLong());
                cot.setNonReptShort(cftcResponse.getNonReptShort());
                cot.setNonReptNet(getNetString(cot.getNonReptLong(), cot.getNonReptShort()));

                return cot;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static String getNetString(String longValue, String shortValue) throws NumberFormatException {
        return String.valueOf(Integer.parseInt(longValue) - Integer.parseInt(shortValue));
    }
}
