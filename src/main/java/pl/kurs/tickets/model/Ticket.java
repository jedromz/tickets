package pl.kurs.tickets.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//nr pesel ukranego, date, przewinienie, ilosc punktow, ilosc pln.
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @SequenceGenerator(name = "hibernateSequence")
    private Long id;
    private LocalDate date;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> offenses = new ArrayList<>();
    private Integer points;
    private BigDecimal charge;
    private boolean deleted;
    @Version
    private int version;
    @ManyToOne()
    @JoinColumn(name = "person_id")

    private Person person;

    public Ticket(LocalDate date, Integer points, BigDecimal charge) {
        this.date = date;
        this.points = points;
        this.charge = charge;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "date=" + date +
                ", offenses=" + offenses +
                ", points=" + points +
                ", charge=" + charge +
                '}';
    }
}
