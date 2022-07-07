package pl.kurs.tickets.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.command.CreatePersonCommand;
import pl.kurs.tickets.model.searchcriteria.PersonSearchCriteria;
import pl.kurs.tickets.model.command.UpdatePersonCommand;
import pl.kurs.tickets.model.dto.PersonDto;
import pl.kurs.tickets.service.PersonService;

import javax.validation.Valid;

@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<PersonDto> postPerson(@RequestBody @Valid CreatePersonCommand command) {
        Person person = personService.savePerson(command);
        return new ResponseEntity<>(modelMapper.map(person, PersonDto.class), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PersonDto>> getAllPeople(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(personService.findAllWithTickets(pageable)
                .map(p -> modelMapper.map(p, PersonDto.class)));
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity deletePersonById(@PathVariable Long id) {
        personService.softDeleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> putPersonById(@PathVariable Long id, @Valid UpdatePersonCommand command) {
        Person person = personService.editById(id, command);
        return new ResponseEntity<>(modelMapper.map(person, PersonDto.class), HttpStatus.CREATED);
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable Long id) {
        Person person = personService.getPersonById(id);
        return ResponseEntity.ok(modelMapper.map(person, PersonDto.class));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<PersonDto>> searchPeople(@PageableDefault Pageable pageable, @RequestBody PersonSearchCriteria criteria) {
        Page<Person> peopleByCriteria = personService.findAllByCriteria(pageable, criteria);
        return ResponseEntity.ok(peopleByCriteria.map(p -> modelMapper.map(p, PersonDto.class)));
    }
}
