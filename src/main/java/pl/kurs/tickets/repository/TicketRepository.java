package pl.kurs.tickets.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.tickets.model.Ticket;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("select sum(t.points) from Ticket t where  t.person.pesel = ?1 and t.date between ?2 and ?3 and t.deleted = false")
    Integer sumPointsByPeselAndDateBetween(String pesel, LocalDate dateStart, LocalDate dateEnd);

    Page<Ticket> findByPerson_Pesel(String pesel, Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_READ)
    boolean existsByPerson_PeselAndDateAndPointsAndChargeAndOffensesIn(String pesel, LocalDate date, Integer points, BigDecimal charge, Collection<String> offenses);


}
