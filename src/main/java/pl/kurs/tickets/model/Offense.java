package pl.kurs.tickets.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class Offense {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @SequenceGenerator(name = "hibernateSequence")
    private Long id;
    private BigDecimal charge;
    private boolean deleted;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "offense_dictionary_id")
    private OffenseDictionary offenseDictionary;
    @Version
    private int version;

    public Offense(BigDecimal charge) {
        this.charge = charge;
    }
}

