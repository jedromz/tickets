package pl.kurs.tickets.service;

import liquibase.pro.packaged.P;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.command.CreatePersonCommand;
import pl.kurs.tickets.model.command.UpdatePersonCommand;
import pl.kurs.tickets.repository.PersonRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    private PersonService personService;
    Person PERSON_1;
    Person PERSON_2;
    Person PERSON_3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personService = new PersonService(personRepository);
        PERSON_1 = new Person("64062278252", "TEST_FIRSTNAME", "TEST_LASTNAME", "test_1@email.com");
        PERSON_2 = new Person("00222295439", "TEST_FIRSTNAME", "TEST_LASTNAME", "test_2@email.com");
        PERSON_3 = new Person("83080442133", "TEST_FIRSTNAME", "TEST_LASTNAME", "test_3@email.com");
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
        Person result = personService.savePerson(command);
        verify(personRepository).saveAndFlush(any());
        assertEquals(PERSON_1, result);
    }

    @Test
    void shouldFindPersonByPesel() {
        when(personRepository.findByPeselWithTickets(any())).thenReturn(Optional.ofNullable(PERSON_1));
        Person result = personService.findPersonByPesel(PERSON_1.getPesel());
        verify(personRepository).findByPeselWithTickets(any());
        assertEquals(PERSON_1, result);
    }

    @Test
    void shouldFindAllWithTickets() {
        Page<Person> page = new PageImpl<>(Arrays.asList(PERSON_1, PERSON_2, PERSON_3));
        when(personRepository.findAllWithTickets(any())).thenReturn(page);
        Page<Person> result = personService.findAllWithTickets(Pageable.unpaged());
        verify(personRepository).findAllWithTickets(any());
        assertEquals(page, result);
    }

    @Test
    void shouldDeleteById() {
        long id = 1L;
        Person TO_DELETE = new Person("83080442133", "TEST_FIRSTNAME", "TEST_LASTNAME", "test_3@email.com");
        Ticket TICKET_TO_DELETE = new Ticket(LocalDate.now(), "TEST_OFFENSE", 5, BigDecimal.valueOf(1000));
        TO_DELETE.getTickets().add(TICKET_TO_DELETE);
        when(personRepository.findByIdWithTickets(any())).thenReturn(Optional.of(TO_DELETE));
        personService.deleteById(id);
        verify(personRepository).findByIdWithTickets(any());
        assertTrue(TO_DELETE.isDeleted());
        assertTrue(TICKET_TO_DELETE.isDeleted());
    }

    @Test
    void editById() {
        String updatedPesel = "76122259181";
        String updatedFirstname = "UPDATED_FIRSTNAME";
        String updatedLastname = "UPDATED_LASTNAME";
        String updatedEmail = "updated@mail.com";
        Person TO_EDIT = new Person("83080442133", "TEST_FIRSTNAME", "TEST_LASTNAME", "test_3@email.com");
        int updatedVersion = PERSON_1.getVersion() + 1;
        UpdatePersonCommand command = UpdatePersonCommand.builder()
                .pesel(updatedPesel)
                .firstname(updatedFirstname)
                .lastname(updatedLastname)
                .email(updatedEmail)
                .version(updatedVersion)
                .build();
        when(personRepository.findByIdWithTickets(any())).thenReturn(Optional.of(TO_EDIT));
        personService.editById(anyLong(), command);
        verify(personRepository).findByIdWithTickets(any());
        assertEquals(TO_EDIT.getPesel(), updatedPesel);
        assertEquals(TO_EDIT.getFirstname(), updatedFirstname);
        assertEquals(TO_EDIT.getLastname(), updatedLastname);
        assertEquals(TO_EDIT.getEmail(), updatedEmail);
        assertEquals(TO_EDIT.getVersion(), updatedVersion);
    }

    @Test
    void shouldGetPersonById() {
        when(personRepository.findByIdWithTickets(any())).thenReturn(Optional.ofNullable(PERSON_1));
        Person result = personService.getPersonById(any());
        verify(personRepository).findByIdWithTickets(any());
        assertEquals(PERSON_1, result);
    }
}