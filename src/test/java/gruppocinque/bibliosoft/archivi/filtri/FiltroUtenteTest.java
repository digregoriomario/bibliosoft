package gruppocinque.bibliosoft.archivi.filtri;

import gruppocinque.bibliosoft.archivi.filtri.FiltroUtente;
import gruppocinque.bibliosoft.archivi.filtri.InterfacciaFiltro;
import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.modelli.Prestito;
import gruppocinque.bibliosoft.modelli.Utente;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class FiltroUtenteTest {

    private Utente utente;
    private Utente utenteConPrestiti;
    private Libro libro;
    private LocalDate dataInizio;
    private LocalDate dataPrevista;

    @BeforeEach
    public void setUp() {
        libro = new Libro("9788800000000", "Il Nome della Rosa", List.of("Umberto Eco"), 1980, 5);
        dataInizio= LocalDate.now();
        dataPrevista= LocalDate.now().plusDays(3);
        utente = new Utente("123", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");

        utenteConPrestiti = new Utente("999", "Luigi", "Verdi", "luigi.verdi@studenti.unisa.it");
        
        
        utenteConPrestiti.aggiungiPrestito(new Prestito(utente,libro,dataInizio,dataPrevista)); 
    }

    @Test
    public void testRicercaMatricola1() { //test di ricercaMatricola(): controlla se trova l'utente con matricola esatta
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricercaMatricola("123");

        assertTrue(filtro.filtra(utente), "Dovrebbe trovare l'utente con la matricola esatta.");
    }

    @Test
    public void testRicercaMatricola2() { //test di ricercaMatricola(): controlla se scarta matricole diverse
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricercaMatricola("000");

        assertFalse(filtro.filtra(utente), "Non dovrebbe trovare l'utente con matricola diversa.");
    }

    @Test
    public void testRicerca1() { //test di ricerca(): controlla se trova l'utente tramite il cognome
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricerca("Rossi");

        assertTrue(filtro.filtra(utente), "Dovrebbe trovare l'utente cercando il cognome.");
    }

    @Test
    public void testRicerca2() { //test di ricerca(): controlla se trova l'utente tramite parte del cognome
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricerca("Ros");

        assertTrue(filtro.filtra(utente), "Dovrebbe trovare l'utente cercando parte del cognome.");
    }

    @Test
    public void testRicerca3() { //test di ricerca(): controlla se trova l'utente tramite cognome ignorando il case
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricerca("rossi");

        assertTrue(filtro.filtra(utente), "Dovrebbe trovare l'utente ignorando il case del cognome.");
    }

    @Test
    public void testRicerca4() { //test di ricerca(): controlla se trova l'utente tramite parte della matricola
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricerca("12");

        assertTrue(filtro.filtra(utente), "Dovrebbe trovare l'utente cercando parte della matricola.");
    }

    @Test
    public void testRicerca5() { //test di ricerca(): controlla che restituisca false se la stringa non è né nel cognome né nella matricola
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricerca("Bianchi");

        assertFalse(filtro.filtra(utente), "Non dovrebbe trovare nessun utente.");
    }
    
    @Test
    public void testRicerca6() { //test di ricerca(): conferma che la ricerca non avviene per nome
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricerca("Mario");

        assertFalse(filtro.filtra(utente), "La ricerca generica (nel codice attuale) non cerca per nome, quindi dovrebbe essere false.");
    }

    @Test
    public void testRicercaUtentiAttivi1() { //test di ricercaUtentiAttivi(): controlla se trova un utente che ha prestiti in corso
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricercaUtentiAttivi();

        assertTrue(filtro.filtra(utenteConPrestiti), "Dovrebbe accettare l'utente che ha prestiti nella lista.");
    }

    @Test
    public void testRicercaUtentiAttivi2() { //test di ricercaUtentiAttivi(): controlla se scarta un utente che non ha prestiti
        InterfacciaFiltro<Utente> filtro = FiltroUtente.ricercaUtentiAttivi();

        assertFalse(filtro.filtra(utente), "Non dovrebbe accettare l'utente che ha la lista prestiti vuota.");
    }
}