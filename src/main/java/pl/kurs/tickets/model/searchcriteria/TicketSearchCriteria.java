package pl.kurs.tickets.model.searchcriteria;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.tickets.model.QPerson;
import pl.kurs.tickets.model.QTicket;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@Builder
public class TicketSearchCriteria {
    private String pesel;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Predicate toPredicate() {
        BooleanBuilder conditions = new BooleanBuilder();
        Optional.ofNullable(getPesel()).map(QTicket.ticket.person.pesel::eq).ifPresent(conditions::and);
        Optional.ofNullable(getFirstname()).map(QTicket.ticket.person.firstname::eq).ifPresent(conditions::and);
        Optional.ofNullable(getLastname()).map(QTicket.ticket.person.lastname::eq).ifPresent(conditions::and);
        Optional.ofNullable(getEmail()).map(QTicket.ticket.person.email::eq).ifPresent(conditions::and);
        Optional.ofNullable(getDateFrom()).map(QTicket.ticket.date::gt).ifPresent(conditions::and);
        Optional.ofNullable(getDateTo()).map(QTicket.ticket.date::lt).ifPresent(conditions::and);
        return conditions;
    }

}
