package pl.kurs.tickets.model.command;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOffenseDictionaryEntryCommand {
    @NotNull(message = "NAME_NOT_NULL")
    private String name;
    @NotNull(message = "POINTS_NOT_NULL")
    @PositiveOrZero
    private Integer points;
    @NotNull(message = "MIN_CHARGE_NOT_NULL")
    @PositiveOrZero
    private BigDecimal minCharge;
    @NotNull(message = "MAX_CHARGE_NOT_NULL")
    @PositiveOrZero
    private BigDecimal maxCharge;
}
