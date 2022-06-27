package pl.kurs.tickets.error.constraint.implementation;

import org.springframework.stereotype.Service;
import pl.kurs.tickets.error.constraint.ConstraintErrorHandler;

@Service
public class PersonPeselUniqueConstraintHandler implements ConstraintErrorHandler {
    @Override
    public String constraintName() {
        return "UC_PERSON_NIP";
    }

    @Override
    public String message() {
        return "NIP_NOT_UNIQUE";
    }

    @Override
    public String field() {
        return "person_nip";
    }
}
