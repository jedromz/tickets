package pl.kurs.tickets.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.kurs.tickets.model.Person;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long>, QuerydslPredicateExecutor<Person> {
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    @Query("select p from Person p left join fetch p.tickets where p.pesel = ?1")
    Optional<Person> findByPeselWithTickets(String pesel);


    @Query("select p from Person p left join fetch p.tickets where p.id = ?1")
    Optional<Person> findByIdWithTickets(Long id);

    @Query(value = "select p from Person p left join fetch p.tickets", countQuery = "select count(p) from Person p")
    Page<Person> findAllWithTickets(Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByPesel(String pesel);

}
