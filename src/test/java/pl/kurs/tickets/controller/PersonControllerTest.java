package pl.kurs.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.tickets.TicketsApplication;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.command.CreatePersonCommand;
import pl.kurs.tickets.service.PersonService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TicketsApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PersonControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonService personService;
    private Person PERSON_1;

    @BeforeEach
    void setUp() {
        PERSON_1 = new Person("64062278252", "TEST_FIRSTNAME", "TEST_LASTNAME", "test_1@email.com");
    }

    @SneakyThrows
    @Test
    void shouldPostPerson() {
        //given
        CreatePersonCommand command = CreatePersonCommand.builder()
                .pesel(PERSON_1.getPesel())
                .firstname(PERSON_1.getFirstname())
                .lastname(PERSON_1.getLastname())
                .email(PERSON_1.getEmail())
                .build();

        String requestJson = objectMapper.writeValueAsString(
                command);
        //when
        String response = postman.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int savedId = JsonPath.read(response, "id");
        //then
        postman.perform(get("/people/" + savedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesel").value(command.getPesel()))
                .andExpect(jsonPath("$.firstname").value(command.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(command.getLastname()))
                .andExpect(jsonPath("$.email").value(command.getEmail()))
                .andReturn();
    }
    @SneakyThrows
    @Test
    void shouldNotPostPersonWithExistingPesel() {
        //given
        CreatePersonCommand command = CreatePersonCommand.builder()
                .pesel(PERSON_1.getPesel())
                .firstname(PERSON_1.getFirstname())
                .lastname(PERSON_1.getLastname())
                .email(PERSON_1.getEmail())
                .build();

        String requestJson = objectMapper.writeValueAsString(
                command);
        //when
        postman.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePersonById() {
    }

    @Test
    void putPersonById() {
    }

    @Test
    void getPersonByPesel() {
    }

    @Test
    void testGetPersonByPesel() {
    }
}