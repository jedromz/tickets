package pl.kurs.tickets.model.command;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.kurs.tickets.validation.annotation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePersonCommand {
    @PESEL
    @NotNull(message = "PESEL_NOT_NULL")
    private String pesel;
    @Length(max = 200,message = "FIRSTNAME_LENGTH_MAX_200")
    @NotNull(message = "FIRSTNAME_NOT_NULL")
    private String firstname;
    @Length(max = 200,message = "LAST_NAME_LENGTH_MAX_200")
    @NotNull(message = "LASTNAME_NOT_NULL")
    private String lastname;
    @NotNull(message = "EMAIL_NOT_NULL")
    @Email
    @UniqueEmail
    private String email;
}
