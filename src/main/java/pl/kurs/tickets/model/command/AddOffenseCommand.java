package pl.kurs.tickets.model.command;

import lombok.*;
import pl.kurs.tickets.validation.annotation.ValidCharge;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidCharge(chargeField = "charge",chargeName = "name")
public class AddOffenseCommand {
    @NotNull(message = "NAME_NOT_NULL")
    private String name;
    @NotNull(message = "CHARGE_NOT_NULL")
    @PositiveOrZero
    private BigDecimal charge;
}
