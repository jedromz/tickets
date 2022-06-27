package pl.kurs.tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketsApplication.class, args);
    }

}
/*
Stworz prosta aplikacje ktora pozwala rejestrowac wykroczenia drogowe.
Kazde wykroczenie ma: nr pesel ukranego, date, przewinienie, ilosc punktow, ilosc pln.
nr pesel musi byc powiazany z bazą danych osób którą będziesz miał w programie.
każda osoba ma: pesel, imie, nazwisko, email.

- nie mozna dodac wykroczenia w ktorym pesel nie wystepuje w naszej bazie osob ok
- nie mozna dodac wykroczenia ktorego data jest w przyszlosci. ok
- nie mozna dodac wykroczenia ktore ma wiecej niz 15 punktow ok
- nie mozna dodac wykroczenia ktore ma wiecej niz 5000 pln madatu. ok

podczas dodawania nowego wykroczenia jesli suma przekroczonych punktow za wszystkie wykroczenia w ostatnim roku jest wieksza niz 24, to do obwinionego wysylany jest email
z informacja ze stracil prawojazdy.
 */