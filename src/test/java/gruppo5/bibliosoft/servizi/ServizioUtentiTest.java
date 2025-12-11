package gruppo5.bibliosoft.servizi;

import gruppo5.bibliosoft.archivi.Archivio;
import gruppo5.bibliosoft.modelli.Utente;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServizioUtentiTest {

    private Archivio archivio;
    private ServizioUtenti servizioUtenti;
    private Utente utenteValido;

    @BeforeEach
    public void setUp() {
        archivio = new Archivio();
        servizioUtenti = new ServizioUtenti(archivio);
        utenteValido = new Utente("012345", "Mario", "Rossi", "m.rossi@studenti.unisa.it");
    }
    
    @Test
    public void testCostruttore() { //test del costruttore: verifica che il servizio venga istanziato
        assertNotNull(servizioUtenti, "Il servizio dovrebbe essere stato istanziato correttamente.");
    }

    @Test
    public void testAggiungiUtente() {   // test su aggiungiUtente(): utente null
        assertThrows(
                RuntimeException.class,
                () -> servizioUtenti.aggiungiUtente(null),
                "Mi aspettavo un'eccezione, ma non è stata lanciata."
        );
    }

    @Test
    public void testModificaUtente() {  // test su modificaUtente(): utente null
        assertThrows(
                RuntimeException.class,
                () -> servizioUtenti.modificaUtente(null),
                "Mi aspettavo un'eccezione, ma non è stata lanciata."
        );
    }

    @Test
    public void testListaUtenti() {     // test su listaUtenti(): archivio vuoto
        List<Utente> utenti = servizioUtenti.listaUtenti();
        
        assertNotNull(utenti, "La lista non dovvrebbe essere null.");
        assertEquals(0, utenti.size(), "L'archivio dovrebbe essere vuoto.");
    }

    @Test
    public void testCerca1() {           // test su cerca(): filtro null
        List<Utente> utenti = servizioUtenti.cerca(null);
        
        assertNotNull(utenti, "La lista non dovvrebbe essere null.");
        assertEquals(0, utenti.size(), "La lista dovrebbe essere vuota.");
    }

    @Test
    public void testCerca2() {   // test su cerca(): filtro vuoto
        List<Utente> utenti = servizioUtenti.cerca("");
        
        assertNotNull(utenti, "La lista non dovvrebbe essere null.");
        assertEquals(0, utenti.size(), "La lista dovrebbe essere vuota.");
    }

    @Test
    public void testEliminaUtente1() { //test di eliminaUtente(): verifica rimozione utente senza prestiti
        servizioUtenti.aggiungiUtente(utenteValido);

        servizioUtenti.eliminaUtente(utenteValido);

        assertEquals(0, servizioUtenti.getUtentiTotali(), "L'utente dovrebbe essere stato rimosso.");
    }

    @Test
    public void testEliminaUtente2() { //test di eliminaUtente(): verifica blocco eliminazione se ci sono prestiti
        servizioUtenti.aggiungiUtente(utenteValido);

        utenteValido.aggiungiPrestito(null);

        assertThrows(IllegalStateException.class, 
            () -> servizioUtenti.eliminaUtente(utenteValido),
            "Utente con prestiti attivi: eliminazione negata");       
    }
    
    @Test
    public void testGetUtentiTotali() {    // test su getUtentiTotali(): archivio vuoto
        assertEquals(0, servizioUtenti.getUtentiTotali(), "Dovrebbe essere 0.");
    }

    @Test
    public void testGetUtentiAttivi() {    // test su getUtentiAttivi(): archivio vuoto
        assertEquals(0, servizioUtenti.getUtentiAttivi(), "Dovrebbe essere 0.");
    }
}