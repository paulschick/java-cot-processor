package jvm.cot.javacotloader.repositories;

import jvm.cot.javacotloader.models.Cot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface CotRepository extends JpaRepository<Cot, Long> {
    @Query("SELECT c FROM Cot c WHERE c.year = :year")
    Collection<Cot> retrieveByYear(@Param("year") int year);
    @Query("SELECT c FROM Cot c WHERE c.market LIKE :market")
    Collection<Cot> retrieveByMarket(@Param("market") String market);
}
