package gruppo5.bibliosoft.modelli;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;


public class UtenteTest {
    private Utente utente;
    private Libro libro;
    private LocalDate dataInizio;
    private LocalDate dataPrevista;

    @BeforeEach
    public void setUp(){
        utente = new Utente("123", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 0);

        dataInizio = LocalDate.now();
        dataPrevista = LocalDate.now().plusDays(3);
    }
    
    
    @Test
    public void testCostruttore() { //test del costruttore: controlla se l'utente viene inizializzato senza prestiti
        assertFalse(utente.haPrestitiAttivi(), "Un nuovo utente non dovrebbe avere prestiti attivi.");
    }
    
    @Test
    public void testAggiungiPrestito() { //test di aggiungiPrestito(): controlla se il prestito viene aggiunto effettivamente alla lista
        utente.aggiungiPrestito(new Prestito(utente,libro,dataInizio,dataPrevista));

        assertEquals(1, utente.getPrestitiAttivi().size(), "La lista dei prestiti dovrebbe contenere 1 elemento.");
    }

    @Test
    public void testRimuoviPrestito1() { //test di rimuoviPrestito(): controlla se il prestito viene rimosso effettivamente dalla lista
        Prestito prestito = new Prestito(utente,libro,dataInizio,dataPrevista);
        utente.aggiungiPrestito(prestito);
        utente.rimuoviPrestito(prestito);

        assertEquals(0, utente.getPrestitiAttivi().size(), "La lista dei prestiti dovrebbe tornare vuota.");
    }
    
    @Test
    public void testRimuoviPrestito2() {    //test di rimuoviPrestito(): controlla se rimuovendo il prestito la lista torna vuota
        Prestito prestito = new Prestito(utente,libro,dataInizio,dataPrevista);
        utente.aggiungiPrestito(prestito);
        utente.rimuoviPrestito(prestito);

        assertFalse(utente.haPrestitiAttivi(), "Non dovrebbe avere prestiti attivi dopo la rimozione.");
    }
    
    @Test
    public void testHaPrestitiAttivi1() {    //test di haPrestitiAttivi(): controlla se un utente appena creato ha prestiti attivi
        assertFalse(utente.haPrestitiAttivi(), "Non dovrebbe avere prestiti attivi.");
    }

    @Test
    public void testHaPrestitiAttivi2() {    //test di haPrestitiAttivi(): controlla se un utente dopo l'aggiunta di un prestito ha prestiti attivi
        utente.aggiungiPrestito(new Prestito(utente,libro,dataInizio,dataPrevista));

        assertTrue(utente.haPrestitiAttivi(), "Dovrebbe avere prestiti attivi dopo l'aggiunta.");
    }   

    @Test
    public void testEquals1() {  //test della equals(): controlla se un utente e la sua referenza sono uguali
        Utente copiaUtente = utente;

        assertTrue(utente.equals(copiaUtente), "I due utenti dovrebbero essere uguali (stesso oggetto).");
    }

