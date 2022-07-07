package pl.kurs.tickets.error.constraint.implementation;

import org.springframework.stereotype.Service;
import pl.kurs.tickets.error.constraint.ConstraintErrorHandler;

@Service
public class OffenseDictUniqueNameConstraint implements ConstraintErrorHandler {
    @Override
    public String constraintName() {
        return "PUBLIC.UC_OFFENSE_DICT_NAME";
    }

    @Override
    public String message() {
        return "OFFENSE_DICT_NAME_NOT_UNIQUE";
    }

    @Override
    public String field() {
        return "offense_dict_name";
    }
}
