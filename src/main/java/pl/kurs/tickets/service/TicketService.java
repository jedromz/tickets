package pl.kurs.tickets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.tickets.error.EntityNotFoundException;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.command.CreateTicketCommand;
import pl.kurs.tickets.model.command.UpdateTicketCommand;
import pl.kurs.tickets.model.dto.NotificationEmail;
import pl.kurs.tickets.repository.PersonRepository;
import pl.kurs.tickets.repository.TicketRepository;

import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final PersonRepository personRepository;
    private final MailService mailService;


    @Transactional
    public Ticket saveTicket(CreateTicketCommand command) throws EntityNotFoundException {
        String pesel = command.getPesel();
        Person person = personRepository.findByPeselWithTickets(pesel)
                .orElseThrow(() -> new EntityNotFoundException("PERSON_PESEL", pesel));
        Ticket ticket = new Ticket(command.getDate(), command.getOffense(), command.getPoints(), command.getCharge());
        person.getTickets().add(ticket);
        ticket.setPerson(person);
        Ticket savedTicket = ticketRepository.saveAndFlush(ticket);
        if (ticketRepository.sumPointsByPeselAndDateBetween(pesel, LocalDate.now().with(firstDayOfYear()), LocalDate.now().with(lastDayOfYear())) > 24) {
            mailService.sendMail(new NotificationEmail());
        }
        return savedTicket;
    }

    @Transactional(readOnly = true)
    public Page<Ticket> findAllByPesel(Pageable page, String pesel) {
        return ticketRepository.findByPerson_Pesel(pesel, page);
    }

    @Transactional
    public void deleteById(Long id) throws EntityNotFoundException {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TICKET_ID", id.toString()));
        ticket.setDeleted(true);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> findAll(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    @Transactional
    public Ticket updateTicket(Long id, UpdateTicketCommand command) throws EntityNotFoundException {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TICKET_ID", id.toString()));
        ticket.setDate(command.getDate());
        ticket.setCharge(command.getCharge());
        ticket.setPoints(command.getPoints());
        ticket.setOffense(command.getOffense());
        ticket.setVersion(command.getVersion());
        return ticket;
    }
}
