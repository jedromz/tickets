package pl.kurs.tickets.model.command;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.pl.PESEL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePersonCommand {
    @PESEL
    @NotNull(message = "PESEL_NOT_NULL")
    private String pesel;
    @Length(max = 200)
    @NotNull(message = "FIRSTNAME_NOT_NULL")
    private String firstname;
    @Length(max = 200)
    @NotNull(message = "LASTNAME_NOT_NULL")
    private String lastname;
    @Length(max = 200)
    @NotNull(message = "EMAIL_NOT_NULL")
    @Email
    private String email;
    @NotNull(message = "VERSION_NOT_NULL")
    private Integer version;
}
