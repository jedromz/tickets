package pl.kurs.tickets.model.dto;

import liquibase.pro.packaged.B;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OffenseDictionaryDto {
    private Long id;
    private String name;
    private Integer points;
    private BigDecimal minCharge;
    private BigDecimal maxCharge;
    private boolean deleted;
    private int version;
}

