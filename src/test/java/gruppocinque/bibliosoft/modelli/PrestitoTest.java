package gruppocinque.bibliosoft.modelli;

import gruppocinque.bibliosoft.modelli.Prestito;
import gruppocinque.bibliosoft.modelli.StatoPrestito;
import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.modelli.Utente;
import org.junit.jupiter.api.*;
import java.time.LocalDate; 
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PrestitoTest {
    private Utente utente;
    private Libro libro;
    private Prestito prestito;
    private LocalDate dataInizio;
    private LocalDate dataPrevista;

    @BeforeEach
    public void setUp() {
        utente = new Utente("123", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 0);
        dataInizio = LocalDate.now();
        dataPrevista = LocalDate.now().plusDays(5);
        prestito = new Prestito(utente, libro, dataInizio, dataPrevista);
    }

    @Test
    public void testCostruttore() { //test del costruttore: verifica corretta inizializzazione (ID, Stato, Date)
        assertNotNull(prestito.getId(), "L'ID deve essere generato e non null.");
        assertEquals(StatoPrestito.IN_CORSO, prestito.getStato(), "Lo stato iniziale deve essere IN_CORSO.");
        assertEquals(dataPrevista, prestito.getDataPrevista(), "La data prevista deve corrispondere.");
    }

    @Test
    public void testAggiornaStato1() { //test di aggiornaStato(): verifica che rimanga IN_CORSO prima della scadenza
        LocalDate oggi = dataPrevista.minusDays(1);
        prestito.aggiornaStato(oggi);
        
        assertEquals(StatoPrestito.IN_CORSO, prestito.getStato(), "Il prestito non dovrebbe essere in ritardo.");
    }

    @Test
    public void testAggiornaStato2() { //test di aggiornaStato(): il giorno esatto della scadenza NON è ritardo
        prestito.aggiornaStato(dataPrevista);
        
        assertEquals(StatoPrestito.IN_CORSO, prestito.getStato(), "Il giorno della scadenza il prestito è ancora valido.");
    }

    @Test
    public void testAggiornaStato3() { //test di aggiornaStato(): verifica transizione a IN_RITARDO dopo la scadenza
        LocalDate oggi = dataPrevista.plusDays(1);
        prestito.aggiornaStato(oggi);
        
        assertEquals(StatoPrestito.IN_RITARDO, prestito.getStato(), "Il prestito dovrebbe passare a IN_RITARDO.");
    }

    @Test
    public void testAggiornaStato4() { //test di aggiornaStato(): verifica invarianza su prestito CONCLUSO
        prestito.setStato(StatoPrestito.CONCLUSO);
        prestito.aggiornaStato(dataPrevista.plusMonths(1));
        
        assertEquals(StatoPrestito.CONCLUSO, prestito.getStato(), "Un prestito concluso non deve cambiare stato.");
    }

    @Test
    public void testEInRitardo1() { //test di eInRitardo(): verifica il confine esatto della scadenza
        assertFalse(prestito.eInRitardo(dataPrevista), "Non deve essere in ritardo il giorno stesso della scadenza.");
        assertTrue(prestito.eInRitardo(dataPrevista.plusDays(1)), "Deve scattare il ritardo il giorno dopo la scadenza.");
    }

    @Test
    public void testEInRitardo2() { //test di eInRitardo(): verifica date molto precedenti alla scadenza
        assertFalse(prestito.eInRitardo(dataPrevista.minusDays(10)),"Non deve risultare in ritardo se mancano ancora giorni alla scadenza.");
    }

    @Test
    public void testEInRitardo3() { //test di eInRitardo(): verifica interazione con stato CONCLUSO
        prestito.setStato(StatoPrestito.CONCLUSO);
        
        assertFalse(prestito.eInRitardo(dataPrevista.plusMonths(1)),"Un prestito già CONCLUSO non deve mai risultare in ritardo.");
    }
 
    @Test
    public void testEquals1() { //test equals(): verifica che l'oggetto sia uguale a se stesso
        assertTrue(prestito.equals(prestito), "Un oggetto è uguale a se stesso.");
    }

    @Test
    public void testEquals2() { //test equals(): confronto con null
        assertFalse(prestito.equals(null), "Equals con null deve tornare false.");
    }

    @Test
    public void testEquals3() { //test equals(): confronto con tipo diverso
        assertFalse(prestito.equals(new Object()), "Equals con altro tipo deve tornare false.");
    }

    @Test
    public void testEquals4() { //test equals(): confronto tra due prestiti distinti (ID diversi)
        Prestito altroPrestito = new Prestito(utente, libro, dataInizio, dataPrevista);
        
        assertFalse(prestito.equals(altroPrestito), "Due prestiti distinti hanno ID diversi.");
    }


    @Test
    public void testCompareTo1() { //test compareTo(): confronto con se stesso
        assertEquals(0, prestito.compareTo(prestito), "Confronto con se stesso deve dare 0.");
    }

    @Test
    public void testCompareTo2() { //test compareTo(): ordinamento per data scadenza
        Prestito scadePrima = new Prestito(utente, libro, dataInizio, dataPrevista.minusDays(10));
        Prestito scadeDopo = new Prestito(utente, libro, dataInizio, dataPrevista.plusDays(10));

        assertTrue(scadePrima.compareTo(prestito) < 0, "Scadenza precedente viene prima.");
        assertTrue(scadeDopo.compareTo(prestito) > 0, "Scadenza successiva viene dopo.");
    }
    
    @Test
    public void testCompareTo3() { //test compareTo(): a parità di data usa ID come discriminante
        Prestito stessoGiorno = new Prestito(utente, libro, dataInizio, dataPrevista);
        
        assertNotEquals(0, prestito.compareTo(stessoGiorno), "A parità di data, l'ID discrimina.");
    }

    @Test
    public void testCompareTo4() { //test compareTo(): proprietà transitiva
        Prestito p1 = new Prestito(utente, libro, dataInizio, dataPrevista.minusDays(10));
        Prestito p2 = prestito;
        Prestito p3 = new Prestito(utente, libro, dataInizio, dataPrevista.plusDays(10));

        assertTrue(p1.compareTo(p2) < 0);
        assertTrue(p2.compareTo(p3) < 0);
        assertTrue(p1.compareTo(p3) < 0, "Proprietà transitiva violata.");
    }
    
    @Test
    public void testGetId() { //test di getId(): verifica che restituisca una stringa valida non nulla
        assertNotNull(prestito.getId(), "L'ID restituito non deve essere null.");
        assertFalse(prestito.getId().isEmpty(), "L'ID restituito non deve essere vuoto.");
    }

    @Test
    public void testGetUtente() { //test di getUtente(): verifica che restituisca l'oggetto Utente corretto associato al prestito
        assertEquals(utente, prestito.getUtente(), "L'utente restituito deve corrispondere a quello passato al costruttore.");
    }

    @Test
    public void testGetLibro() { //test di getLibro(): verifica che restituisca l'oggetto Libro corretto associato al prestito
        assertEquals(libro, prestito.getLibro(), "Il libro restituito deve corrispondere a quello passato al costruttore.");
    }

    @Test
    public void testGetDataInizio() { //test di getDataInizio(): verifica che restituisca la data di inizio corretta
        assertEquals(dataInizio, prestito.getDataInizio(), "La data di inizio deve corrispondere a quella impostata nel costruttore.");
    }

    @Test
    public void testGetDataPrevista() { //test di getDataPrevista(): verifica che restituisca la data prevista di restituzione corretta
        assertEquals(dataPrevista, prestito.getDataPrevista(), "La data prevista deve corrispondere a quella impostata nel costruttore.");
    }

    @Test
    public void testGetDataRestituzioneEffettiva() { //test di getDataRestituzioneEffettiva(): verifica che inizialmente sia null
        assertNull(prestito.getDataRestituzioneEffettiva(), "La data di restituzione effettiva deve essere null appena creato il prestito.");
    }

    @Test
    public void testSetDataRestituzioneEffettiva() { //test di setDataRestituzioneEffettiva(): verifica che il setter aggiorni correttamente la data
        LocalDate dataRestituzione = LocalDate.now().plusDays(3);
        prestito.setDataRestituzioneEffettiva(dataRestituzione);
        
        assertEquals(dataRestituzione, prestito.getDataRestituzioneEffettiva(), "La data di restituzione effettiva deve essere aggiornata dopo il set.");
    }

    @Test
    public void testGetStato() { //test di getStato(): verifica che restituisca lo stato corrente (inizialmente IN_CORSO)
        assertEquals(StatoPrestito.IN_CORSO, prestito.getStato(), "getStato deve restituire lo stato corrente.");
    }

   @Test
    public void testSetStato() { //test di setStato(): verifica che il setter modifichi correttamente lo stato del prestito tra quelli ammessi
  
        prestito.setStato(StatoPrestito.IN_RITARDO);
        assertEquals(StatoPrestito.IN_RITARDO, prestito.getStato(), "setStato deve aggiornare lo stato a IN_RITARDO.");
        
        prestito.setStato(StatoPrestito.CONCLUSO);
        assertEquals(StatoPrestito.CONCLUSO, prestito.getStato(), "setStato deve aggiornare lo stato a CONCLUSO.");

        prestito.setStato(StatoPrestito.IN_CORSO);
        assertEquals(StatoPrestito.IN_CORSO, prestito.getStato(), "setStato deve aggiornare lo stato a IN_CORSO.");
    }
}