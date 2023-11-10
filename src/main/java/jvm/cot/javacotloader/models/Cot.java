package jvm.cot.javacotloader.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter @Setter @NoArgsConstructor
public class Cot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private Date date;
    @Column(name = "year")
    private int year;
    @Column(name = "market")
    private String market;
    @Column(name = "open_interest")
    private String openInterest;
    @Column(name = "non_commercial_long")
    private String nonCommLong;
    @Column(name = "non_commercial_short")
    private String nonCommShort;
    @Column(name = "comm_long")
    private String commLong;
    @Column(name = "comm_short")
    private String commShort;
    @Column(name = "non_rept_long")
    private String nonReptLong;
    @Column(name = "non_rept_short")
    private String nonReptShort;

    public Map<String, Object> toMap() {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", id);
        responseMap.put("date", date);
        responseMap.put("year", year);
        responseMap.put("market", market);
        responseMap.put("openInterest", openInterest);
        responseMap.put("nonCommLong", nonCommLong);
        responseMap.put("nonCommShort", nonCommShort);
        responseMap.put("commLong", commLong);
        responseMap.put("commShort", commShort);
        responseMap.put("nonReptLong", nonReptLong);
        responseMap.put("nonReptShort", nonReptShort);
        return responseMap;
    }
}
