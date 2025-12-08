/**
 * @file FiltroUtente.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.archivi.filtri;

/**
 * @brief Classe di utilità per la creazione di filtri applicabili agli Utenti.
 * @details Fornisce metodi per ricercare utenti tramite matricola, cognome o
 * stato di attività.
 *
 * @see InterfacciaFiltro.
 */
public class FiltroUtente {

    /**
     * @brief Crea un filtro per la ricerca di un utente tramite corrispondenza
     * esatta della matricola.
     * @details La ricerca è case-insensitive.
     *
     * @param[in] matricola La matricola da cercare.
     *
     * @return Un filtro per la matricola specificata.
     */
    public static InterfacciaFiltro<Utente> ricercaMatricola(String matricola) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Crea un filtro per la ricerca generica su cognome o matricola.
     * @details Verifica se la stringa fornita è contenuta (parzialmente) nel
     * cognome o nella matricola dell'utente. La ricerca è case-insensitive.
     *
     * @param[in] stringaFiltro La stringa da cercare.
     *
     * @return Un filtro che restituisce true se la stringa è contenuta nel
     * cognome o nella matricola dell'utente.
     */
    public static InterfacciaFiltro<Utente> ricerca(String stringaFiltro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Crea un filtro per la ricerca degli utenti che hanno prestiti
     * attivi.
     * @details Verifica se la lista dei prestiti attivi dell'utente non è
     * vuota.
     *
     * @return Un filtro che seleziona gli utenti con almeno un prestito in
     * corso o in ritardo.
     */
    public static InterfacciaFiltro<Utente> ricercaUtentiAttivi() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
