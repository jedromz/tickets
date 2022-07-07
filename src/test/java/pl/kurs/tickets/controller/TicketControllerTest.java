package pl.kurs.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.kurs.tickets.TicketsApplication;
import pl.kurs.tickets.model.OffenseDictionary;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.command.AddOffenseCommand;
import pl.kurs.tickets.model.command.CreateTicketCommand;
import pl.kurs.tickets.model.searchcriteria.TicketSearchCriteria;
import pl.kurs.tickets.service.OffenseDictionaryService;
import pl.kurs.tickets.service.PersonService;
import pl.kurs.tickets.service.TicketService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = TicketsApplication.class)
@AutoConfigureMockMvc
class TicketControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private PersonService personService;
    @Autowired
    private OffenseDictionaryService offenseDictionaryService;


    @SneakyThrows
    @Test
    void shouldPostSingleTicket() {
        //given
        personService.savePerson(new Person("59112876591", "TEST_FIRSTNAME_1", "TEST_LASTNAME_1", "test_1@mail.com"));
        offenseDictionaryService.saveEntry(new OffenseDictionary("SPEEDING", 15, BigDecimal.valueOf(2000.0), BigDecimal.valueOf(4000.00)));
        CreateTicketCommand command = CreateTicketCommand.builder()
                .pesel("59112876591")
                .date(LocalDate.now())
                .offenses(Set.of(new AddOffenseCommand("SPEEDING", BigDecimal.valueOf(2000))))
                .build();
        String requestJson = objectMapper.writeValueAsString(
                command);
        //when
        MvcResult mvcResult = postman.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        //then
        Ticket ticket = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Ticket.class);
        assertThat(ticket).isNotNull();
        assertThat(ticket.getDate()).isEqualTo(command.getDate());
    }

    @SneakyThrows
    @Test
    void shouldNotPostTicketWithNotFoundPesel() {
        offenseDictionaryService.saveEntry(new OffenseDictionary("SPEEDING", 15, BigDecimal.valueOf(2000.0), BigDecimal.valueOf(4000.00)));
        CreateTicketCommand command = CreateTicketCommand.builder()
                .pesel("61090885791")
                .date(LocalDate.now())
                .offenses(Set.of(new AddOffenseCommand("SPEEDING", BigDecimal.valueOf(2000))))
                .build();
        String requestJson = objectMapper.writeValueAsString(
                command);
        postman.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
    @SneakyThrows
    @Test
    void shouldNotPostTicketWithNotFoundOffense() {
        personService.savePerson(new Person("61090885791", "TEST_FIRSTNAME_1", "TEST_LASTNAME_1", "test_1@mail.com"));
        CreateTicketCommand command = CreateTicketCommand.builder()
                .pesel("61090885791")
                .date(LocalDate.now())
                .offenses(Set.of(new AddOffenseCommand("SPEEDING", BigDecimal.valueOf(2000))))
                .build();
        String requestJson = objectMapper.writeValueAsString(
                command);
        postman.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
    @SneakyThrows
    @Test
    void shouldNotPostTicketWithBadPesel() {
        offenseDictionaryService.saveEntry(new OffenseDictionary("SPEEDING", 15, BigDecimal.valueOf(2000.0), BigDecimal.valueOf(4000.00)));
        CreateTicketCommand command = CreateTicketCommand.builder()
                .pesel("111")
                .date(LocalDate.now())
                .offenses(Set.of(new AddOffenseCommand("SPEEDING", BigDecimal.valueOf(2000))))
                .build();
        String requestJson = objectMapper.writeValueAsString(
                command);
        postman.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}