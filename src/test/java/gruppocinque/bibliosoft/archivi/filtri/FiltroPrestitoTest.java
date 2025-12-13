package gruppocinque.bibliosoft.archivi.filtri;

import gruppocinque.bibliosoft.archivi.filtri.FiltroPrestito;
import gruppocinque.bibliosoft.archivi.filtri.InterfacciaFiltro;
import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.modelli.Prestito;
import gruppocinque.bibliosoft.modelli.StatoPrestito;
import gruppocinque.bibliosoft.modelli.Utente;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class FiltroPrestitoTest {

    private Utente utente;
    private Libro libro;
    private LocalDate dataInizio;
    private LocalDate dataPrevista;
    private Prestito prestitoConcluso;
    private Prestito prestitoInCorso;
    private Prestito prestitoInRitardo;

    @BeforeEach
    public void setUp() {
        utente = new Utente("123", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        libro = new Libro("9788800000000", "Il Nome della Rosa", List.of("Umberto Eco"), 1980, 5);
        dataInizio= LocalDate.now();
        dataPrevista= LocalDate.now().plusDays(3);
        
        
        prestitoConcluso = new Prestito(utente,libro,dataInizio,dataPrevista);
        prestitoConcluso.setStato(StatoPrestito.CONCLUSO);

        prestitoInCorso = new Prestito(utente,libro,dataInizio,dataPrevista);
        prestitoInCorso.setStato(StatoPrestito.IN_CORSO);

        prestitoInRitardo = new Prestito(utente,libro,dataInizio,dataPrevista);
        prestitoInRitardo.setStato(StatoPrestito.IN_RITARDO);
    }

    @Test
    public void testFiltraConclusi() { //test di filtraConclusi(): controlla se identifica correttamente i prestiti conclusi
        InterfacciaFiltro<Prestito> filtro = FiltroPrestito.filtraConclusi();

        assertTrue(filtro.filtra(prestitoConcluso), "Dovrebbe accettare il prestito concluso.");
        assertFalse(filtro.filtra(prestitoInCorso), "Non dovrebbe accettare il prestito in corso.");
        assertFalse(filtro.filtra(prestitoInRitardo), "Non dovrebbe accettare il prestito in ritardo");
    }

    @Test
    public void testFiltraInCorso() { //test di filtraInCorso(): controlla se identifica correttamente i prestiti in corso
        InterfacciaFiltro<Prestito> filtro = FiltroPrestito.filtraInCorso();

        assertTrue(filtro.filtra(prestitoInCorso), "Dovrebbe accettare il prestito in corso.");
        assertFalse(filtro.filtra(prestitoInRitardo), "Non dovrebbe accettare il prestito in ritardo.");
        assertFalse(filtro.filtra(prestitoConcluso), "Non dovrebbe accettare il prestito concluso.");
    }

    @Test
    public void testFiltraInRitardo() { //test di filtraInRitardo(): controlla se identifica correttamente i prestiti in ritardo
        InterfacciaFiltro<Prestito> filtro = FiltroPrestito.filtraInRitardo();

        assertTrue(filtro.filtra(prestitoInRitardo), "Dovrebbe accettare il prestito in ritardo.");
        assertFalse(filtro.filtra(prestitoInCorso), "Non dovrebbe accettare il prestito in corso.");
        assertFalse(filtro.filtra(prestitoConcluso), "Non dovrebbe accettare il prestito concluso.");
    }

    @Test
    public void testFiltraAttivi() { //test di filtraAttivi(): controlla se accetta sia i prestiti in corso che quelli in ritardo
        InterfacciaFiltro<Prestito> filtro = FiltroPrestito.filtraAttivi();

        assertTrue(filtro.filtra(prestitoInCorso), "Dovrebbe accettare il prestito in corso (è attivo).");
        assertTrue(filtro.filtra(prestitoInRitardo), "Dovrebbe accettare il prestito in ritardo (è attivo).");
        assertFalse(filtro.filtra(prestitoConcluso), "Non dovrebbe accettare il prestito concluso.");
    }


    @Test
    public void testRicercaMatricola1() { //test di ricercaMatricola(): controlla se trova i prestiti associati a una specifica matricola
        InterfacciaFiltro<Prestito> filtro = FiltroPrestito.ricercaMatricola("123");

        assertTrue(filtro.filtra(prestitoInCorso), "Dovrebbe trovare il prestito dell'utente 123.");
        assertTrue(filtro.filtra(prestitoInRitardo), "Dovrebbe trovare il prestito dell'utente 123.");
        assertTrue(filtro.filtra(prestitoConcluso), "Dovrebbe trovare il prestito dell'utente 123.");
        
    }

    @Test
    public void testRicercaMatricola2() { //test di ricercaMatricola(): controlla se scarta i prestiti di altre matricole
        InterfacciaFiltro<Prestito> filtro = FiltroPrestito.ricercaMatricola("999");

        assertFalse(filtro.filtra(prestitoInCorso), "Non dovrebbe trovare prestiti di un'altra matricola.");
        assertFalse(filtro.filtra(prestitoInRitardo), "Non Dovrebbe trovare il prestito dell'utente 999.");
        assertFalse(filtro.filtra(prestitoConcluso), "Non Dovrebbe trovare il prestito dell'utente 999.");
    }


    @Test
    public void testRicercaAttiviMatricola1() { //test di ricercaAttiviMatricola(): caso positivo (Matricola corretta + Stato attivo) e caso negativo (Stato concluso)
        InterfacciaFiltro<Prestito> filtro = FiltroPrestito.ricercaAttiviMatricola("123");

        assertTrue(filtro.filtra(prestitoInCorso), "Dovrebbe accettare prestito attivo dell'utente corretto.");
        assertTrue(filtro.filtra(prestitoInRitardo), "Dovrebbe accettare prestito in ritardo dell'utente corretto.");
        assertFalse(filtro.filtra(prestitoConcluso), "Non dovrebbe accettare prestito concluso anche se la matricola è corretta.");
    }

    @Test
    public void testRicercaAttiviMatricola2() { //test di ricercaAttiviMatricola(): caso negativo (Stato Attivo MA Matricola errata)
        InterfacciaFiltro<Prestito> filtro = FiltroPrestito.ricercaAttiviMatricola("999");

        assertFalse(filtro.filtra(prestitoInCorso), "Non dovrebbe accettare prestito di un altro utente anche se è attivo.");
    }
}