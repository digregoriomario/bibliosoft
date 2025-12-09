package gruppo5.bibliosoft.strumenti;

import gruppo5.bibliosoft.modelli.Libro;
import gruppo5.bibliosoft.modelli.Utente;
import java.time.Year;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidatoreTest {

    public ValidatoreTest() {
    }

    @Test
    public void testValidaLibro1() { //test di validaLibro(): controlla se un libro valido viene accettato senza eccezioni
        Libro libro = new Libro("1234567890", "Titolo Valido", List.of("Mario Rossi"), 2020, 5);

        assertDoesNotThrow(() -> Validatore.validaLibro(libro), "Il libro valido non dovrebbe sollevare eccezioni.");
    }

    @Test
    public void testValidaLibro2() { //test di validaLibro(): controlla eccezione se ISBN è null
        Libro libro = new Libro(null, "Titolo", List.of("Autore"), 2020, 5);

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaLibro(libro), "Il libro ha il campo isbn pari a null quindi solleva eccezioni");
    }

    @Test
    public void testValidaLibro3() { //test di validaLibro(): controlla eccezione se ISBN è vuoto
        Libro libro = new Libro("", "Titolo", List.of("Autore"), 2020, 5);

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaLibro(libro), "Il libro ha il campo isbn è vuoto quindi solleva eccezioni");
    }

    @Test
    public void testValidaLibro4() { //test di validaLibro(): controlla eccezione se ISBN contiene caratteri non numerici
        Libro libro = new Libro("12345X7890", "Titolo", List.of("Autore"), 2020, 5);

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaLibro(libro), "Il libro ha il campo isbn contenente caratteri non numerici quindi solleva eccezioni");
    }

    @Test
    public void testValidaLibro5() { //test di validaLibro(): controlla eccezione se lunghezza ISBN diversa da 10 o 13
        Libro libro = new Libro("123", "Titolo", List.of("Autore"), 2020, 5);

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaLibro(libro), "Il libro ha il campo isbn contenente meno di 10 o 13 caratteri quindi solleva eccezioni");

    }

    @Test
    public void testValidaLibro6() { //test di validaLibro(): controlla eccezione se Titolo è vuoto
        Libro libro = new Libro("1234567890", "", List.of("Autore"), 2020, 5);

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaLibro(libro), "Il libro ha il campo titolo è vuoto quindi solleva eccezioni");

    }

    @Test
    public void testValidaLibro7() { //test di validaLibro(): controlla eccezione se copie totali sono <= 0
        Libro libro = new Libro("1234567890", "Titolo", List.of("Autore"), 2020, 0);

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaLibro(libro), "Il libro ha il campo copie totali <= 0 quindi solleva eccezioni");
    }

    @Test
    public void testValidaLibro8() { //test di validaLibro(): controlla eccezione se l'anno è nel futuro
        int anno = Year.now().getValue() + 1;
        Libro libro = new Libro("1234567890", "Titolo", List.of("Autore"), anno, 5);

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaLibro(libro), "Il libro ha il campo annoPubblicazione è maggione rispetto l'anno presenre quindi solleva eccezioni");

    }

    @Test
    public void testValidaLibro9() { //test di validaLibro(): controlla eccezione se un autore nella lista è vuoto
        Libro libro = new Libro("1234567890", "Titolo", List.of(""), 2020, 5);

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaLibro(libro), "Il libro ha il campo autore è vuoto quindi solleva eccezioni");

    }

    @Test
    public void testValidaLibro10() { //test di validaLibro(): controlla eccezione se un autore contiene caratteri non validi (es. numeri)
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario 123"), 2020, 5);

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaLibro(libro), "Il libro ha il campo autore non ha caratteri validi quindi solleva eccezioni");

    }

    @Test
    public void testValidaUtente1() { //test di validaUtente(): controlla se un utente valido viene accettato senza eccezioni
        Utente utente = new Utente("012345", "Mario", "Rossi", "m.rossi@studenti.unisa.it");

        assertDoesNotThrow(() -> Validatore.validaUtente(utente), "L'utente valido non dovrebbe sollevare eccezioni.");
    }

    @Test
    public void testValidaUtente2() { //test di validaUtente(): controlla eccezione se la matricola è vuota
        Utente utente = new Utente("", "Mario", "Rossi", "m.rossi@studenti.unisa.it");

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaUtente(utente), "L'utente ha il campo matricola vuoto quindi solleva eccezioni.");

    }

    @Test
    public void testValidaUtente3() { //test di validaUtente(): controlla eccezione se la matricola contiene lettere
        Utente utente = new Utente("AB123", "Mario", "Rossi", "m.rossi@studenti.unisa.it");

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaUtente(utente), "L'utente ha il campo matricola che contiene lettere quindi solleva eccezioni.");

    }

    @Test
    public void testValidaUtente4() { //test di validaUtente(): controlla eccezione se il nome è vuoto
        Utente utente = new Utente("012345", "", "Rossi", "m.rossi@studenti.unisa.it");

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaUtente(utente), "L'utente ha il campo nome vuoto quindi solleva eccezioni.");

    }

    @Test
    public void testValidaUtente5() { //test di validaUtente(): controlla eccezione se il nome contiene numeri
        Utente utente = new Utente("012345", "Mario123", "Rossi", "m.rossi@studenti.unisa.it");

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaUtente(utente), "L'utente ha il campo nome contenente dei numeri quindi solleva eccezioni.");

    }

    @Test
    public void testValidaUtente6() { //test di validaUtente(): controlla eccezione se il cognome è vuoto
        Utente utente = new Utente("012345", "Mario", "", "m.rossi@studenti.unisa.it");

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaUtente(utente), "L'utente ha il campo cognome vuoto quindi solleva eccezioni.");
    }

    @Test
    public void testValidaUtente7() { //test di validaUtente(): controlla eccezione se il cognome contiene simboli o numeri
        Utente utente = new Utente("012345", "Mario", "Rossi_1", "m.rossi@studenti.unisa.it");

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaUtente(utente), "L'utente ha il campo cognome contenente simboli e/o numeri quindi solleva eccezioni.");
    }

    @Test
    public void testValidaUtente8() { //test di validaUtente(): controlla eccezione se l'email è vuota
        Utente utente = new Utente("012345", "Mario", "Rossi", "");

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaUtente(utente), "L'utente ha il campo email vuoto quindi solleva eccezioni.");
    }

    @Test
    public void testValidaUtente9() { //test di validaUtente(): controlla eccezione se l'email non appartiene al dominio corretto
        Utente utente = new Utente("012345", "Mario", "Rossi", "mario.rossi@gmail.com");

        assertThrows(IllegalArgumentException.class, () -> Validatore.validaUtente(utente), "L'utente ha il campo email ha un dominio diverso da 'studenti.unisa.it' quindi solleva eccezioni.");
    }
}
