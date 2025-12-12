/**
 * @file InterfacciaFiltro.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.archivi.filtri;

/**
 * 
 * @brief Interfaccia funzionale per la definizione di filtri generici.
 * @details
 * Questa interfaccia definisce il contratto per le classi che fungono da
 * predicati per filtrare liste di oggetti.
 *
 * @tparam T Il tipo di oggetto da filtrare (es. Libro, Utente, Prestito).
 */
public interface InterfacciaFiltro<T> {

    /**
     * @brief Valuta se un elemento soddisfa i criteri del filtro.
     * @details
     * @param[in] elemento L'oggetto da valutare.
     *
     * @return true se l'elemento soddisfa la condizione, false altrimenti.
     */
    boolean filtra(T elemento);
}
