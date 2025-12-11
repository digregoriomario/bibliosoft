package gruppo5.bibliosoft.modelli;

import java.util.ArrayList;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LibroTest {

    @Test
    public void testConstruttore() { //test del costruttore: controlla se le copie disponibili sono pari alle copie totali
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 3);

        assertEquals(3, libro.getCopieDisponibili(), "Copie disponibili dovrebbe essere uguale a copie totali.");
    }

    @Test
    public void testIsDisponibile1() {   //test di isDisponibile(): controlla se un libro con 0 copie totali è disponibile
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 0);

        assertFalse(libro.isDisponibile(), "Dovrebbe essere non disponibile.");
    }

    @Test
    public void testIsDisponibile2() {   //test di isDisponibile(): controlla se un libro con 5 copie totali è disponibile
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 5);

        assertTrue(libro.isDisponibile(), "Dovrebbe essere disponibile.");
    }

    @Test
    public void testHaPrestitiAttivi1() {    //test di haPrestitiAttivi(): controlla se un libro appena creato ha prestiti attivi
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 5);

        assertFalse(libro.haPrestitiAttivi(), "Non dovrebbe avere prestiti attivi.");
    }

    @Test
    public void testHaPrestitiAttivi2() {    //test di haPrestitiAttivi(): controlla se un libro con copieTotali != copieDisponibili ha prestiti attivi
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 5);
        libro.setCopieDisponibili(4);

        assertTrue(libro.haPrestitiAttivi(), "Dovrebbe avere prestiti attivi.");
    }

    @Test
    public void testEquals1() {  //test della equals(): controlla se un libro e la sua copia sono uguali
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 5);
        Libro copiaLibro = libro;

        assertTrue(libro.equals(copiaLibro), "I due libri dovrebbero essere uguali.");
    }

    @Test
    public void testEquals2() {  //test della equals(): controlla se un due libri con tutti gli attributi sono diversi
        Libro libro1 = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 5);
        Libro libro2 = new Libro("9999999999", "tITOLO", List.of("Rossi Mario"), 1920, 3);

        assertFalse(libro1.equals(libro2), "I due libri dovrebbero essere diversi.");
    }

    @Test
    public void testEquals3() {  //test della equals(): controllo se un libro è uguale a un oggetto generico
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 5);

        assertFalse(libro.equals(new Object()), "Il libro dovrebbe essere diverso da un generico oggetto.");
    }

    @Test
    public void testEquals4() {  //test della equals(): controlla se due libri (con solo gli isbn identici) sono uguali
        Libro libro1 = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 5);
        Libro libro2 = new Libro("1234567890", "tITOLO", List.of("Rossi Mario"), 1920, 3);

        assertTrue(libro1.equals(libro2), "I due libri dovrebbero essere uguali (stesso isbn).");
    }

    @Test
    public void testEquals5() {  //test della equals(): controlla se due libri (con solo gli isbn diversi) sono uguali
        Libro libro1 = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 5);
        Libro libro2 = new Libro("9999999999", "Titolo", List.of("Mario Rossi"), 2020, 5);

        assertFalse(libro1.equals(libro2), "I due libri dovrebbero essere diversi (isbn diversi).");
    }

    @Test
    void testCompareTo1() { //test sulla compareTo(): controlla se due libri risultano simili avendo lo stesso isbn ma titolo diverso (serve solo per evitare duplicati)
        Libro libro1 = new Libro("1234567890", "Titolo A", List.of("Mario Rossi"), 2020, 5);
        Libro libro2 = new Libro("1234567890", "Titolo B", List.of("Mario Rossi"), 2020, 5);

        assertEquals(0, libro1.compareTo(libro2), "I due libri dovrebbero essere simili (stesso isbn).");
        assertEquals(0, libro2.compareTo(libro1), "I due libri dovrebbero essere simili (stesso isbn).");
    }

    @Test
    void testCompareTo2() { //test sulla compareTo(): controlla l'ordine lessicografico del titolo (ha la priorità sull'isbn)
        Libro libro1 = new Libro("1111111111", "Titolo A", List.of("Mario Rossi"), 2020, 5);
        Libro libro2 = new Libro("9999999999", "Titolo B", List.of("Mario Rossi"), 2020, 5);

        assertTrue(libro1.compareTo(libro2) < 0, "libro1 dovrebbe venere prima(lessicograficamente) di libro2.");
        assertTrue(libro2.compareTo(libro1) > 0, "libro2 dovrebbe venere dopo(lessicograficamente) di libro1.");
    }

    @Test
    void testCompareTo3() { //test sulla compareTo(): controlla l'ordine lessicografico dell'isbn (a parità di titolo)
        Libro libro1 = new Libro("1111111111", "Titolo A", List.of("Mario Rossi"), 2020, 5);
        Libro libro2 = new Libro("9999999999", "Titolo A", List.of("Mario Rossi"), 2020, 5);

        assertTrue(libro1.compareTo(libro2) < 0, "libro1 dovrebbe venere prima(lessicograficamente) di libro2.");
        assertTrue(libro2.compareTo(libro1) > 0, "libro2 dovrebbe venere dopo(lessicograficamente) di libro1.");
    }

    @Test
    void testCompareTo4() { //test sulla compareTo(): controlla che che quando titolo è diverso, l'ISBN non influenzi il risultato
        Libro libro1 = new Libro("1111111111", "Titolo A", List.of("Mario Rossi"), 2020, 5);
        Libro libro2 = new Libro("9999999999", "Titolo B", List.of("Mario Rossi"), 2020, 5);

        assertTrue(libro1.compareTo(libro2) < 0, "libro 1 dovrebbe venire prima(lessicograficamente) di libro2.");
    }

    @Test
    void testCompareTo5() { //test sulla compareTo(): controlla il caso di uguaglianza totale (titolo e ISBN uguali)
        Libro libro1 = new Libro("1111111111", "Titolo A", List.of("Mario Rossi"), 2020, 5);
        Libro libro2 = new Libro("1111111111", "Titolo A", List.of("Mario Rossi"), 2020, 5);

        assertEquals(0, libro1.compareTo(libro2), "libro1 dovrebbe essere uguale a libro 2.");
        assertEquals(0, libro2.compareTo(libro1), "libro1 dovrebbe essere uguale a libro 2.");
    }

    @Test
    void testCompareTo6() { //test sulla compareTo(): controlla la oerenza minima di ordinamento su tre libri
        Libro libro1 = new Libro("3333333333", "Titolo A", List.of("Mario Rossi"), 2020, 5);
        Libro libro2 = new Libro("2222222222", "Titolo A", List.of("Mario Rossi"), 2020, 5);
        Libro libro3 = new Libro("1111111111", "Titolo B", List.of("Mario Rossi"), 2020, 5);
        
        assertTrue(libro2.compareTo(libro1) < 0, "libro2 dovrebbe venire prima di libro1.");
        assertTrue(libro2.compareTo(libro3) < 0, "libro2 dovrebbe venire prima di libro3.");
        assertTrue(libro1.compareTo(libro3) < 0, "libro1 dovrebbe venire prima di libro3.");
    }
    
    @Test
    void testContieneAutore1() {    //test sulla contieneAutore(): controlla se un libro contiene effettivamente un autore
        Libro libro = new Libro("1111111111", "Titolo A", List.of("Umberto Eco","Italo Calvino"), 2020, 5);

        assertTrue(libro.contieneAutore("Umberto Eco"), "L'autore dovrebbe essere contenuto.");
    }

    @Test
    void testContieneAutore2() {    //test sulla contieneAutore(): controlla se un libro contiene effettivamente un autore ma scritto male (case-insensitive)
        Libro libro = new Libro("1111111111", "Titolo A", List.of("Umberto Eco","Italo Calvino"), 2020, 5);

        assertTrue(libro.contieneAutore("uMbErTo eCo"), "L'autore dovrebbe essere contenuto (case-insensitive).");
    }

    @Test
    void testContieneAutore3() {    //test sulla contieneAutore(): controlla se un libro contiene effettivamente un autore (sottostringa) (case-insensitive)
        // match per sottostringa
        Libro libro = new Libro("1111111111", "Titolo A", List.of("Umberto Eco","Italo Calvino"), 2020, 5);

        assertTrue(libro.contieneAutore("eco"), "L'autore dovrebbe essere contenuto.");
        assertTrue(libro.contieneAutore("Calv"), "L'autore dovrebbe essere contenuto.");
    }

    @Test
    void testContieneAutore4() {    //test sulla contieneAutore(): controlla se un libro contiene effettivamente un autore che in realtà non contiene
        Libro libro = new Libro("1111111111", "Titolo A", List.of("Umberto Eco","Italo Calvino"), 2020, 5);

        assertFalse(libro.contieneAutore("Pirandello"), "L'autore non dovrebbe essere contenuto.");
    }

    @Test
    void testContieneAutore5() {    //test sulla contieneAutore(): controlla se un libro contiene effettivamente un autore che in realtà non contiene (lista vuota)
        Libro libro = new Libro("1111111111", "Titolo A", List.of(), 2020, 5);

        assertFalse(libro.contieneAutore("Eco"), "L'autore non dovrebbe essere contenuto (lista vuota).");
    }

    @Test
    void testContieneAutore6() {    //test sulla contieneAutore(): controlla se un libro contiene effettivamente un autore che in realtà non contiene (stringa vuota)
        Libro libro = new Libro("1111111111", "Titolo A", List.of("Umberto Eco"), 2020, 5);

        assertTrue(libro.contieneAutore(""), "La ricerca dobrebbe uscire true");
    }
    
   @Test
    public void testGetAutori() { //test di getAutori(): controlla che le modifiche alla lista restituita non alterino i dati interni del libro
        Libro libro = new Libro("1234567890", "Titolo", List.of("Mario Rossi"), 2020, 5);
        List<String> listaOttenuta = libro.getAutori();

        listaOttenuta.add("Autore Falso"); 

        assertFalse(libro.contieneAutore("Autore Falso"), "L'autore aggiunto alla lista esterna non dovrebbe risultare nel libro.");
    }

    @Test
    public void testSetAutori() { //test di setAutori(): verifica l'incapsulamento, modificare la lista originale dopo il set non deve cambiare il libro
        ArrayList<String> listaEsterna = new ArrayList<>();
        listaEsterna.add("Mario Rossi");

        Libro libro = new Libro("1234567890", "Titolo", List.of("Umberto Eco"), 2020, 5);
        libro.setAutori(listaEsterna);

        listaEsterna.add("Autore Falso"); 

        assertFalse(libro.contieneAutore("Autore Falso"), "Il libro non dovrebbe contenere autori aggiunti successivamente.");
    }
    
    @Test
    public void testToString() { //test del toString(): verifica che la stringa generata sia nel formato corretto, ovvero Titolo + ISBN
        Libro libro = new Libro("1234567890", "Il Signore degli Anelli", List.of("Tolkien"), 1954, 5);
        
        String aspettato = "Il Signore degli Anelli (1234567890)";
        
        assertEquals(aspettato, libro.toString(), "Il formato del toString non è corretto.");
    }
}
