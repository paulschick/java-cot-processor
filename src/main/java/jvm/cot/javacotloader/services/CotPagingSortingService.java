package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.models.Cot;
import jvm.cot.javacotloader.repositories.CotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CotPagingSortingService {
    private static final Logger logger = LoggerFactory.getLogger(CotPagingSortingService.class);
    private final CotRepository cotPagingRepository;

    @Autowired
    public CotPagingSortingService(CotRepository cotPagingRepository) {
        this.cotPagingRepository = cotPagingRepository;
    }

    public Page<Cot> getByPageSorted(int page, int size, String sort) {
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(parseSortArray(sort)));
        return cotPagingRepository.findAll(pagingSort);
    }

    private List<Order> parseSortArray(String sort) {
        List<Order> orders = new ArrayList<>();
        if (sort == null || sort.isEmpty()) {
            return orders;
        }
        logger.info("sort: " + sort);
        String[] pairs = sort.split(";");
        for (String pair : pairs) {
            String[] elements = pair.split(",");
            String property = elements[0];
            String direction = elements.length > 1 ? elements[1] : "asc";
            orders.add(new Order(getSortDirection(direction), property));
        }
        return orders;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        }
        return Sort.Direction.DESC;
    }
}
