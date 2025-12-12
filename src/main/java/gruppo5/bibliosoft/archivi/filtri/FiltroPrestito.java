/**
 * @file FiltroPrestito.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.archivi.filtri;

import gruppo5.bibliosoft.modelli.Prestito;

/**
 * @brief Classe di utilità per la creazione di filtri applicabili ai Prestiti.
 * @details Permette di filtrare i prestiti in base al loro stato (Concluso, In
 * Corso, In Ritardo) o in base alla matricola dell'utente associato.
 *
 * @see InterfacciaFiltro
 */
public class FiltroPrestito {

    /**
     * @brief Crea un filtro per i prestiti conclusi.
     * @details
     * @return Un filtro che seleziona i prestiti con stato CONCLUSO.
     */
    public static InterfacciaFiltro<Prestito> filtraConclusi() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Crea un filtro per i prestiti attualmente in corso.
     * @details
     * @return Un filtro che seleziona i prestiti con stato IN_CORSO.
     */
    public static InterfacciaFiltro<Prestito> filtraInCorso() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Crea un filtro per i prestiti in ritardo.
     * @details
     * @return Un filtro che seleziona i prestiti con stato IN_RITARDO.
     */
    public static InterfacciaFiltro<Prestito> filtraInRitardo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Crea un filtro per tutti i prestiti attivi (In Corso oppure In Ritardo).
     * @details Combina i filtri In Corso e In Ritardo.
     *
     * @return Un filtro che seleziona i prestiti non ancora conclusi.
     */
    public static InterfacciaFiltro<Prestito> filtraAttivi() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Crea un filtro per i prestiti associati a una specifica matricola utente.
     * @details
     * @param[in] matricola La matricola dell'utente.
     *
     * @return Un filtro che seleziona i prestiti di un determinato utente.
     */
    public static InterfacciaFiltro<Prestito> ricercaMatricola(String matricola) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Crea un filtro per i i prestiti attivi di una specifica matricola.
     * @details Combina il filtro per matricola e il filtro per prestiti attivi.
     * Utile per verificare se un utente ha libri ancora da restituire.
     *
     * @param[in] matricola La matricola dell'utente.
     *
     * @return Un filtro che seleziona i prestiti attivi dell'utente
     * specificato.
     */
    public static InterfacciaFiltro<Prestito> ricercaAttiviMatricola(String matricola) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
