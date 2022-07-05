package pl.kurs.tickets.model.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto extends RepresentationModel<TicketDto> {
    private Long id;
    private LocalDate date;
    private Set<String> offenses;
    private Integer points;
    private BigDecimal charge;
    private String pesel;
    private boolean deleted;
    private int version;
}
