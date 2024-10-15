package jvm.cot.javacotloader.models.map;

import jvm.cot.javacotloader.models.entities.CotCsv;
import jvm.cot.javacotloader.models.entities.Cot;

@FunctionalInterface
public interface CotCsvMapProvider {
    Cot map(CotCsv cotCsv) throws RuntimeException;
}
