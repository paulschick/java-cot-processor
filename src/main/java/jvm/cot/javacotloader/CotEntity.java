package jvm.cot.javacotloader;

import jakarta.persistence.*;

@Entity
@Table(name = "cot_data")
public class CotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private String date;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpenInterest() {
        return openInterest;
    }

    public void setOpenInterest(String openInterest) {
        this.openInterest = openInterest;
    }

    public String getNonCommLong() {
        return nonCommLong;
    }

    public void setNonCommLong(String nonCommLong) {
        this.nonCommLong = nonCommLong;
    }

    public String getNonCommShort() {
        return nonCommShort;
    }

    public void setNonCommShort(String nonCommShort) {
        this.nonCommShort = nonCommShort;
    }

    public String getCommLong() {
        return commLong;
    }

    public void setCommLong(String commLong) {
        this.commLong = commLong;
    }

    public String getCommShort() {
        return commShort;
    }

    public void setCommShort(String commShort) {
        this.commShort = commShort;
    }

    public String getNonReptLong() {
        return nonReptLong;
    }

    public void setNonReptLong(String nonReptLong) {
        this.nonReptLong = nonReptLong;
    }

    public String getNonReptShort() {
        return nonReptShort;
    }

    public void setNonReptShort(String nonReptShort) {
        this.nonReptShort = nonReptShort;
    }
}
