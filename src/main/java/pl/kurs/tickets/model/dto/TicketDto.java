package pl.kurs.tickets.model.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pl.kurs.tickets.model.Person;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto extends RepresentationModel<TicketDto> {
    private Long id;
    private LocalDate date;
    private List<String> offenses;
    private Integer points;
    private BigDecimal charge;
    private String pesel;
}
