package gruppocinque.bibliosoft.archivi.filtri;

import gruppocinque.bibliosoft.archivi.filtri.FiltroLibro;
import gruppocinque.bibliosoft.archivi.filtri.InterfacciaFiltro;
import gruppocinque.bibliosoft.modelli.Libro;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FiltroLibroTest {

    private Libro libroTest;

    @BeforeEach
    public void setUp() {
        libroTest = new Libro("9788800000000", "Il Nome della Rosa", List.of("Umberto Eco"), 1980, 5);
    }

    @Test
    public void testRicerca1() { //test di ricerca(): controlla se trova il libro tramite una parte del titolo
        libroTest = new Libro("9788800000000", "Il Nome della Rosa", List.of("Umberto Eco"), 1980, 5);
        InterfacciaFiltro<Libro> filtro = FiltroLibro.ricerca("della");
        assertTrue(filtro.filtra(libroTest) , "Dovrebbe trovare il libro cercando parte del titolo.");
    }

    @Test
    public void testRicerca2() { //test di ricerca(): controlla se trova il libro tramite titolo ignorando maiuscole/minuscole
        InterfacciaFiltro<Libro> filtro = FiltroLibro.ricerca("nOmE");

        assertTrue(filtro.filtra(libroTest), "Dovrebbe trovare il libro ignorando maiuscole e minuscole.");
    }

    @Test
    public void testRicerca3() { //test di ricerca(): controlla se trova il libro tramite nome autore
        InterfacciaFiltro<Libro> filtro = FiltroLibro.ricerca("Eco");

        assertTrue(filtro.filtra(libroTest), "Dovrebbe trovare il libro cercando l'autore solo per nome.");
    }

    @Test
    public void testRicerca4() { //test di ricerca(): controlla se trova il libro tramite una parte dell'ISBN
        InterfacciaFiltro<Libro> filtro = FiltroLibro.ricerca("97888");

        assertTrue(filtro.filtra(libroTest), "Dovrebbe trovare il libro cercando solo una parte dell'ISBN.");
    }

    @Test
    public void testRicerca5() { //test di ricerca(): controlla se il filtro restituisce false per una stringa non presente
        InterfacciaFiltro<Libro> filtro = FiltroLibro.ricerca("Harry Potter");

        assertFalse(filtro.filtra(libroTest), "Non dovrebbe trovare nessun libro.");
    }

    @Test
    public void testRicerca6() { //test di ricerca(): controlla il comportamento con stringa vuota
        InterfacciaFiltro<Libro> filtro = FiltroLibro.ricerca("");

        assertTrue(filtro.filtra(libroTest), "Una stringa di ricerca vuota è contenuta ovunque, dovrebbe tornare true.");
    }

    @Test
    public void testRicercaIsbn1() { //test di ricercaIsbn(): controlla se trova il libro con ISBN esatto
        InterfacciaFiltro<Libro> filtro = FiltroLibro.ricercaIsbn("9788800000000");

        assertTrue(filtro.filtra(libroTest), "Dovrebbe trovare il libro con ISBN esatto.");
    }

    @Test
    public void testRicercaIsbn2() { //test di ricercaIsbn(): controlla che la ricerca esatta NON trovi parziali (differenza con ricerca generica)
        InterfacciaFiltro<Libro> filtro = FiltroLibro.ricercaIsbn("97888");

        assertFalse(filtro.filtra(libroTest), "La ricerca ISBN esatta non deve accettare match parziali.");
    }

    @Test
    public void testRicercaIsbn3() { //test di ricercaIsbn(): controlla che un ISBN diverso non passi il filtro
        InterfacciaFiltro<Libro> filtro = FiltroLibro.ricercaIsbn("0000000000000");

        assertFalse(filtro.filtra(libroTest), "Non dovrebbe trovare il libro con ISBN diverso.");
    }
}