package gruppocinque.bibliosoft.servizi;

import gruppocinque.bibliosoft.servizi.ServizioArchivio;
import gruppocinque.bibliosoft.archivi.Archivio;
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
}
