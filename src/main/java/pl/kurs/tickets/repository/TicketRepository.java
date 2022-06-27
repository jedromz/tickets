package pl.kurs.tickets.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.tickets.model.Ticket;

import java.time.LocalDate;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    @Query("select sum(t.points) from Ticket t where  t.person.pesel = ?1 and t.date between ?2 and ?3 ")
    Integer sumPointsByPeselAndDateBetween(String pesel, LocalDate dateStart, LocalDate dateEnd);

    Page<Ticket> findByPerson_Pesel(String pesel, Pageable pageable);

}
