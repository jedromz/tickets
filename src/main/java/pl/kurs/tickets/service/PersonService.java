package pl.kurs.tickets.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.tickets.error.EntityNotFoundException;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.command.CreatePersonCommand;
import pl.kurs.tickets.model.command.UpdatePersonCommand;
import pl.kurs.tickets.repository.PersonRepository;


@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    @Transactional
    public Person savePerson(CreatePersonCommand command) {
        Person person = new Person(command.getPesel(), command.getFirstname(), command.getLastname(), command.getEmail());
        return personRepository.saveAndFlush(person);
    }

    @Transactional(readOnly = true)
    public Person findPersonByPesel(String pesel) throws EntityNotFoundException {
        return personRepository.findByPeselWithTickets(pesel)
                .orElseThrow(() -> new EntityNotFoundException("PERSON_PESEL", pesel));
    }

    @Transactional(readOnly = true)
    public Page<Person> findAllWithTickets(Pageable pageable) {
        return personRepository.findAllWithTickets(pageable);
    }

    @Transactional
    public void deleteById(Long id) throws EntityNotFoundException {
        Person person = getPersonById(id);
        person.setDeleted(true);
        person.getTickets().forEach(p -> p.setDeleted(true));
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

    public Person getPersonById(Long id) throws EntityNotFoundException {
        return personRepository.findByIdWithTickets(id)
                .orElseThrow(() -> new EntityNotFoundException("PERSON_ID", id.toString()));
    }
}
