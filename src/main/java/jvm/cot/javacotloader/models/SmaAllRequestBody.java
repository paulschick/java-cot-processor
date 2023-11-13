package jvm.cot.javacotloader.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmaAllRequestBody {
    @Schema(name = "market", type = "String", example = "S&P 500 ANNUAL DIVIDEND INDEX - CHICAGO MERCANTILE EXCHANGE")
    @JsonProperty("market")
    private String market;
    @Schema(name = "page", type = "number", example = "0")
    @JsonProperty("page")
    private Integer page;
    @Schema(name = "size", type = "number", example = "20")
    @JsonProperty("size")
    private Integer size;
    @Schema(name = "sort", type = "String", example = "date,desc;id")
    @JsonProperty("sort")
    private String sort;
}
