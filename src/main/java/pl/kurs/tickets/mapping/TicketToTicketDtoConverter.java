package pl.kurs.tickets.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.tickets.controller.PersonController;
import pl.kurs.tickets.model.Offense;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.dto.TicketDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TicketToTicketDtoConverter implements Converter<Ticket, TicketDto> {
    @Override
    public TicketDto convert(MappingContext<Ticket, TicketDto> mappingContext) {
        Ticket ticket = mappingContext.getSource();
        TicketDto ticketDto = TicketDto.builder()
                .id(ticket.getId())
                .pesel(ticket.getPerson().getPesel())
                .date(ticket.getDate())
                .deleted(ticket.isDeleted())
                .version(ticket.getVersion())
                .offenses(ticket.getOffenses().stream().map(o -> o.getOffenseDictionary().getName()).collect(Collectors.toSet()))
                .charge(ticket.getOffenses().stream().map(Offense::getCharge).reduce(BigDecimal.ZERO, BigDecimal::add))
                .points(ticket.getOffenses().stream().map(o -> o.getOffenseDictionary().getPoints()).reduce(0, Integer::sum))
                .build();
        ticketDto.add(linkTo(methodOn(PersonController.class).getPersonByPesel(ticket.getPerson().getPesel()))
                .withRel("person-details"));
        return ticketDto;
    }
}
