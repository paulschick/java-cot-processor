package jvm.cot.javacotloader.models.entities;

import jakarta.persistence.*;
import jvm.cot.javacotloader.models.map.CotMapper;
import jvm.cot.javacotloader.models.map.NumericCotDataSource;
import jvm.cot.javacotloader.models.response.CotResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cot implements NumericCotDataSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private String date;
    @Column(name = "market")
    private String market;
    @Column(name = "market_date")
    private String marketDate;
    @Column(name = "contract_name")
    private String contractName;
    @Column(name = "commodity_name")
    private String commodityName;
    @Column(name = "commodity_subgroup")
    private String commoditySubgroup;
    @Column(name = "commodity_group")
    private String commodityGroup;
    @Column(name = "open_interest")
    private String openInterest;
    @Column(name = "non_commercial_long")
    private String nonCommLong;
    @Column(name = "non_commercial_short")
    private String nonCommShort;
    @Column(name = "non_commercial_net")
    private String nonCommNet;
    @Column(name = "comm_long")
    private String commLong;
    @Column(name = "comm_short")
    private String commShort;
    @Column(name = "comm_net")
    private String commNet;
    @Column(name = "non_rept_long")
    private String nonReptLong;
    @Column(name = "non_rept_short")
    private String nonReptShort;
    @Column(name = "non_rept_net")
    private String nonReptNet;

    public CotResponse mapToCotResponse() {
        var cotResponse = new CotResponse();
        cotResponse.setId(getId());
        cotResponse.setDate(getDate());
        cotResponse.setMarket(getMarket());
        cotResponse.setContractName(getContractName());
        cotResponse.setCommodityName(getCommodityName());
        cotResponse.setCommoditySubgroup(getCommoditySubgroup());
        cotResponse.setCommodityGroup(getCommodityGroup());
        CotMapper.mapNumericFields(cotResponse, this);
        return cotResponse;
    }
}
