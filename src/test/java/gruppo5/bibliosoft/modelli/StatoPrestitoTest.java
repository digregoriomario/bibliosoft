package gruppo5.bibliosoft.modelli;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StatoPrestitoTest {
    
    @Test
    public void testValoriEnum() {  //test sulla struttura dell'enum: controlla che siano definiti esattamente i 3 stati previsti
        assertEquals(3, StatoPrestito.values().length, "L'enum dovrebbe contenere esattamente 3 stati.");
    }
    
    @Test
    public void testToString() { //test del toString(): verifica che ogni stato sia associato alla descrizione corretta ("In Corso", ecc.)
        assertEquals("In Corso", StatoPrestito.IN_CORSO.toString(), "La descrizione per IN_CORSO non è corretta.");
        assertEquals("In Ritardo", StatoPrestito.IN_RITARDO.toString(), "La descrizione per IN_RITARDO non è corretta.");
        assertEquals("Concluso", StatoPrestito.CONCLUSO.toString(), "La descrizione per CONCLUSO non è corretta.");
    }
}