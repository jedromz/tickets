package pl.kurs.tickets.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import pl.kurs.tickets.model.OffenseDictionary;
import pl.kurs.tickets.repository.OffenseDictionaryRepository;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.kurs.tickets.error.EntityNotFoundException;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.command.CreatePersonCommand;
import pl.kurs.tickets.repository.PersonRepository;
import pl.kurs.tickets.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;


class OffenseDictionaryServiceTest {

    @Mock
    private OffenseDictionaryRepository offenseDictionaryRepository;
    private OffenseDictionaryService offenseDictionaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offenseDictionaryService = new OffenseDictionaryService(offenseDictionaryRepository);
    }

    @Test
    void chargeShouldBeWithinRange() {
        OffenseDictionary offenseDictionary = new OffenseDictionary("speeding", 15, BigDecimal.valueOf(1000), BigDecimal.valueOf(3000));
        when(offenseDictionaryRepository.findByName(any())).thenReturn(Optional.of(offenseDictionary));
        offenseDictionaryService.chargeIsWithinRage(offenseDictionary.getName(), BigDecimal.valueOf(2000));
        verify(offenseDictionaryRepository).findByName(any());
    }
}