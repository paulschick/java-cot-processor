package jvm.cot.javacotloader.services;

import jvm.cot.javacotloader.mappers.PaginationMapper;
import jvm.cot.javacotloader.models.entities.Cot;
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
        Pageable pagingSort = PaginationMapper.getPageRequest(page, size, sort);
        return cotPagingRepository.findAll(pagingSort);
    }
}
