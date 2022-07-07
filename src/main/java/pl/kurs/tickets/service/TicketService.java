package pl.kurs.tickets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.tickets.error.EntityNotFoundException;
import pl.kurs.tickets.model.Offense;
import pl.kurs.tickets.model.OffenseDictionary;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.command.AddOffenseCommand;
import pl.kurs.tickets.model.command.CreateTicketCommand;
import pl.kurs.tickets.model.dto.NotificationEmail;
import pl.kurs.tickets.model.dto.TicketDto;
import pl.kurs.tickets.model.searchcriteria.PersonSearchCriteria;
import pl.kurs.tickets.model.searchcriteria.TicketSearchCriteria;
import pl.kurs.tickets.repository.OffenseDictionaryRepository;
import pl.kurs.tickets.repository.OffenseRepository;
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
    private final OffenseRepository offenseRepository;
    private final OffenseDictionaryRepository offenseDictionaryRepository;
    private final MailService mailService;

    @Transactional
    public Ticket saveTicket(CreateTicketCommand command) {
        Person person = getPersonByPesel(command);
        Ticket ticket = new Ticket(command.getDate());
        person.getTickets().add(ticket);
        ticket.setPerson(person);
        for (AddOffenseCommand offenseCommand : command.getOffenses()) {
            OffenseDictionary offenseDictEntry = getOffenseDictEntryByName(offenseCommand.getName());
            Offense offense = new Offense(offenseCommand.getCharge());
            offense.setOffenseDictionary(offenseDictEntry);
            ticket.getOffenses().add(offense);
            offense.setTicket(ticket);
        }
        if (sumPointsByPeselAndDateBetween(person.getPesel(), LocalDate.now().with(firstDayOfYear()), LocalDate.now().with(lastDayOfYear())) > 24) {
            mailService.sendMail(new NotificationEmail("polska@policja.com", "mandat", person.getEmail(), "mandat elo"));
        }
        return ticketRepository.saveAndFlush(ticket);
    }

    private Integer sumPointsByPeselAndDateBetween(String pesel, LocalDate start, LocalDate end) {
        return ticketRepository.sumPointsByPeselAndDateBetween(pesel, start, end);
    }

    private OffenseDictionary getOffenseDictEntryByName(String name) {
        return offenseDictionaryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("OFFENSE_DICT_NAME", name));
    }

    private Person getPersonByPesel(CreateTicketCommand command) {
        return personRepository.findByPeselWithTickets(command.getPesel())
                .orElseThrow(() -> new EntityNotFoundException("PERSON_PESEL", command.getPesel()));
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
        ticket.getOffenses().forEach(o -> o.setDeleted(true));
    }

    @Transactional(readOnly = true)
    public Page<Ticket> findAll(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Ticket> findAllByCriteria(Pageable pageable, TicketSearchCriteria criteria) {
        return ticketRepository.findAll(criteria.toPredicate(), pageable);
    }

    public Page<Offense> getOffensesById(Pageable pageable, Long id) {
        return offenseRepository.findByTicket_Id(id, pageable);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TICKET_ID", Long.toString(id)));
    }

    @Transactional
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.saveAndFlush(ticket);
    }
    public void deleteTicket(Ticket ticket){
        ticketRepository.delete(ticket);
    }
}
