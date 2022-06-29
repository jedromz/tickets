package pl.kurs.tickets.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.command.CreateTicketCommand;
import pl.kurs.tickets.model.command.UpdateTicketCommand;
import pl.kurs.tickets.repository.PersonRepository;
import pl.kurs.tickets.repository.TicketRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    List<String> OFFENSES;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketService(ticketRepository, personRepository, mailService);
        PERSON_1 = new Person("00231048459", "TEST_FIRSTNAME", "TEST_LASTNAME", "test@mail.com");
        OFFENSES = new ArrayList<>(List.of("speeding", "drunk driving", "no seatbelt", "no license plate", "dangerous driving"));
        TICKET_1 = new Ticket(LocalDate.now(), 5, BigDecimal.valueOf(1000));
        TICKET_2 = new Ticket(LocalDate.now(), 10, BigDecimal.valueOf(2000));
        TICKET_3 = new Ticket(LocalDate.now(), 15, BigDecimal.valueOf(3000));
        TICKET_1.setOffenses(OFFENSES);
    }

    @SneakyThrows
    @Test
    void shouldSaveTicket() {
        CreateTicketCommand command = CreateTicketCommand.builder()
                .date(TICKET_1.getDate())
                .offenses(TICKET_1.getOffenses())
                .charge(TICKET_1.getCharge())
                .pesel(PERSON_1.getPesel())
                .points(TICKET_1.getPoints())
                .build();
        when(personRepository.findByPeselWithTickets(anyString())).thenReturn(Optional.ofNullable(PERSON_1));
        when(ticketRepository.saveAndFlush(any())).thenReturn(TICKET_1);
        ticketService.saveTicket(command);
        verify(personRepository).findByPeselWithTickets(anyString());
        verify(ticketRepository).saveAndFlush(any());
    }

    @Test
    void shouldFindAllByPesel() {
        Page<Ticket> page = new PageImpl<>(Arrays.asList(TICKET_1, TICKET_2, TICKET_3));
        when(ticketRepository.findByPerson_Pesel(any(), any())).thenReturn(page);
        ticketService.findAllByPesel(Pageable.unpaged(), PERSON_1.getPesel());
        verify(ticketRepository).findByPerson_Pesel(any(), any());
    }

    @Test
    void shouldDeleteById() {
        Ticket TO_DELETE = new Ticket(LocalDate.now(), 15, BigDecimal.valueOf(3000));
        TO_DELETE.setOffenses(OFFENSES);
        when(ticketRepository.findById(any())).thenReturn(Optional.of(TO_DELETE));
        ticketService.deleteById(anyLong());
        assertTrue(TO_DELETE.isDeleted());
        verify(ticketRepository).findById(any());
    }

    @Test
    void shouldFindAll() {
        Page<Ticket> page = new PageImpl<>(Arrays.asList(TICKET_1, TICKET_2, TICKET_3));
        when(ticketRepository.findAll(any(Pageable.class))).thenReturn(page);
        ticketService.findAll(Pageable.unpaged());
        verify(ticketRepository).findAll(any(Pageable.class));
    }

    @Test
    void updateTicket() {
        LocalDate updatedDate = LocalDate.now().plusDays(30);
        BigDecimal updatedCharge = BigDecimal.valueOf(100);
        int updatedPoints = 0;
        int updatedVersion = TICKET_1.getVersion() + 1;
        UpdateTicketCommand command = UpdateTicketCommand.builder()
                .date(updatedDate)
                .charge(updatedCharge)
                .points(updatedPoints)
                .version(updatedVersion)
                .build();
        when(ticketRepository.findById(any())).thenReturn(Optional.of(TICKET_1));
        Ticket updatedTicket = ticketService.updateTicket(1L, command);
        verify(ticketRepository).findById(any());
        assertEquals(updatedTicket.getDate(), updatedDate);
        assertEquals(updatedTicket.getCharge(), updatedCharge);
        assertEquals(updatedTicket.getPoints(), updatedPoints);
    }
}