package pl.kurs.tickets.error.constraint;

public interface ConstraintErrorHandler {
    String constraintName();

    String message();

    String field();
}
