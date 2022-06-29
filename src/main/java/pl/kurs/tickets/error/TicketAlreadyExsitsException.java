package pl.kurs.tickets.error;

import lombok.Value;

@Value
public class TicketAlreadyExsitsException extends Exception {
    public TicketAlreadyExsitsException() {
        super("TICKET_EXISTS");
    }
}
