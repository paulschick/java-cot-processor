package jvm.cot.javacotloader.repositories;

import jvm.cot.javacotloader.models.Cot;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CotPagingRepository extends PagingAndSortingRepository<Cot, Long> {
}
