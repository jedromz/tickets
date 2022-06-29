package pl.kurs.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.Ticket;
import pl.kurs.tickets.model.command.CreateTicketCommand;
import pl.kurs.tickets.service.PersonService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Slf4j
class TicketControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonService personService;
    private Person PERSON_1;
    private Ticket TICKET_1;
    List<String> OFFENSES;

    @BeforeEach
    void setUp() {
        TICKET_1 = new Ticket(LocalDate.now(), 5, BigDecimal.valueOf(1000));
        OFFENSES = new ArrayList<>(List.of("speeding", "drunk driving", "no seatbelt", "no license plate", "dangerous driving"));
        TICKET_1.setOffenses(OFFENSES);
    }

    @SneakyThrows
    @Test
    void shouldPostTicket() {
        //given
        Person PERSON_4 = new Person("95072267154", "TEST_FIRSTNAME", "TEST_LASTNAME", "test_int4@email.com");
        personService.savePerson(PERSON_4);
        CreateTicketCommand command = CreateTicketCommand
                .builder()
                .date(TICKET_1.getDate())
                .charge(TICKET_1.getCharge())
                .points(TICKET_1.getPoints())
                .offenses(TICKET_1.getOffenses())
                .pesel(PERSON_4.getPesel())
                .build();

        String requestJson = objectMapper.writeValueAsString(
                command);
        //when
        String response = postman.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String savedPesel = JsonPath.read(response, "pesel");
        //then
        postman.perform(get("/tickets/" + savedPesel))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].pesel").value(command.getPesel()))
                //.andExpect(jsonPath("$.content[*].offenses").value(command.getOffenses()))
                .andExpect(jsonPath("$.content[*].points").value(command.getPoints()))
                .andReturn();
    }

    @SneakyThrows
    @Test
    void shouldNotPostTicketWithWrongPesel() {
        //given
        Person PERSON_1 = new Person("51063085592", "TEST_FIRSTNAME", "TEST_LASTNAME", "test_int@email.com");
        personService.savePerson(PERSON_1);
        CreateTicketCommand command = CreateTicketCommand
                .builder()
                .date(TICKET_1.getDate())
                .charge(TICKET_1.getCharge())
                .points(TICKET_1.getPoints())
                .offenses(TICKET_1.getOffenses())
                .pesel("")
                .build();

        String requestJson = objectMapper.writeValueAsString(
                command);
        //when
        postman.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void pessimisticLockTest() {
        Person PERSON_2 = new Person("74070236984", "TEST_FIRSTNAME", "TEST_LASTNAME", "test_int2@email.com");
        personService.savePerson(PERSON_2);
        LocalDate date = LocalDate.now();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            CreateTicketCommand command = CreateTicketCommand.builder()
                    .date(date.plusDays(i))
                    .offenses(TICKET_1.getOffenses())
                    .charge(TICKET_1.getCharge())
                    .pesel(PERSON_2.getPesel())
                    .points(TICKET_1.getPoints())
                    .build();
            List<Future<HttpStatus>> futures = new ArrayList<>(10);
            List<HttpStatus> statuses = new ArrayList<>(10);
            for (int j = 0; j < 10; j++) {
                futures.add(executorService.submit(new RequestCall(command)));
            }
            for (int j = 0; j < 10; j++) {
                statuses.add(futures.get(j).get());
            }

            assertEquals(1, (statuses.stream().filter(HttpStatus::is2xxSuccessful)).count());
        }
    }

    @RequiredArgsConstructor
    static class RequestCall implements Callable<HttpStatus> {
        private final CreateTicketCommand command;

        @Override
        public HttpStatus call() throws Exception {
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<Void> re = restTemplate.postForEntity("http://localhost:8082/api/tickets", command, Void.class);
                log.info(re.getBody() + "");
                return re.getStatusCode();
            } catch (Exception exc) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}