package gruppocinque.bibliosoft;

import gruppocinque.bibliosoft.Bibliosoft;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BibliosoftTest {
    @Test
    public void testCostruttore() { // Verifica che la classe principale possa essere istanziata.
      Bibliosoft app = new Bibliosoft();
      assertNotNull(app, "L'oggetto Bibliosoft non dovrebbe essere null.");
    }
}
