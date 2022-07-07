package pl.kurs.tickets.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.tickets.error.EntityNotFoundException;
import pl.kurs.tickets.model.OffenseDictionary;
import pl.kurs.tickets.model.command.CreateOffenseDictionaryEntryCommand;
import pl.kurs.tickets.model.command.UpdateOffenseDictionaryEntryCommand;
import pl.kurs.tickets.repository.OffenseDictionaryRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OffenseDictionaryService {
    private final OffenseDictionaryRepository offenseDictionaryRepository;

    @Transactional(readOnly = true)
    public boolean chargeIsWithinRage(String name, BigDecimal charge) {
        OffenseDictionary offenseDictEntry = getByName(name);
        BigDecimal minCharge = offenseDictEntry.getMinCharge();
        BigDecimal maxCharge = offenseDictEntry.getMaxCharge();
        return charge.compareTo(minCharge) >= 0 && charge.compareTo(maxCharge) <= 0;
    }

    @Transactional
    public OffenseDictionary saveEntry(CreateOffenseDictionaryEntryCommand command) {
        OffenseDictionary entry = new OffenseDictionary(command.getName(), command.getPoints(), command.getMinCharge(), command.getMaxCharge());
        return offenseDictionaryRepository.saveAndFlush(entry);
    }

    @Transactional
    public void deleteByName(String name) {
        OffenseDictionary offenseDictionary = getByName(name);
        offenseDictionary.setDeleted(true);
    }

    @Transactional(readOnly = true)
    public OffenseDictionary getByName(String name) {
        return offenseDictionaryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("OFFENSE_DICT_NAME", name));
    }

    @Transactional
    public OffenseDictionary editByName(String name, UpdateOffenseDictionaryEntryCommand command) {
        OffenseDictionary offenseDictionary = getByName(name);
        offenseDictionary.setName(command.getName());
        offenseDictionary.setMaxCharge(command.getMaxCharge());
        offenseDictionary.setMinCharge(command.getMinCharge());
        offenseDictionary.setPoints(command.getPoints());
        return offenseDictionary;
    }

    @Transactional
    public OffenseDictionary saveEntry(OffenseDictionary offenseDictionary) {
        return offenseDictionaryRepository.saveAndFlush(offenseDictionary);
    }
}
