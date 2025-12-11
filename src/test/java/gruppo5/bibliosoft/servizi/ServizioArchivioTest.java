package gruppo5.bibliosoft.servizi;

import gruppo5.bibliosoft.archivi.Archivio;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServizioArchivioTest {
    private String fileArchivio;
    private Archivio archivio;

    @BeforeEach
    public void setUp() {
        archivio = new Archivio();
    }
    
    @Test
    public void testCostruttore() { //test del costruttore: controlla se il servizio viene istanziato correttamente senza errori
        ServizioArchivio servizio = new ServizioArchivio("file_di_prova.dat", archivio);

        assertNotNull(servizio, "L'oggetto ServizioArchivio dovrebbe essere stato creato e non essere null.");
    }

    @Test
    public void testCarica() {   //test su carica(): con percorso file vuoto
        ServizioArchivio servizioArchivio = new ServizioArchivio("", archivio);
        assertThrows(
                IOException.class,
                () -> servizioArchivio.carica(),
                "Mi aspettavo IOException, ma non è stata lanciata."
        );
    }
    
    @Test
    public void testSalva() {   //test su salva(): con percorso file vuoto
        ServizioArchivio servizioArchivio = new ServizioArchivio("", archivio);
        assertThrows(
                IOException.class,
                () -> servizioArchivio.salva(),
                "Mi aspettavo IOException, ma non è stata lanciata."
        );
    }
}
