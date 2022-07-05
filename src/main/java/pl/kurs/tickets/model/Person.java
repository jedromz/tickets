package pl.kurs.tickets.model;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"pesel"}, name = "UC_PERSON_PESEL"), @UniqueConstraint(columnNames = {"email"}, name = "UC_PERSON_EMAIL")})
@EqualsAndHashCode(exclude = {"id", "tickets"})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernateSequence")
    @SequenceGenerator(name = "hibernateSequence")
    private Long id;
    private String pesel;
    private String firstname;
    private String lastname;
    private String email;
    private boolean deleted;
    @OneToMany(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Ticket> tickets = new HashSet<>();
    @Version
    private int version;

    public Person(String pesel, String firstname, String lastname, String email) {
        this.pesel = pesel;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

}
