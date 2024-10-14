package jvm.cot.javacotloader.models;

import jvm.cot.javacotloader.models.entities.Cot;
import jvm.cot.javacotloader.models.response.CftcResponse;

import java.text.ParseException;

@FunctionalInterface
public interface CotEntityMapProvider {
    Cot map(CftcResponse cftcResponse) throws ParseException, IllegalArgumentException;
}
