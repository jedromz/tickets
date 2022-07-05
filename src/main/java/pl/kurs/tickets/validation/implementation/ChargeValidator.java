package pl.kurs.tickets.validation.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import pl.kurs.tickets.service.OffenseDictionaryService;
import pl.kurs.tickets.validation.annotation.ValidCharge;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ChargeValidator implements ConstraintValidator<ValidCharge, Object> {

    private String chargeName;
    private String chargeField;
    private final OffenseDictionaryService offenseDictionaryService;

    public void initialize(ValidCharge constraintAnnotation) {
        this.chargeName = constraintAnnotation.chargeName();
        this.chargeField = constraintAnnotation.chargeField();
    }


    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String chargeNameValue = String.valueOf(new BeanWrapperImpl(value)
                .getPropertyValue(chargeName));
        BigDecimal chargeFieldValue = (BigDecimal) new BeanWrapperImpl(value)
                .getPropertyValue(chargeField);

        if (chargeFieldValue != null && !chargeNameValue.isEmpty())
            return offenseDictionaryService.chargeIsWithinRage(chargeNameValue, chargeFieldValue);
        return false;
    }
}