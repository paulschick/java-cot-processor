package jvm.cot.javacotloader;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CotDataRepository extends JpaRepository<CotEntity, Long> {
}
