package pl.kurs.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
import pl.kurs.tickets.model.command.CreateOffenseDictionaryEntryCommand;
import pl.kurs.tickets.model.command.CreateTicketCommand;
import pl.kurs.tickets.service.OffenseDictionaryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TicketsApplication.class)
@AutoConfigureMockMvc
class OffenseDictionaryControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OffenseDictionaryService offenseDictionaryService;

    @SneakyThrows
    @Test
    void shouldPostOffenseDictionary() {
        //given
        CreateOffenseDictionaryEntryCommand command = CreateOffenseDictionaryEntryCommand
                .builder()
                .name("TEST_OFFENSE")
                .minCharge(BigDecimal.valueOf(1000))
                .maxCharge(BigDecimal.valueOf(4000))
                .points(10)
                .build();
        String requestJson = objectMapper.writeValueAsString(
                command);
        //when
        MvcResult mvcResult = postman.perform(post("/offense-dictionary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        //then
        OffenseDictionary offenseDictionary = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OffenseDictionary.class);
        assertThat(offenseDictionary).isNotNull();
        assertThat(offenseDictionary.getName()).isEqualTo(command.getName());
    }

    @SneakyThrows
    @Test
    void shouldNotPostOffenseDictionaryWithNotUniqueName() {
        offenseDictionaryService.saveEntry(new OffenseDictionary("TEST_OFFENSE", 10, BigDecimal.valueOf(1000), BigDecimal.valueOf(4000)));
        //given
        CreateOffenseDictionaryEntryCommand command = CreateOffenseDictionaryEntryCommand
                .builder()
                .name("TEST_OFFENSE")
                .minCharge(BigDecimal.valueOf(1000))
                .maxCharge(BigDecimal.valueOf(4000))
                .points(10)
                .build();
        String requestJson = objectMapper.writeValueAsString(
                command);
        //then
        postman.perform(post("/offense-dictionary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }
    @SneakyThrows
    @Test
    void shouldNotPostOffenseDictionaryWithExceedingMaxCharge() {
        offenseDictionaryService.saveEntry(new OffenseDictionary("TEST_OFFENSE", 10, BigDecimal.valueOf(1000), BigDecimal.valueOf(4000)));
        //given
        CreateOffenseDictionaryEntryCommand command = CreateOffenseDictionaryEntryCommand
                .builder()
                .name("TEST_OFFENSE")
                .minCharge(BigDecimal.valueOf(1000))
                .maxCharge(BigDecimal.valueOf(10000))
                .points(10)
                .build();
        String requestJson = objectMapper.writeValueAsString(
                command);
        //then
        postman.perform(post("/offense-dictionary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}