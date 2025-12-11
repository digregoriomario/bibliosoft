package gruppo5.bibliosoft.archivi;

import gruppo5.bibliosoft.modelli.Utente;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SottoarchivioTest {

    private Sottoarchivio<Utente> instance;

    @BeforeEach
    public void setUp() { // Garantisce un ambiente pulito e isolato (archivio vuoto) per ogni test.
        instance = new Sottoarchivio<>();
    }

    @Test
    public void testAggiungi() { // Verifica che un nuovo utente venga aggiunto e conservato correttamente.
        Utente utente = new Utente("0512101234", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        instance.aggiungi(utente);
        
        assertEquals(1, instance.conta(), "Il conteggio deve essere 1 dopo l'inserimento dell'utente.");
        assertTrue(instance.lista().contains(utente), "L'utente aggiunto deve essere presente nella lista.");
    }

    @Test
    public void testRimuovi() { // Verifica la corretta rimozione dell'oggetto e l'aggiornamento del contatore.
        Utente utente = new Utente("0512101234", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        instance.aggiungi(utente);
        
        instance.rimuovi(utente);
        
        assertEquals(0, instance.conta(), "Il conteggio deve tornare a 0 dopo la rimozione.");
        assertFalse(instance.lista().contains(utente), "L'utente non deve più essere presente in archivio.");
    }

    @Test
    public void testModifica() { // Verifica l'effettivo aggiornamento delle informazioni dell'utente.
        Utente utente = new Utente("0512101234", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        instance.aggiungi(utente);
        
        utente.setNome("Luigi");

        instance.modifica(utente);
        
        assertEquals(1, instance.conta(), "Il numero di utenti deve restare invariato.");
        assertEquals("Luigi", instance.lista().get(0).getNome(), "Il nome dell'utente deve risultare aggiornato nella lista.");
    }

    @Test
    public void testLista() { // Controlla che il metodo restituisca una collezione valida di oggetti Utente.
        Utente u1 = new Utente("0512100001", "A", "B", "a.b@studenti.unisa.it");
        instance.aggiungi(u1);
        
        List<Utente> risultato = instance.lista();
        
        assertNotNull(risultato, "La lista restituita non deve mai essere null.");
        assertEquals(1, risultato.size(), "La dimensione della lista deve corrispondere agli inserimenti.");
        assertEquals(u1, risultato.get(0), "L'elemento nella lista deve corrispondere a quello aggiunto.");
    }

    @Test
    public void testCerca() { // Verifica il funzionamento del filtro su oggetti complessi e la gestione del null.
        Utente u1 = new Utente("0512100001", "Franck", "Rossi", "m.rossi@studenti.unisa.it");
        Utente u2 = new Utente("0512100002", "Angela", "Verdi", "l.verdi@studenti.unisa.it");
        instance.aggiungi(u1);
        instance.aggiungi(u2);

        List<Utente> tutti = instance.cerca(null);
        assertEquals(2, tutti.size(), "La ricerca con filtro null deve restituire tutti gli utenti.");

        List<Utente> filtrati = instance.cerca(u -> u.getNome().equals("Franck"));
        
        assertEquals(1, filtrati.size(), "Il filtro dovrebbe trovare esattamente 1 utente.");
        assertEquals("0512100001", filtrati.get(0).getMatricola(), "L'utente trovato deve essere quello corretto (Mario).");
    }

    @Test
    public void testConta() { // Assicura che un nuovo sottoarchivio parta da zero elementi.
        assertEquals(0, instance.conta(), "Un nuovo sottoarchivio deve partire da 0 utenti.");
    }
  
   @Test
    public void testOrdinamento() { // Verifica che il Sottoarchivio mantenga gli elementi ordinati (TreeSet).
        Utente u1 = new Utente("002", "Mario", "Rossi", "z.rossi@studenti.unisa.it");
        Utente u2 = new Utente("001", "Pippo", "Graziuso", "a.graziuso@studenti.unisa.it");        
      
        instance.aggiungi(u1);
        instance.aggiungi(u2);
        
        assertEquals("Graziuso", instance.lista().get(0).getCognome(), "Il primo elemento dovrebbe essere Graziuso (ordine alfabetico).");
        assertEquals("Rossi", instance.lista().get(1).getCognome(), "Il secondo elemento dovrebbe essere Rossi.");
    }
}