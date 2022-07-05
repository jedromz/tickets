package pl.kurs.tickets.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//nr pesel ukranego, date, przewinienie, ilosc punktow, ilosc pln.
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id","offenses"})
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @SequenceGenerator(name = "hibernateSequence")
    private Long id;
    private LocalDate date;
    private boolean deleted;
    @Version
    private int version;
    @OneToMany(mappedBy = "ticket",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Offense> offenses = new HashSet<>();
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "person_id")
    private Person person;
    public Ticket(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "date=" + date +
                ", offenses=" + offenses +
                '}';
    }
}
