package jvm.cot.javacotloader.repositories;

import jvm.cot.javacotloader.models.entities.Cot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface CotRepository extends JpaRepository<Cot, Long> {
    @Query("SELECT c FROM Cot c WHERE c.year = :year")
    Collection<Cot> retrieveByYear(@Param("year") int year);
    @Query("SELECT c FROM Cot c WHERE c.market LIKE :market")
    Collection<Cot> retrieveByMarket(@Param("market") String market);
    @Query("SELECT c FROM Cot c WHERE c.market LIKE :market")
    Page<Cot> retrieveByMarketPageable(@Param("market") String market, Pageable pageable);
}
