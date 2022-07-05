package pl.kurs.tickets.repository;

import liquibase.pro.packaged.O;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.tickets.model.Offense;

import java.util.Optional;

public interface OffenseRepository extends JpaRepository<Offense, Long> {


}