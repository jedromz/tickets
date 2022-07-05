package pl.kurs.tickets.validation.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.tickets.service.PersonService;
import pl.kurs.tickets.validation.annotation.UniqueEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final PersonService personService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !personService.existsByEmail(email);
    }
}
