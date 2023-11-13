package jvm.cot.javacotloader.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor
public class CotPaginatedResponse {
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private List<CotResponse> cots;

    public Map<String, Object> toMap() {
        return Map.of(
                "currentPage", this.currentPage,
                "totalItems", this.totalItems,
                "totalPages", this.totalPages,
                "cots", this.cots
        );
    }
}
