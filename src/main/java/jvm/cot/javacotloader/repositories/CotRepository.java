package jvm.cot.javacotloader.repositories;

import jvm.cot.javacotloader.models.entities.Cot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;

public interface CotRepository extends Repository<Cot, Long> {
    @Query("SELECT c FROM Cot c")
    Collection<Cot> get();
}
