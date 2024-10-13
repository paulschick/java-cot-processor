package jvm.cot.javacotloader.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class CftcResponse {
    @JsonProperty("report_date_as_yyyy_mm_dd")
    private String reportDate;
    @JsonProperty("market_and_exchange_names")
    private String market;
    @JsonProperty("cftc_contract_market_code")
    private String contractName;
    @JsonProperty("commodity_name")
    private String commodityName;
    @JsonProperty("commodity_subgroup_name")
    private String commoditySubgroup;
    @JsonProperty("commodity_group_name")
    private String commodityGroup;
    @JsonProperty("open_interest_all")
    private String openInterest;
    @JsonProperty("noncomm_positions_long_all")
    private String nonCommLong;
    @JsonProperty("noncomm_positions_short_all")
    private String nonCommShort;
    @JsonProperty("comm_positions_long_all")
    private String commLong;
    @JsonProperty("comm_positions_short_all")
    private String commShort;
    @JsonProperty("nonrept_positions_long_all")
    private String nonReptLong;
    @JsonProperty("nonrept_positions_short_all")
    private String nonReptShort;
}
