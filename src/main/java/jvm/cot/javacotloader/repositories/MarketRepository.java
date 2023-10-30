package jvm.cot.javacotloader.repositories;

import jvm.cot.javacotloader.models.entities.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MarketRepository extends JpaRepository<Market, Long> {
    @Query("SELECT m FROM Market m WHERE m.market = :market")
    Market findByMarket(String market);
}
