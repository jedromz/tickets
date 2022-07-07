package pl.kurs.tickets.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "UC_OFFENSE_DICT_NAME")})
@EqualsAndHashCode(exclude = {"id"})
public class OffenseDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @SequenceGenerator(name = "hibernateSequence")
    private Long id;
    private String name;
    private Integer points;
    private BigDecimal minCharge;
    private BigDecimal maxCharge;
    private boolean deleted;
    @Version
    private int version;

    public OffenseDictionary(String name, Integer points, BigDecimal minCharge, BigDecimal maxCharge) {
        this.name = name;
        this.points = points;
        this.minCharge = minCharge;
        this.maxCharge = maxCharge;
    }
}
