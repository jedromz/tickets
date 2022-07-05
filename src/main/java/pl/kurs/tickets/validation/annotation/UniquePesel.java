package pl.kurs.tickets.validation.annotation;

import pl.kurs.tickets.validation.implementation.UniqueEmailValidator;
import pl.kurs.tickets.validation.implementation.UniquePeselValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePeselValidator.class)
public @interface UniquePesel {

    String message() default "PESEL_NOT_UNIQUE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
