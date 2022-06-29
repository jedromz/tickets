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
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.command.CreateTicketCommand;
import pl.kurs.tickets.model.command.UpdateTicketCommand;
import pl.kurs.tickets.model.dto.PersonDto;
import pl.kurs.tickets.model.dto.TicketDto;
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
        return new ResponseEntity<>(modelMapper.map(ticket, TicketDto.class),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @SneakyThrows
    public ResponseEntity<TicketDto> putTicket(@PathVariable Long id, @RequestBody @Valid UpdateTicketCommand command) {
        Ticket ticket = ticketService.updateTicket(id, command);
        return ResponseEntity.ok(modelMapper.map(ticket, TicketDto.class));
    }

    @GetMapping("/{pesel}")
    public ResponseEntity<Page<TicketDto>> getAllTicketsByPesel(@PageableDefault Pageable pageable, @PathVariable String pesel) {
        return ResponseEntity.ok(ticketService.findAllByPesel(pageable, pesel)
                .map(t -> modelMapper.map(t, TicketDto.class)));
    }

    @GetMapping
    public ResponseEntity<Page<TicketDto>> getAllTickets(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(ticketService.findAll(pageable)
                .map(t -> modelMapper.map(t, TicketDto.class)));
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity deletePersonById(@PathVariable Long id) {
        ticketService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
