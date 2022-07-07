package pl.kurs.tickets.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.tickets.error.EntityNotFoundException;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.command.CreatePersonCommand;
import pl.kurs.tickets.model.searchcriteria.PersonSearchCriteria;
import pl.kurs.tickets.model.command.UpdatePersonCommand;
import pl.kurs.tickets.repository.PersonRepository;


@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final TicketService ticketService;

    @Transactional
    public Person savePerson(CreatePersonCommand command) {
        Person person = new Person(command.getPesel(), command.getFirstname(), command.getLastname(), command.getEmail());
        return personRepository.saveAndFlush(person);
    }


    @Transactional
    public Person savePerson(Person person) {
        return personRepository.saveAndFlush(person);
    }

    @Transactional(readOnly = true)
    public Person getPersonByPesel(String pesel) throws EntityNotFoundException {
        return personRepository.findByPeselWithTickets(pesel)
                .orElseThrow(() -> new EntityNotFoundException("PERSON_PESEL", pesel));
    }

    @Transactional(readOnly = true)
    public Page<Person> findAllWithTickets(Pageable pageable) {
        return personRepository.findAllWithTickets(pageable);
    }

    @Transactional
    public void softDeleteById(Long id) throws EntityNotFoundException {
        Person person = getPersonById(id);
        person.setDeleted(true);
        person.getTickets().forEach(t -> ticketService.deleteById(t.getId()));
    }

    @Transactional
    public Person editById(Long id, UpdatePersonCommand command) throws EntityNotFoundException {
        Person person = getPersonById(id);
        person.setFirstname(command.getFirstname());
        person.setLastname(command.getLastname());
        person.setEmail(command.getEmail());
        person.setPesel(command.getPesel());
        person.setVersion(command.getVersion());
        return person;
    }

    @Transactional(readOnly = true)
    public Person getPersonById(Long id) throws EntityNotFoundException {
        return personRepository.findByIdWithTickets(id)
                .orElseThrow(() -> new EntityNotFoundException("PERSON_ID", id.toString()));
    }

    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    public boolean existsByPesel(String pesel) {
        return personRepository.existsByPesel(pesel);
    }

    public void deletePerson(Person person) {
        personRepository.delete(person);
    }

    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Person> findAllByCriteria(Pageable pageable, PersonSearchCriteria criteria) {
        return personRepository.findAll(criteria.toPredicate(), pageable);
    }
}