    @Test
    public void testEquals2() {  //test della equals(): controlla se due utenti con tutti attributi diversi sono diversi
        Utente utente1 = new Utente("123", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        Utente utente2 = new Utente("999", "Luigi", "Verdi", "luigi.verdi@studenti.unisa.it");

        assertFalse(utente1.equals(utente2), "I due utenti dovrebbero essere diversi.");
    }

    @Test
    public void testEquals3() {  //test della equals(): controllo se un utente è uguale a un oggetto generico
        assertFalse(utente.equals(new Object()), "L'utente dovrebbe essere diverso da un generico oggetto.");
    }

    @Test
    public void testEquals4() {  //test della equals(): controlla se due utenti con solo la matricola identica sono uguali
        Utente utente1 = new Utente("123", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        Utente utente2 = new Utente("123", "Luigi", "Verdi", "luigi.verdi@studenti.unisa.it");

        assertTrue(utente1.equals(utente2), "I due utenti dovrebbero essere uguali (stessa matricola).");
    }

    @Test
    public void testEquals5() {  //test della equals(): controlla se due utenti con solo la matricola diversa sono diversi
        Utente utente1 = new Utente("123", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        Utente utente2 = new Utente("999", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");

        assertFalse(utente1.equals(utente2), "I due utenti dovrebbero essere diversi.");
    }

    @Test
    void testCompareTo1() { //test sulla compareTo(): controlla se due utenti risultano simili avendo la stessa matricola
        Utente utente1 = new Utente("123", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");
        Utente utente2 = new Utente("123", "Luigi", "Verdi", "luigi.verdi@studenti.unisa.it");

        assertEquals(0, utente1.compareTo(utente2), "I due utenti dovrebbero essere simili (stessa matricola).");
        assertEquals(0, utente2.compareTo(utente1), "I due utenti dovrebbero essere simili (stessa matricola).");
    }

    @Test
    void testCompareTo2() { //test sulla compareTo(): controlla se due utenti risultano simili avendo la stessa email (logica specifica Utente)
        Utente utente1 = new Utente("111", "Mario", "Rossi", "email@studenti.unisa.it");
        Utente utente2 = new Utente("222", "Luigi", "Verdi", "email@studenti.unisa.it");

        assertEquals(0, utente1.compareTo(utente2), "I due utenti dovrebbero essere simili (stessa email).");
    }

    @Test
    void testCompareTo3() { //test sulla compareTo(): controlla l'ordine lessicografico del cognome
        Utente utente1 = new Utente("111", "Mario", "Bianchi", "mario.bianchi@studenti.unisa.it");
        Utente utente2 = new Utente("222", "Mario", "Rossi", "mario.rossi@studenti.unisa.it");

        assertTrue(utente1.compareTo(utente2) < 0, "utente1 dovrebbe venire prima di utente2.");
        assertTrue(utente2.compareTo(utente1) > 0, "utente2 dovrebbe venire dopo di utente1.");
    }

    @Test
    void testCompareTo4() { //test sulla compareTo(): controlla l'ordine lessicografico del nome (a parità di cognome)
        Utente utente1 = new Utente("111", "Alberto", "Rossi", "alberto.rossi@studenti.unisa.it");
        Utente utente2 = new Utente("222", "Bruno", "Rossi", "bruno.rossi@studenti.unisa.it");

        assertTrue(utente1.compareTo(utente2) < 0, "utente1 dovrebbe venire prima(lessicograficamente per nome) di utente2.");
        assertTrue(utente2.compareTo(utente1) > 0, "utente2 dovrebbe venire dopo(lessicograficamente per nome) di utente1.");
    }

    @Test
    void testCompareTo5() { //test sulla compareTo(): controlla l'ordine lessicografico della matricola (a parità di cognome e nome)
        Utente utente1 = new Utente("001", "Mario", "Rossi", "mario.rossi1@studenti.unisa.it");
        Utente utente2 = new Utente("002", "Mario", "Rossi", "mario.rossi2@studenti.unisa.it");

        assertTrue(utente1.compareTo(utente2) < 0, "utente1 dovrebbe venire prima di utente2.");
        assertTrue(utente2.compareTo(utente1) > 0, "utente2 dovrebbe venire dopo di utente1.");
    }

    @Test
    void testCompareTo6() { //test sulla compareTo(): controlla la coerenza minima di ordinamento su tre utenti
        Utente utente1 = new Utente("333", "Carlo", "Verdi", "carlo.verdi@studenti.unisa.it");
        Utente utente2 = new Utente("222", "Bruno", "Verdi", "bruno.verdi@studenti.unisa.it");
        Utente utente3 = new Utente("111", "Aldo", "Verdi", "aldo.verdi@studenti.unisa.it"); 
        
        assertTrue(utente3.compareTo(utente2) < 0, "utente3 dovrebbe venire prima di utente2.");
        assertTrue(utente3.compareTo(utente1) < 0, "utente3 dovrebbe venire prima di utente1.");
        assertTrue(utente2.compareTo(utente1) < 0, "utente2 dovrebbe venire prima di utente1.");
    }
    
    @Test
    public void testGetPrestitiAttivi() { //test di getPrestitiAttivi(): verifica che le modifiche alla lista restituita non alterino i dati interni dell'utente
        utente.aggiungiPrestito(new Prestito(utente, libro, dataInizio, dataPrevista));
        
        List<Prestito> listaEsterna = utente.getPrestitiAttivi();
        
        listaEsterna.clear();

        assertEquals(1, utente.getPrestitiAttivi().size(), "La lista interna dell'utente non deve subire modifiche se cambio quella restituita.");
    }

    @Test
    public void testToString() { //test del toString(): verifica che la stringa rispetti il formato corretto, ovvere Cognome + Nome + (Matricola)
        String risultato = utente.toString();
        String aspettato = "Rossi Mario (123)";

        assertEquals(aspettato, risultato, "Il formato del toString non è corretto.");
    }
}