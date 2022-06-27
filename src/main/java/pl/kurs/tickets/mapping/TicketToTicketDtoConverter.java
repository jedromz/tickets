package pl.kurs.tickets.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.tickets.controller.PersonController;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.dto.TicketDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TicketToTicketDtoConverter implements Converter<Ticket, TicketDto> {
    @Override
    public TicketDto convert(MappingContext<Ticket, TicketDto> mappingContext) {
        Ticket ticket = mappingContext.getSource();
        TicketDto ticketDto = TicketDto.builder()
                .id(ticket.getId())
                .date(ticket.getDate())
                .pesel(ticket.getPerson().getPesel())
                .charge(ticket.getCharge())
                .offense(ticket.getOffense())
                .points(ticket.getPoints())
                .build();
        ticketDto.add(linkTo(methodOn(PersonController.class).getPersonByPesel(ticket.getPerson().getPesel()))
                .withRel("person-details"));
        return ticketDto;
    }
}
