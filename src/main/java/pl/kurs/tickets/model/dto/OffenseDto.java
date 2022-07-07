package pl.kurs.tickets.model.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pl.kurs.tickets.model.OffenseDictionary;
import pl.kurs.tickets.model.Ticket;

import javax.persistence.*;
import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OffenseDto extends RepresentationModel<OffenseDto> {

    private Long id;
    private BigDecimal charge;
    private boolean deleted;
    private int version;
}
