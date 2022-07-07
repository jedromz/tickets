package pl.kurs.tickets.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.repository.PersonRepository;
import pl.kurs.tickets.repository.TicketRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private MailService mailService;
    private TicketService ticketService;
    Person PERSON_1;
    Ticket TICKET_1;
    Ticket TICKET_2;
    Ticket TICKET_3;

    @BeforeEach
    void setUp() {
        PERSON_1 = new Person("79021533233", "TEST_FIRSTNAME_1", "TEST_LASTNAME_1", "test_1@mail.com");
        TICKET_1 = new Ticket(LocalDate.now());
        TICKET_2 = new Ticket(LocalDate.now().plusDays(1));
        TICKET_3 = new Ticket(LocalDate.now().plusDays(3));
    }

    @Test
    void saveTicket() {
    }

    @Test
    void findAllByPesel() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void findAll() {
    }
}