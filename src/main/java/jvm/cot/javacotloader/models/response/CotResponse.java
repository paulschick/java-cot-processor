package jvm.cot.javacotloader.models.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CotResponse {
    private Long id;
    private Date date;
    private String market;
    private int openInterest;
    private int nonCommLong;
    private int nonCommShort;
    private int nonCommNet;
    private int commLong;
    private int commShort;
    private int commNet;
    private int nonReptLong;
    private int nonReptShort;
    private int nonReptNet;
}
