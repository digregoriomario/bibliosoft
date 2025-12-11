package gruppo5.bibliosoft.servizi;

import gruppo5.bibliosoft.archivi.Archivio;
import gruppo5.bibliosoft.modelli.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServizioLibriTest {

    private Archivio archivio;
    private ServizioLibri servizioLibri;
    private Libro libroValido;

    @BeforeEach
    public void setUp() {
        archivio = new Archivio();
        servizioLibri = new ServizioLibri(archivio);
        
        libroValido = new Libro("9788800000000", "Libro Test", List.of("Autore Test"), 2020, 5);
    }
    
    @Test
    public void testCostruttore() { //test del costruttore: controlla se il servizio viene istanziato correttamente
        assertNotNull(servizioLibri, "Il servizio dovrebbe essere stato istanziato.");
    }

    @Test
    public void testAggiungiLibro() {   //test su aggiungiLibro(): libro nullo
        assertThrows(
                RuntimeException.class,
                () -> servizioLibri.aggiungiLibro(null),
                "Mi aspettavo un'eccezione per libro nullo."
        );
    }

    @Test
    public void testModificaLibro() {   //test su modificaLibro(): libro nullo
        assertThrows(
                RuntimeException.class,
                () -> servizioLibri.modificaLibro(null),
                "Mi aspettavo un'eccezione per libro nullo."
        );
    }
    
    @Test
    public void testEliminaLibro1() { //test di eliminaLibro(): controlla se il libro viene rimosso se non ha prestiti attivi
        servizioLibri.aggiungiLibro(libroValido);
        
        servizioLibri.eliminaLibro(libroValido);
        
        assertEquals(0, servizioLibri.getLibriTotali(), "Il libro dovrebbe essere stato rimosso.");
    }

    @Test
    public void testEliminaLibro2() { //test di eliminaLibro(): controlla che venga impedita l'eliminazione se ci sono copie in prestito
        servizioLibri.aggiungiLibro(libroValido);
        
        libroValido.setCopieDisponibili(4); 
        
        assertThrows(
                IllegalStateException.class, 
                () -> servizioLibri.eliminaLibro(libroValido),
                "Impossibile eliminare: libro in prestito o senza copie disponibili");
    }

    @Test
    public void testCercaLibri() {  //test su cercaLibri(): filtro nullo su archivio vuoto
        List<Libro> risultati = servizioLibri.cercaLibri(null);
        
        assertNotNull(risultati);
        assertEquals(0, risultati.size(), "Dovrebbe uscire 0.");
    }

    @Test
    public void testListaLibri() {  //test su listaLibri(): su archivio vuoto
        List<Libro> libri = servizioLibri.listaLibri();
        
        assertNotNull(libri);
        assertEquals(0, libri.size(), "Dovrebbe uscire 0.");
    }

    @Test
    public void testGetLibriTotali1() {  //test su getLibriTotali(): su archivio vuoto
        assertEquals(0, servizioLibri.getLibriTotali(), "Dovrebbe uscire 0.");
    }
    
    @Test
    public void testGetLibriTotali2() {  //test su getLibriTotali(): su archivio popolato da 2 libri
        servizioLibri.aggiungiLibro(new Libro("1111111111", "Titolo", List.of("Mario Rossi"), 2020, 3));
        servizioLibri.aggiungiLibro(new Libro("2222222222", "Titolo2", List.of("Mario Rossi"), 2021, 5));
        
        assertEquals(2, servizioLibri.getLibriTotali(), "Dovrebbe uscire 2.");
    }
    
    @Test
    public void testGetCopieTotali1() {  //test su getCopieTotali(): su archivio vuoto
        assertEquals(0, servizioLibri.getCopieTotali(), "Dovrebbe uscire 0.");
    }
    
    @Test
    public void testGetCopieTotali2() {  //test su getCopieTotali(): su archivio popolato da 2 libri con un totale di 8 copie fisiche
        servizioLibri.aggiungiLibro(new Libro("1111111111", "Titolo", List.of("Mario Rossi"), 2020, 3));
        servizioLibri.aggiungiLibro(new Libro("2222222222", "Titolo2", List.of("Mario Rossi"), 2021, 5));
        
        assertEquals(8, servizioLibri.getCopieTotali(), "Dovrebbe uscire 8.");
    }

    @Test
    public void testGetCopieDisponibili1() {    //test su getCopieDisponibili(): su archivio vuoto
        assertEquals(0, servizioLibri.getCopieDisponibili(), "Dovrebbe uscire 0.");
    }
    
    @Test
    public void testGetCopieDisponibili2() {    //test su getCopieDisponibili(): su archivio popolato da 2 libri
        Libro libro1 = new Libro("1111111111", "Titolo", List.of("Mario Rossi"), 2020, 3);
        Libro libro2 = new Libro("2222222222", "Titolo2", List.of("Mario Rossi"), 2021, 5);
        servizioLibri.aggiungiLibro(libro1);
        servizioLibri.aggiungiLibro(libro2);
        libro1.setCopieDisponibili(libro1.getCopieDisponibili() - 2);
        libro2.setCopieDisponibili(libro2.getCopieDisponibili() - 1);
        
        assertEquals(5, servizioLibri.getCopieDisponibili(), "Dovrebbe uscire 5.");
    }
}