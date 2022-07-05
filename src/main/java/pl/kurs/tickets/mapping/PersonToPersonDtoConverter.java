package pl.kurs.tickets.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.tickets.controller.PersonController;
import pl.kurs.tickets.controller.TicketController;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.dto.PersonDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonToPersonDtoConverter implements Converter<Person, PersonDto> {
    @Override
    public PersonDto convert(MappingContext<Person, PersonDto> mappingContext) {
        Person person = mappingContext.getSource();
        PersonDto personDto = PersonDto.builder()
                .id(person.getId())
                .firstname(person.getFirstname())
                .lastname(person.getLastname())
                .email(person.getEmail())
                .pesel(person.getPesel())
                .version(person.getVersion())
                .deleted(person.isDeleted())
                .build();
        return personDto;
    }
}
