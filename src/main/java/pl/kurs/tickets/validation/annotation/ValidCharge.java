package pl.kurs.tickets.validation.annotation;

import pl.kurs.tickets.validation.implementation.ChargeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

@Constraint(validatedBy = ChargeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCharge {

    String message() default "BAD_CHARGE";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    String chargeField();
    String chargeName();
}