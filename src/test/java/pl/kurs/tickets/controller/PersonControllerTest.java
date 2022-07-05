package pl.kurs.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

//    @SneakyThrows
//    @Test
//    void shouldPostPerson() {
//        //given
//        CreatePersonCommand command = CreatePersonCommand.builder()
//                .pesel(PERSON_1.getPesel())
//                .firstname(PERSON_1.getFirstname())
//                .lastname(PERSON_1.getLastname())
//                .email(PERSON_1.getEmail())
//                .build();
//
//        String requestJson = objectMapper.writeValueAsString(
//                command);
//        //when
//        String response = postman.perform(post("/people")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isCreated())
//                .andReturn().getResponse().getContentAsString();
//        int savedId = JsonPath.read(response, "id");
//        //then
//        postman.perform(get("/people/" + savedId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.pesel").value(command.getPesel()))
//                .andExpect(jsonPath("$.firstname").value(command.getFirstname()))
//                .andExpect(jsonPath("$.lastname").value(command.getLastname()))
//                .andExpect(jsonPath("$.email").value(command.getEmail()))
//                .andReturn();
//        personService.deletePersonById((long) savedId);
//    }
//
//    @SneakyThrows
//    @Test
//    void shouldNotPostPersonWithExistingPesel() {
//        Person savedPerson = personService.savePerson(PERSON_1);
//        //given
//        CreatePersonCommand command = CreatePersonCommand.builder()
//                .pesel(PERSON_1.getPesel())
//                .firstname(PERSON_1.getFirstname())
//                .lastname(PERSON_1.getLastname())
//                .email(PERSON_1.getEmail())
//                .build();
//
//        String requestJson = objectMapper.writeValueAsString(
//                command);
//        //when
//        postman.perform(post("/people")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isBadRequest());
//        personService.deletePerson(savedPerson);
//    }
//
//    @SneakyThrows
//    @Test
//    void shouldSoftDeletePersonById() {
//        //given
//        Person savedPerson = personService.savePerson(PERSON_1);
//        //when
//        String response = postman.perform(delete("/people/" + savedPerson.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent())
//                .andReturn().getResponse().getContentAsString();
//        //then
//        postman.perform(get("/people/" + savedPerson.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.deleted").value(true))
//                .andExpect(status().isOk());
//        personService.deletePerson(savedPerson);
//    }
//
//    @Test
//    void putPersonById() {
//    }


}