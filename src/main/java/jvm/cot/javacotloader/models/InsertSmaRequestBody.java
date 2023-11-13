package jvm.cot.javacotloader.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InsertSmaRequestBody {
    @Schema(name = "market", type = "string", example = "S&P 500 ANNUAL DIVIDEND INDEX - CHICAGO MERCANTILE EXCHANGE")
    @JsonProperty("market")
    private String market;
    @Schema(name = "period", type = "number", example = "20")
    @JsonProperty("period")
    private Integer period;
}
