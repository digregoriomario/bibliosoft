/**
 * @file FiltroLibro.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.archivi.filtri;

/**
 * @brief Classe di utilità per la creazione di filtri applicabili ai Libri.
 * @details Fornisce metodi statici che restituiscono istanze di
 * InterfacciaFiltro<Libro>
 * per effettuare ricerche basate su testo generico o campi specifici come ISBN.
 *
 * @see InterfacciaFiltro
 */
public class FiltroLibro {

    /**
     * @brief Genera un filtro per una ricerca testuale generica.
     * @details Cerca la corrispondenza della stringa fornita all'interno del
     * titolo, dell'autore o del codice ISBN del libro. La ricerca è
     * case-insensitive.
     *
     * @param[in] testo La stringa da ricercare.
     * @return Un'istanza di InterfacciaFiltro che restituisce true se trova
     * corrispondenze, false altrimenti.
     * @pre {@code testo != null}
     */
    public static InterfacciaFiltro<Libro> ricerca(String testo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Genera un filtro per la ricerca esatta tramite ISBN.
     * @details La ricerca è case-insensitive.
     *
     * @param[in] codice Il codice ISBN da cercare.
     *
     * @return Un filtro che restituisce true se l'ISBN corrisponde.
     */
    public static InterfacciaFiltro<Libro> ricercaIsbn(String codice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
