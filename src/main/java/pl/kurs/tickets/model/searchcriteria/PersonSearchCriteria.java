package pl.kurs.tickets.model.searchcriteria;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.tickets.model.QPerson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class PersonSearchCriteria {
    private String pesel;
    private String firstname;
    private String lastname;
    private String email;

    public Predicate toPredicate() {
        BooleanBuilder conditions = new BooleanBuilder();
        Optional.ofNullable(getPesel()).map(QPerson.person.pesel::eq).ifPresent(conditions::and);
        Optional.ofNullable(getFirstname()).map(QPerson.person.firstname::eq).ifPresent(conditions::and);
        Optional.ofNullable(getLastname()).map(QPerson.person.lastname::eq).ifPresent(conditions::and);
        Optional.ofNullable(getEmail()).map(QPerson.person.email::eq).ifPresent(conditions::and);
        return conditions;
    }
}
