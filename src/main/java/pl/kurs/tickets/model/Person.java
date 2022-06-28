package pl.kurs.tickets.model;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"pesel"}, name = "UC_PERSON_PESEL"), @UniqueConstraint(columnNames = {"email"}, name = "UC_PERSON_EMAIL")})
@EqualsAndHashCode(exclude = {"id", "tickets"})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pesel;
    private String firstname;
    private String lastname;
    private String email;
    private boolean deleted;
    @Version
    private int version;
    @OneToMany(mappedBy = "person")
    private List<Ticket> tickets = new ArrayList<>();

    public Person(String pesel, String firstname, String lastname, String email) {
        this.pesel = pesel;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

}
