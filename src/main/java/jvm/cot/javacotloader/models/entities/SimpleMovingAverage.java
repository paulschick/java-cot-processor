package jvm.cot.javacotloader.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class SimpleMovingAverage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cot_id")
    private Long cotId;
    @Column(name = "period")
    private int period;
    @Column(name = "value")
    private String value;
}
