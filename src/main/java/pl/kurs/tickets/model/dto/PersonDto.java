package pl.kurs.tickets.model.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pl.kurs.tickets.model.Person;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDto extends RepresentationModel<PersonDto> {
    private Long id;
    private String pesel;
    private String firstname;
    private String lastname;
    private String email;
    private boolean deleted;
    private int version;
}
