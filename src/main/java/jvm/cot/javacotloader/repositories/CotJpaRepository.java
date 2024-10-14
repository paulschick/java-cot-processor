package jvm.cot.javacotloader.repositories;

import jvm.cot.javacotloader.models.entities.Cot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CotJpaRepository extends JpaRepository<Cot, Long> {
    boolean existsByMarketDate(String marketDate);
}
