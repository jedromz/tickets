package pl.kurs.tickets.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.kurs.tickets.error.EntityNotFoundException;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.command.CreatePersonCommand;
import pl.kurs.tickets.repository.PersonRepository;
import pl.kurs.tickets.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonServiceTest {
    private PersonService personService;
    @Mock
    private TicketService ticketService;
    @Mock
    private PersonRepository personRepository;
    private Person PERSON_1;
    private Person PERSON_2;
    private Person PERSON_3;
    private Page<Person> PERSON_PAGE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personService = new PersonService(personRepository, ticketService);
        PERSON_1 = new Person("79021533233", "TEST_FIRSTNAME_1", "TEST_LASTNAME_1", "test_1@mail.com");
        PERSON_2 = new Person("62022289968", "TEST_FIRSTNAME_2", "TEST_LASTNAME_2", "test_2@mail.com");
        PERSON_3 = new Person("04293031796", "TEST_FIRSTNAME_3", "TEST_LASTNAME_3", "test_3@mail.com");
        List<Person> peopleLIST = new ArrayList<>();
        peopleLIST.add(PERSON_1);
        peopleLIST.add(PERSON_2);
        peopleLIST.add(PERSON_3);
        PERSON_PAGE = new PageImpl<>(peopleLIST);
    }

    @Test
    void shouldSavePerson() {
        CreatePersonCommand command = CreatePersonCommand.builder()
                .pesel(PERSON_1.getPesel())
                .firstname(PERSON_1.getFirstname())
                .lastname(PERSON_1.getLastname())
                .email(PERSON_1.getEmail())
                .build();
        when(personRepository.saveAndFlush(any())).thenReturn(PERSON_1);
        personService.savePerson(command);
        verify(personRepository).saveAndFlush(any(Person.class));
    }

    @Test
    void shouldGetPersonByPesel() {
        when(personRepository.findByPeselWithTickets(anyString())).thenReturn(Optional.ofNullable(PERSON_1));
        personService.getPersonByPesel(PERSON_1.getPesel());
        verify(personRepository).findByPeselWithTickets(anyString());
    }

    @Test
    void shouldNotGetPersonByPesel() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            when(personRepository.findByPeselWithTickets(anyString())).thenReturn(Optional.empty());
            personService.getPersonByPesel(PERSON_1.getPesel());
            verify(personRepository).findByPeselWithTickets(anyString());
        });
    }

    @Test
    void findAllWithTickets() {
        when(personRepository.findAllWithTickets(any())).thenReturn(PERSON_PAGE);
        personService.findAllWithTickets(Pageable.unpaged());
        verify(personRepository).findAllWithTickets(any());
    }

    @Test
    void shouldSoftDeleteById() {
        Person TO_DELETE = new Person("80102357976", "TO_DELETE_FIRSTNAME", "TO_DELETE_LASTNAME", "to_delete@mail.com");
        when(personRepository.findByIdWithTickets(anyLong())).thenReturn(Optional.of(TO_DELETE));
        personService.softDeleteById(anyLong());
        assertTrue(TO_DELETE.isDeleted());
        verify(personRepository).findByIdWithTickets(anyLong());
    }

    @Test
    void shouldNotSoftDeleteByNotExistingId() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            when(personRepository.findByIdWithTickets(anyLong())).thenReturn(Optional.empty());
            personService.softDeleteById(anyLong());
            verify(personRepository).findByIdWithTickets(anyLong());
        });
    }
}