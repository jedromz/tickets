package pl.kurs.tickets.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.kurs.tickets.model.Ticket;

import java.time.LocalDate;

public interface TicketRepository extends JpaRepository<Ticket, Long>, QuerydslPredicateExecutor<Ticket> {

//    left join fetch o.ticket t where t.person.pesel = ?1 and t.date between ?2 and ?3 and t.deleted = false
    @Query("select sum(od.points) from Offense o join o.offenseDictionary od  join  o.ticket t where t.person.pesel = ?1 and t.date between ?2 and ?3 and t.deleted = false ")
    Integer sumPointsByPeselAndDateBetween(String pesel,LocalDate dateStart, LocalDate dateEnd);

    @Query(value = "select t from Ticket t left join fetch t.offenses where t.person.pesel = ?1",countQuery = "select count(t) from Ticket t")
    Page<Ticket> findByPerson_Pesel(String pesel, Pageable pageable);


//
//    @Query("select t from Ticket t left join fetch Offense o left join fetch Person p where t.person.pesel = ?1")
//    Page<Ticket> findByPerson_Pesel(String pesel, Pageable pageable);


}
