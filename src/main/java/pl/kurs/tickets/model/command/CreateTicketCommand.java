package pl.kurs.tickets.model.command;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.pl.PESEL;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketCommand {

    @FutureOrPresent
    @NotNull(message = "DATE_NOT_NULL")
    private LocalDate date;
    @NotNull(message = "OFFENSE_NOT_NULL")
    private List<String> offenses;
    @Max(15)
    @NotNull(message = "POINTS_NOT_NULL")
    private Integer points;
    @Max(5000)
    @NotNull(message = "CHARGE_NOT_NULL")
    private BigDecimal charge;
    @NotNull(message = "PESEL_NOT_NULL")
    @PESEL
    private String pesel;
}
