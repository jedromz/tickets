package pl.kurs.tickets.validation.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.tickets.service.PersonService;
import pl.kurs.tickets.validation.annotation.UniqueEmail;
import pl.kurs.tickets.validation.annotation.UniquePesel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
@RequiredArgsConstructor
public class UniquePeselValidator implements ConstraintValidator<UniquePesel, String> {

    private final PersonService personService;

    @Override
    public boolean isValid(String pesel, ConstraintValidatorContext constraintValidatorContext) {
        return !personService.existsByPesel(pesel);
    }
}
