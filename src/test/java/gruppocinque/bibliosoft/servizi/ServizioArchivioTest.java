package gruppocinque.bibliosoft.servizi;

import gruppocinque.bibliosoft.archivi.Archivio;
import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.modelli.Prestito;
import gruppocinque.bibliosoft.modelli.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;



public class ServizioArchivioTest {

    private ServizioArchivio servizioArchivio;
    private String fileArchivio;
    private Archivio archivio;

    @BeforeEach
    public void setUp() {
        archivio = new Archivio();
    }

    @Test
    public void testCostruttore() {
        ServizioArchivio servizio = new ServizioArchivio("file_di_prova.dat", archivio);
        assertNotNull(servizio, "L'oggetto ServizioArchivio dovrebbe essere stato creato e non essere null.");
    }

    @Test
    void testCarica() { //test di carica(): mi assicuro che carica provi a caricare un file che non esiste (non dovrebbe lanciare eccezioni poichè vengono gestite internamente)
        File file = new File("file_non_esistent.dat");

        // Se esiste, lo elimino; se non esiste va bene lo stesso
        if (file.exists())
            assertTrue(file.delete(), "Se il file esiste, dovrebbe essere eliminato.");
        assertFalse(file.exists(), "Il file deve risultare inesistente prima di caricare.");

        ServizioArchivio servizio = new ServizioArchivio(file.getAbsolutePath(), archivio);

        assertDoesNotThrow(() -> servizio.carica(), "Non dovrebbe lanciare eccezioni (sono gestite internamente).");

        assertTrue(archivio.listaLibri().isEmpty(), "La lista dei libri dovrebbe essere vuota.");
        assertTrue(archivio.listaUtenti().isEmpty(), "La lista degli utenti dovrebbe essere vuota.");
        assertTrue(archivio.listaPrestiti().isEmpty(), "La lista dei prestiti dovrebbe essere vuota.");
    }

    @Test
    void testSalva() throws Exception {
        File file = new File("archivio.dat");

        try {
            ServizioArchivio servizio = new ServizioArchivio(file.getAbsolutePath(), archivio);
            servizio.salva();

            assertTrue(file.exists(), "Il file dovrebbe esistere (appena creato).");
            assertTrue(file.length() > 0, "Il file dovrebbe contenere qualcosa (mappa vuota).");

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                assertTrue(obj instanceof Map, "Il file dovrebbe contenere una instanza di mappa.");

                Map<String, Object> mappa = (Map<String, Object>) obj;

                assertTrue(mappa.containsKey("libri"), "La mappa dovrebbe contenere una lista di libri vuota.");
                assertTrue(mappa.containsKey("utenti"), "La mappa dovrebbe contenere una lista di utenti vuota.");
                assertTrue(mappa.containsKey("prestiti"), "La mappa dovrebbe contenere una lista di prestiti vuota.");

                assertTrue(((List<Libro>) mappa.get("libri")).isEmpty(), "La lista di libri dovrebbe essere vuota.");
                assertTrue(((List<Utente>) mappa.get("utenti")).isEmpty(), "La lista di utenti dovrebbe essere vuota.");
                assertTrue(((List<Prestito>) mappa.get("prestiti")).isEmpty(), "La lista di prestiti dovrebbe essere vuota.");
            }
        } finally {
            new File(file.getAbsolutePath()).delete(); //mi assicuro che il file venga cancellato
        }
    }
}
