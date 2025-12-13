package gruppocinque.bibliosoft.archivi;

import gruppocinque.bibliosoft.archivi.Archivio;
import gruppocinque.bibliosoft.archivi.filtri.InterfacciaFiltro;
import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.modelli.Prestito;
import gruppocinque.bibliosoft.modelli.Utente;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArchivioTest {

    private Archivio archivio;

    @BeforeEach
    public void setUp() { // Garantisce un ambiente pulito e isolato per ogni singolo test.
        archivio = new Archivio();
    }


    @Test
    public void testAggiungiLibro() { // Verifica che l'inserimento persista il dato e aggiorni il contatore.
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 1);
        archivio.aggiungiLibro(libro);
        assertEquals(1, archivio.contaLibri(), "Il conteggio libri dovrebbe essere 1 dopo l'inserimento.");
    }

    @Test
    public void testRimuoviLibro() { // Verifica la corretta pulizia dei dati e il decremento del contatore.
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 1);
        archivio.aggiungiLibro(libro);
        archivio.rimuoviLibro(libro);
        assertEquals(0, archivio.contaLibri(), "Il conteggio libri dovrebbe tornare a 0 dopo la rimozione.");
    }

    @Test
    public void testModificaLibro() { // Assicura che le modifiche ai campi dell'oggetto vengano salvate correttamente.
        Libro libro = new Libro("1234567890", "Titolo Vecchio", List.of("Mario Rossi"), 2020, 1);
        archivio.aggiungiLibro(libro);
        
        libro.setTitolo("Nuovo Titolo");
        archivio.modificaLibro(libro);
        
        assertEquals(1, archivio.contaLibri(), "Il conteggio deve restare invariato.");
        assertEquals("Nuovo Titolo", archivio.listaLibri().get(0).getTitolo(), "Il titolo dovrebbe essere aggiornato.");
    }

    @Test
    public void testListaLibri() { // Controlla che il metodo restituisca una collezione valida (mai null).
        assertNotNull(archivio.listaLibri(), "La lista libri non deve essere null.");
    }

    @Test
    public void testCercaLibri() { // Verifica la robustezza del metodo di ricerca anche senza filtri.
        assertNotNull(archivio.cercaLibri(null), "La ricerca non deve restituire null.");
    }

    @Test
    public void testContaLibri() { // Verifica lo stato iniziale coerente (archivio vuoto).
        assertEquals(0, archivio.contaLibri(), "L'archivio nuovo deve avere 0 libri.");
    }


    @Test
    public void testAggiungiUtente() { // Verifica l'inserimento di un utente valido nel sistema.
        Utente utente = new Utente("0512103456", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        archivio.aggiungiUtente(utente);
        
        assertEquals(1, archivio.contaUtenti(), "Il conteggio utenti dovrebbe essere 1.");
    }

    @Test
    public void testRimuoviUtente() { // Verifica che un utente venga eliminato definitivamente.
        Utente utente = new Utente("0512103456", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        archivio.aggiungiUtente(utente);
        archivio.rimuoviUtente(utente);
        
        assertEquals(0, archivio.contaUtenti(), "Il conteggio utenti dovrebbe tornare a 0.");
    }

    @Test
    public void testModificaUtente() { // Verifica l'effettivo aggiornamento dei dati anagrafici.
        Utente utente = new Utente("0512103456", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        archivio.aggiungiUtente(utente);
        
        utente.setNome("Luigi");
        archivio.modificaUtente(utente);
        
        assertEquals(1, archivio.contaUtenti(), "Il conteggio utenti deve restare invariato.");
        assertEquals("Luigi", archivio.listaUtenti().get(0).getNome(), "Il nome utente dovrebbe essere aggiornato.");
    }

    @Test
    public void testListaUtenti() { // Garantisce che la lista utenti sia istanziata correttamente.
        assertNotNull(archivio.listaUtenti(), "La lista utenti non deve essere null.");
    }

    @Test
    public void testContaUtenti() { // Assicura che un nuovo archivio parta da zero utenti.
        assertEquals(0, archivio.contaUtenti(), "L'archivio nuovo deve avere 0 utenti.");
    }

    @Test
    public void testCercaUtenti() { // Verifica che la ricerca restituisca risultati validi e gestisca il null.
        assertNotNull(archivio.cercaUtenti(null), "La ricerca utenti non deve restituire null.");
    }

    @Test
    public void testAggiungiPrestito() { // Verifica la registrazione di un nuovo movimento di prestito.
        Utente u = new Utente("0512103456", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        Libro l = new Libro("1234567890", "Titolo", List.of("Autore"), 2020, 1);
        Prestito p = new Prestito(u, l, LocalDate.now(), LocalDate.now().plusDays(30));
        
        archivio.aggiungiPrestito(p);
        
        assertEquals(1, archivio.contaPrestiti(), "Il conteggio prestiti dovrebbe essere 1.");
    }

    @Test
    public void testRimuoviPrestito() { // Verifica la cancellazione di un prestito dallo storico.
        Utente u = new Utente("0512103456", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        Libro l = new Libro("1234567890", "Titolo", List.of("Autore"), 2020, 1);
        Prestito p = new Prestito(u, l, LocalDate.now(), LocalDate.now().plusDays(30));
        
        archivio.aggiungiPrestito(p);
        archivio.rimuoviPrestito(p);
        
        assertEquals(0, archivio.contaPrestiti(), "Il conteggio prestiti dovrebbe tornare a 0.");
    }

    @Test
    public void testModificaPrestito() { // Verifica l'aggiornamento dello stato (es. restituzione effettiva).
        Utente u = new Utente("0512103456", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        Libro l = new Libro("1234567890", "Titolo", List.of("Autore"), 2020, 1);
        Prestito p = new Prestito(u, l, LocalDate.now(), LocalDate.now().plusDays(30));
        
        archivio.aggiungiPrestito(p);
        
        p.setDataRestituzioneEffettiva(LocalDate.now());
        archivio.modificaPrestito(p);
        
        assertEquals(1, archivio.contaPrestiti(), "Il conteggio prestiti deve restare invariato.");
        assertNotNull(archivio.listaPrestiti().get(0).getDataRestituzioneEffettiva(), "La data di restituzione dovrebbe essere stata aggiornata.");
    }

    @Test
    public void testListaPrestiti() { // Controlla l'integrità della lista dei prestiti.
        assertNotNull(archivio.listaPrestiti(), "La lista prestiti non deve essere null.");
    }

    @Test
    public void testContaPrestiti() { // Verifica l'inizializzazione corretta del contatore prestiti.
        assertEquals(0, archivio.contaPrestiti(), "L'archivio nuovo deve avere 0 prestiti.");
    }
    
    @Test
    public void testCercaPrestiti() { // Verifica che il motore di ricerca prestiti sia attivo.
        assertNotNull(archivio.cercaPrestiti(null), "La ricerca prestiti non deve restituire null.");
    }
}