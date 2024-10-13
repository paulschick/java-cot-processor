package jvm.cot.javacotloader.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestCotResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CftcResponse> cots;

    public TestCotResponse(List<CftcResponse> cots) {
        this.cots = cots;
    }

    public TestCotResponse(String error) {
        this.cots = null;
        this.error = error;
    }
}
