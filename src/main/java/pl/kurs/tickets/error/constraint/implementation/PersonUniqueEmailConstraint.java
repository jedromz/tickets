package pl.kurs.tickets.error.constraint.implementation;

import org.springframework.stereotype.Service;
import pl.kurs.tickets.error.constraint.ConstraintErrorHandler;

@Service
public class PersonUniqueEmailConstraint implements ConstraintErrorHandler {
    @Override
    public String constraintName() {
        return "PUBLIC.UC_PERSON_EMAIL";
    }

    @Override
    public String message() {
        return "EMAIL_NOT_UNIQUE";
    }

    @Override
    public String field() {
        return "person_email";
    }
}
