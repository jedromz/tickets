package pl.kurs.tickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.tickets.model.Offense;
import pl.kurs.tickets.model.OffenseDictionary;

import java.util.Optional;

public interface OffenseDictionaryRepository extends JpaRepository<OffenseDictionary, Long> {
    Optional<OffenseDictionary> findByName(String name);

}
