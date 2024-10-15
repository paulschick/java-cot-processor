package jvm.cot.javacotloader.models.map;

import jvm.cot.javacotloader.models.entities.Cot;
import jvm.cot.javacotloader.models.response.CftcResponse;

@FunctionalInterface
public interface CotEntityMapProvider {
    Cot map(CftcResponse cftcResponse) throws RuntimeException;
}
