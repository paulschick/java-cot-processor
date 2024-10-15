package jvm.cot.javacotloader.models.entities;

import com.opencsv.bean.CsvBindByPosition;
import jvm.cot.javacotloader.models.map.CotMapper;
import jvm.cot.javacotloader.models.map.NumericCotDataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CotCsv implements NumericCotDataSource {
    @CsvBindByPosition(position = 1)
    private String market;
    @CsvBindByPosition(position = 2)
    private String date;
    @CsvBindByPosition(position = 4)
    private String contractName;
    @CsvBindByPosition(position = 9)
    private String commodityName;
    @CsvBindByPosition(position = 130)
    private String commoditySubgroup;
    @CsvBindByPosition(position = 131)
    private String commodityGroup;
    @CsvBindByPosition(position = 10)
    private String openInterest;
    @CsvBindByPosition(position = 11)
    private String nonCommLong;
    @CsvBindByPosition(position = 12)
    private String nonCommShort;
    @CsvBindByPosition(position = 14)
    private String commLong;
    @CsvBindByPosition(position = 15)
    private String commShort;
    @CsvBindByPosition(position = 18)
    private String nonReptLong;
    @CsvBindByPosition(position = 19)
    private String nonReptShort;

    public Cot mapToCot() {
        var cot = new Cot();
        var dateStr = CotMapper.reFormatDate(getDate(), CotMapper.CSV_DATE_FORMAT);
        cot.setDate(dateStr);
        cot.setMarket(getMarket());
        cot.setMarketDate(cot.getMarket() + " " + dateStr);
        cot.setContractName(getContractName());
        cot.setCommodityName(getCommodityName());
        cot.setCommoditySubgroup(getCommoditySubgroup());
        cot.setCommodityGroup(getCommodityGroup());
        CotMapper.mapNumericFields(cot, this);
        return cot;
    }
}
