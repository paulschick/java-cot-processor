package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.models.Cot;
import jvm.cot.javacotloader.repositories.CotRepository;
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
    private final CotRepository cotPagingRepository;

    @Autowired
    public CotPagingSortingService(CotRepository cotPagingRepository) {
        this.cotPagingRepository = cotPagingRepository;
    }

    public Page<Cot> getByPageSorted(int page, int size, String[] sort) {
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(parseSortArray(sort)));
        return cotPagingRepository.findAll(pagingSort);
    }

    private List<Order> parseSortArray(String[] sort) {
        List<Order> orders = new ArrayList<>();
        if (sort == null || sort.length == 0) {
            return orders;
        }
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] sortParams = sortOrder.split(",");
                orders.add(new Order(getSortDirection(sortParams[1]), sortParams[0]));
            }
        } else {
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
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
