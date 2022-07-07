package pl.kurs.tickets.mapping;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.tickets.controller.PersonController;
import pl.kurs.tickets.controller.TicketController;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.dto.TicketDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
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
                .build();
        ticketDto.add(linkTo(methodOn(TicketController.class).getOffensesByTicketId(Pageable.unpaged(), ticket.getId()))
                .withRel("offenses"));
        ticketDto.add(linkTo(methodOn(PersonController.class).getPersonById(ticket.getPerson().getId()))
                .withRel("person-details"));
        return ticketDto;

    }
}
