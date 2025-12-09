package gruppo5.bibliosoft.modelli;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StatoPrestitoTest {
    @Test
    public void testValoriEnum() {  //test sulla struttura dell'enum: controlla che siano definiti esattamente i 3 stati previsti
        assertEquals(3, StatoPrestito.values().length, "L'enum dovrebbe contenere esattamente 3 stati.");
    }
}