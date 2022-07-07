package pl.kurs.tickets.repository;

import liquibase.pro.packaged.O;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.tickets.model.Offense;

import java.util.Optional;

public interface OffenseRepository extends JpaRepository<Offense, Long> {
    @Query("select o from Offense o where o.ticket.id = ?1")
    Page<Offense> findByTicket_Id(Long id, Pageable pageable);


}