package jvm.cot.javacotloader.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @NoArgsConstructor
public class CalculatedValues {
    private String netComm;
    private String netNonComm;
    private String netNonRept;
    private Map<Integer, String> simpleMovingAverages;
}
