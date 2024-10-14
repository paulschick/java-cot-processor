package jvm.cot.javacotloader.models;

import jvm.cot.javacotloader.models.entities.Cot;
import jvm.cot.javacotloader.models.response.CotResponse;

@FunctionalInterface
public interface CotResponseMapProvider {
    CotResponse map(Cot cot);
}
