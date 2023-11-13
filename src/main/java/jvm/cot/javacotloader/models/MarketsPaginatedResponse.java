package jvm.cot.javacotloader.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MarketsPaginatedResponse {
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private List<MarketResponse> markets;
}
