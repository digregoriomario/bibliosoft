package gruppo5.bibliosoft.archivi.filtri;

/**
 * @file FiltroLibro.java
 * 
 * @brief Classe di utilità per la creazione di filtri applicabili ai Libri.
 * 
 * * Fornisce metodi statici che restituiscono istanze di InterfacciaFiltro<Libro>
 * per effettuare ricerche basate su testo generico o campi specifici come ISBN.
 * 
 * @see InterfacciaFiltro
 */
public class FiltroLibro {

     /**
     * @brief Genera un filtro per una ricerca testuale generica.
     * 
     * * Cerca la corrispondenza della stringa fornita all'interno del titolo,
     * dell'autore o del codice ISBN del libro. La ricerca è case-insensitive.
     * 
     * * @param[in] testo La stringa da ricercare.
     * 
     * @return Un'istanza di InterfacciaFiltro che restituisce true se trova corrispondenze.
     */
    public static InterfacciaFiltro<Libro> ricerca(String testo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     /**
     * @brief Genera un filtro per la ricerca esatta tramite ISBN.
     * 
     * La ricerca è case-insensitive.
     * 
     * * @param[in] codice Il codice ISBN da cercare.
     * 
     * @return Un filtro che restituisce true se l'ISBN corrisponde.
     */
    public static InterfacciaFiltro<Libro> ricercaIsbn(String codice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
