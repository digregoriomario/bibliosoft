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

    @BeforeEach
    public void setUp() {
        archivio = new Archivio();
        servizioUtenti = new ServizioUtenti(archivio);
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
    public void testCerca() {           // test su cerca(): filtro null
        List<Utente> utenti = servizioUtenti.cerca(null);
        
        assertNotNull(utenti, "La lista non dovvrebbe essere null.");
        assertEquals(0, utenti.size(), "La lista dovrebbe essere vuota.");
    }

    @Test
    public void testCercaFiltroVuoto() {   // test su cerca(): filtro vuoto
        List<Utente> utenti = servizioUtenti.cerca("");
        
        assertNotNull(utenti, "La lista non dovvrebbe essere null.");
        assertEquals(0, utenti.size(), "La lista dovrebbe essere vuota.");
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
