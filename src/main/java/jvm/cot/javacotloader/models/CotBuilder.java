//package jvm.cot.javacotloader.models;
//
//import jvm.cot.javacotloader.mappers.CotResponseMapper;
//import jvm.cot.javacotloader.models.entities.Cot;
//import lombok.Getter;
//import org.springframework.data.domain.Page;
//
//import java.util.List;
//
//@Getter
//public class CotBuilder {
//    private final Page<Cot> cotPage;
//    private final CotPaginatedResponse cotPaginatedResponse;
//    private boolean withNetValues;
//
//    public CotBuilder(Page<Cot> cotPage) {
//        this.cotPage = cotPage;
//        this.withNetValues = true;
//        this.cotPaginatedResponse = new CotPaginatedResponse();
//        setPageValues();
//    }
//
//    public CotBuilder withNetValues(boolean withNetValues) {
//        this.withNetValues = withNetValues;
//        return this;
//    }
//
//    public CotPaginatedResponse build() {
//        setCotResponseList();
//        return this.cotPaginatedResponse;
//    }
//
//    private void setPageValues() {
//        this.cotPaginatedResponse.setCurrentPage(this.cotPage.getNumber());
//        this.cotPaginatedResponse.setTotalItems(this.cotPage.getTotalElements());
//        this.cotPaginatedResponse.setTotalPages(this.cotPage.getTotalPages());
//    }
//
//    private void setCotResponseList() {
//        List<Cot> cotList = this.cotPage.getContent();
//        List<CotResponse> cotResponseList = cotList.stream()
//                .map(cot -> {
//                    if (withNetValues) {
//                        return CotResponseMapper.cotToResponseWithNet(cot);
//                    }
//                    return CotResponseMapper.cotToResponse(cot);
//                })
//                .toList();
//        cotPaginatedResponse.setCots(cotResponseList);
//    }
//}
