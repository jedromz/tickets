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
import pl.kurs.tickets.model.Offense;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.command.CreateTicketCommand;
import pl.kurs.tickets.model.dto.OffenseDto;
import pl.kurs.tickets.model.dto.PersonDto;
import pl.kurs.tickets.model.dto.TicketDto;
import pl.kurs.tickets.model.searchcriteria.PersonSearchCriteria;
import pl.kurs.tickets.model.searchcriteria.TicketSearchCriteria;
import pl.kurs.tickets.service.TicketService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<TicketDto> postTicket(@RequestBody @Valid CreateTicketCommand command) {
        Ticket ticket = ticketService.saveTicket(command);
        return new ResponseEntity<>(modelMapper.map(ticket, TicketDto.class), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<Page<TicketDto>> getAllTickets(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(ticketService.findAll(pageable)
                .map(t -> modelMapper.map(t, TicketDto.class)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        TicketDto ticketDto = modelMapper.map(ticket, TicketDto.class);
        return ResponseEntity.ok(ticketDto);
    }

    @PostMapping("/search")

    public ResponseEntity<Page<TicketDto>> searchPeople(@PageableDefault Pageable pageable, @RequestBody TicketSearchCriteria criteria) {
        Page<Ticket> ticketsByCriteria = ticketService.findAllByCriteria(pageable, criteria);
        return ResponseEntity.ok(ticketsByCriteria.map(t -> modelMapper.map(t, TicketDto.class)));
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity deletePersonById(@PathVariable Long id) {
        ticketService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/offenses")
    public ResponseEntity<Page<OffenseDto>> getOffensesByTicketId(@PageableDefault Pageable pageable, @PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getOffensesById(pageable, id).map(o -> modelMapper.map(o, OffenseDto.class)));
    }
}
