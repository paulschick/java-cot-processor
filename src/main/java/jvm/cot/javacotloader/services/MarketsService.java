package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.mappers.PaginationMapper;
import jvm.cot.javacotloader.models.MarketResponse;
import jvm.cot.javacotloader.models.MarketsPaginatedResponse;
import jvm.cot.javacotloader.models.entities.Market;
import jvm.cot.javacotloader.repositories.MarketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketsService {
    private static final Logger logger = LoggerFactory.getLogger(MarketsService.class);
    private final MarketRepository marketRepository;
    @Autowired
    public MarketsService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }
    public MarketsPaginatedResponse getMarketsResponse(int page, int size, String sort) {
        try {
            Pageable pagingSort = PaginationMapper.getPageRequest(page, size, sort);
            Page<Market> marketPage = marketRepository.findAll(pagingSort);
            List<MarketResponse> markets = new ArrayList<>();
            for (Market market : marketPage.getContent()) {
                MarketResponse res = new MarketResponse();
                res.setId(market.getId());
                res.setMarket(market.getMarket());
                markets.add(res);
            }
            MarketsPaginatedResponse response = new MarketsPaginatedResponse();
            response.setCurrentPage(marketPage.getNumber());
            response.setTotalPages(marketPage.getTotalPages());
            response.setTotalItems(marketPage.getTotalElements());
            response.setMarkets(markets);
            logger.info("Retrieved " + markets.size() + " markets from the database. (page=" + response.getCurrentPage() +
                    ", size=" + response.getTotalItems() + ")");
            return response;
        } catch (Exception e) {
            logger.error("Error retrieving markets from the database for page " + page + ": " + e);
            throw e;
        }
    }
}
