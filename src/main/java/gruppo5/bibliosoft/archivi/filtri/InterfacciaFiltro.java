package gruppo5.bibliosoft.archivi.filtri;

/**
 * @file InterfacciaFiltro.java
 * 
 * @brief Interfaccia funzionale per la definizione di filtri generici.
 * 
 * Questa interfaccia definisce il contratto per le classi che fungono da predicati per filtrare liste di oggetti.
 * 
 * @tparam T Il tipo di oggetto da filtrare (es. Libro, Utente, Prestito).
 */
public interface InterfacciaFiltro {

    /**
     * @brief Valuta se un elemento soddisfa i criteri del filtro.
     * 
     * @param[in] elemento L'oggetto da valutare.
     * 
     * @return true se l'elemento soddisfa la condizione, false altrimenti.
     */
    boolean filtra(T elemento);
}
