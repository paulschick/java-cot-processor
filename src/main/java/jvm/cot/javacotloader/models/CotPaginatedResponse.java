package jvm.cot.javacotloader.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CotPaginatedResponse {
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private Iterable<CotResponse> cots;
}
