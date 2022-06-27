package pl.kurs.tickets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String offense;
    private Integer points;
    private BigDecimal charge;
    private boolean deleted;
    @Version
    private int version;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    public Ticket(LocalDate date, String offense, Integer points, BigDecimal charge) {
        this.date = date;
        this.offense = offense;
        this.points = points;
        this.charge = charge;
    }
}
