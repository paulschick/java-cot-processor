package jvm.cot.javacotloader.repositories;

import jvm.cot.javacotloader.models.SimpleMovingAverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface SmaRepository extends JpaRepository<SimpleMovingAverage, Long> {
    @Query("SELECT s FROM SimpleMovingAverage s WHERE s.cotId = :cotId AND s.period = :period")
    SimpleMovingAverage findByCotIdAndPeriod(@Param("cotId") Long cotId, @Param("period") int period);

    @Query("SELECT s FROM SimpleMovingAverage s WHERE s.cotId = :cotId")
    SimpleMovingAverage findByCotId(@Param("cotId") Long cotId);
}
