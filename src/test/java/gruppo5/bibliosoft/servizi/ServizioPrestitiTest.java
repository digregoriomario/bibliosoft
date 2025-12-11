package gruppo5.bibliosoft.servizi;

import gruppo5.bibliosoft.archivi.Archivio;
import gruppo5.bibliosoft.modelli.Libro;
import gruppo5.bibliosoft.modelli.Prestito;
import gruppo5.bibliosoft.modelli.StatoPrestito;
import gruppo5.bibliosoft.modelli.Utente;
import gruppo5.bibliosoft.archivi.filtri.FiltroPrestito;
import gruppo5.bibliosoft.archivi.filtri.InterfacciaFiltro;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ServizioPrestitiTest {

    private Archivio archivio;
    private ServizioPrestiti servizio;
    private Libro libro;
    private Utente utente;

    @BeforeEach
    public void setUp() {
        archivio = new Archivio();
        servizio = new ServizioPrestiti(archivio);
        
        utente = new Utente("123", "Mario", "Rossi", "m.rossi@studenti.unisa.it");
        libro = new Libro("1234567890", "Libro Test", List.of("Autore"), 2020, 5);
    }
    
    @Test
    public void testCostruttore() { //test del costruttore: verifica che il servizio venga inizializzato
        assertNotNull(servizio, "Il servizio dovrebbe essere stato creato correttamente.");
    }

    @Test
    public void testLista() {   //test di lista(): su archivio vuoto
        List<Prestito> lista = servizio.lista();
        
        assertNotNull(lista, "La lista non dovrebbe essere null.");
        assertEquals(0, lista.size(), "La dimensione della lista dovrebbe essere 0.");
    }

    @Test
    public void testCerca() { //test di cerca(): verifica il funzionamento con un filtro generico
        servizio.registraPrestito(utente, libro, LocalDate.now().plusDays(15));
        
        InterfacciaFiltro<Prestito> filtroTutto = prestito -> true;
        
        List<Prestito> risultato = servizio.cerca(filtroTutto);
        assertEquals(1, risultato.size(), "Il filtro dovrebbe trovare il prestito.");
    }

    @Test
    public void testStorico() { //test di storico(): verifica che ritorni solo i prestiti di uno specifico utente
        Utente altroUtente = new Utente("999", "Luigi", "Verdi", "l.verdi@unisa.it");
        archivio.aggiungiUtente(altroUtente);
        Libro altroLibro = new Libro("111", "Altro", List.of("B"), 2021, 5);

        // Prestito utente 1
        servizio.registraPrestito(utente, libro, LocalDate.now().plusDays(15));
        // Prestito utente 2
        servizio.registraPrestito(altroUtente, altroLibro, LocalDate.now().plusDays(15));

        List<Prestito> storicoUtente1 = servizio.storico(utente);

        assertEquals(1, storicoUtente1.size(), "Lo storico deve contenere solo i prestiti dell'utente specificato.");
        assertEquals("123", storicoUtente1.get(0).getUtente().getMatricola(), "La matricola del prestito deve corrispondere.");
    }
    
    @Test
    public void testGetPrestitiInCorso() {  //test di getPrestitiInCorso(): su archivio vuoto
        assertEquals(0, servizio.getPrestitiInCorso(), "Dovrebbe restituire 0.");
    }

    @Test
    public void testRegistraPrestito1() {   //test di registraPrestito(): su un libro con 0 copie disponibili
        Utente utente = new Utente("123", "Mario", "Rossi", "m@studenti.unisa.it");
        Libro libro = new Libro("1234567890", "Titolo", List.of("Umberto Eco"), 2020, 1);
        libro.setCopieDisponibili(0);


        assertThrows(
                IllegalStateException.class,
                () -> servizio.registraPrestito(utente, libro, LocalDate.now().plusDays(7)),
                "Mi aspettavo IllegalStateException per copie non disponibili."
        );
    }
    
    @Test
    public void testRegistraPrestito2() {   //test di registraPrestito(): su un utente con 3 prestiti già attivi (limite)
        Utente utente = new Utente("123", "Mario", "Rossi", "m@studenti.unisa.it");
        Libro l1 = new Libro("1111111111", "Titolo1", List.of("Umberto Eco"), 2020, 1);
        Libro l2 = new Libro("2222222222", "Titolo2", List.of("Umberto Eco"), 2021, 1);
        Libro l3 = new Libro("3333333333", "Titolo3", List.of("Umberto Eco"), 2022, 1);
        Libro l4 = new Libro("4444444444", "Titolo4", List.of("Umberto Eco"), 2023, 1);

        servizio.registraPrestito(utente, l1, LocalDate.now().plusDays(7));
        servizio.registraPrestito(utente, l2, LocalDate.now().plusDays(7));
        servizio.registraPrestito(utente, l3, LocalDate.now().plusDays(7));

        assertThrows(
                IllegalStateException.class,
                () -> servizio.registraPrestito(utente, l4, LocalDate.now().plusDays(7)),
                "Mi aspettavo IllegalStateException per utente con già 3 prestiti attivi."
        );
    }

    @Test
    public void testRegistraPrestito3() {    //testi di registraPrestito(): verifica delle postcondizioni
        Utente utente = new Utente("123", "Mario", "Rossi", "m@studenti.unisa.it");
        Libro libro = new Libro("1111111111", "Titolo1", List.of("Umberto Eco"), 2020, 2);

        int disponibiliPrima = libro.getCopieDisponibili();

        servizio.registraPrestito(utente, libro, LocalDate.now().plusDays(7));

        assertEquals(1, archivio.listaPrestiti().size(), "L'archivio dovrebbe contenere un prestito.");
        assertEquals(disponibiliPrima - 1, libro.getCopieDisponibili(), "Il libro dovrebbe avere " + (disponibiliPrima - 1) + 
         " copie disponibili.");
        assertEquals(1, utente.getPrestitiAttivi().size(), "L'utente dovrebbe avere 1 prestito attivo.");
    }
    
    
    @Test
    public void testRegistraRestituzione1() {    //test di registraRestituzione(): su prestito già concluso
        Utente utente = new Utente("123", "Mario", "Rossi", "m@studenti.unisa.it");
        Libro libro = new Libro("1111111111", "Titolo1", List.of("Umberto Eco"), 2020, 1);

        Prestito prestito = new Prestito(utente, libro, LocalDate.now().minusDays(3), LocalDate.now().minusDays(1));
        prestito.setStato(StatoPrestito.CONCLUSO);

        assertThrows(
                IllegalStateException.class,
                () -> servizio.registraRestituzione(prestito),
                "Mi aspettavo IllegalStateException per prestito già concluso."
        );
    }
    
    
    @Test
    public void testRegistraRestituzione2() {   //test di registraRestituzione(): test delle postcondizioni
        Utente utente = new Utente("123", "Mario", "Rossi", "m@studenti.unisa.it");
        Libro libro = new Libro("1111111111", "Titolo1", List.of("Umberto Eco"), 2020, 1);

        servizio.registraPrestito(utente, libro, LocalDate.now().plusDays(7));

        Prestito prestito = archivio.listaPrestiti().get(0);
        int disponibiliPrimaRestituzione = libro.getCopieDisponibili();

        servizio.registraRestituzione(prestito);

        assertEquals(StatoPrestito.CONCLUSO, prestito.getStato(), "Il prestito dovrebbe risultare Concluso.");
        assertEquals(disponibiliPrimaRestituzione + 1, libro.getCopieDisponibili(), "Il numero di copie disponibili del libro dovrebbe essere stato incrementato.");
        assertEquals(0, utente.getPrestitiAttivi().size(), "L'utente non dovrebbe più avere prestiti attivi.");
    }

    @Test
    public void testAggiorna() {    //test di aggiornaRitardi(): con libro già in ritardo appena inserito
        Utente utente = new Utente("123", "Mario", "Rossi", "m@studenti.unisa.it");
        Libro libro = new Libro("1111111111", "Titolo1", List.of("Umberto Eco"), 2020, 1);
        
        Prestito prestito = new Prestito(utente, libro, LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));
        archivio.aggiungiPrestito(prestito);
        utente.aggiungiPrestito(prestito);

        servizio.aggiornaRitardi();

        int inRitardo = archivio.cercaPrestiti(FiltroPrestito.filtraInRitardo()).size();
        assertTrue(inRitardo >= 1, "Il libro dovrebbe essere già in ritardo.");
    }

    @Test
    public void testGetPrestitiConclusi() { //test di getPrestitiConclusi(): verifica il conteggio
        servizio.registraPrestito(utente, libro, LocalDate.now().plusDays(15));
        Prestito p = archivio.listaPrestiti().get(0);
        servizio.registraRestituzione(p);

        assertEquals(1, servizio.getPrestitiConclusi(), "Dovrebbe esserci 1 prestito concluso.");
        assertEquals(0, servizio.getPrestitiInCorso(), "Non dovrebbero esserci prestiti in corso.");
    }

    @Test
    public void testGetPrestitiInRitardo() { //test di getPrestitiInRitardo(): verifica il conteggio
        Prestito pScaduto = new Prestito(utente, libro, LocalDate.now().minusDays(10), LocalDate.now().minusDays(1));
        pScaduto.setStato(StatoPrestito.IN_RITARDO); // Simuliamo stato già settato o settato da aggiornaRitardi
        archivio.aggiungiPrestito(pScaduto);

        assertEquals(1, servizio.getPrestitiInRitardo(), "Dovrebbe esserci 1 prestito in ritardo.");
    }
}