/**
 * @file FiltroUtente.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.archivi.filtri;

import gruppo5.bibliosoft.modelli.Utente;

/**
 * @brief Classe di utilità per la creazione di filtri applicabili agli Utenti.
 * @details Fornisce metodi per ricercare utenti tramite matricola, cognome o
 * stato di attività. Questa classe supporta il requisito funzionale di Ricerca Utente (3.1.2.5).
 *
 * @see InterfacciaFiltro.
 */
public class FiltroUtente {

    /**
     * @brief Crea un filtro per la ricerca di un utente tramite corrispondenza
     * esatta della matricola.
     * @details La ricerca è case-insensitive. Se la matricola fornita è nulla, vuota 
     * o contiene solo spazi, il filtro accetta tutti gli elementi.
     *
     * @param[in] matricola La matricola da cercare.
     *
     * @return Un filtro per la matricola specificata.
     */
    public static InterfacciaFiltro<Utente> ricercaMatricola(String matricola) {
        // Gestione del caso limite: se la stringa è nulla o vuota, non filtra nulla.
        if (matricola == null || matricola.trim().isEmpty()) {
            return l -> true; 
        }
        // Filtraggio
        return l-> l.getMatricola().equals(matricola); //controlla per matricola
    }

    /**
     * @brief Crea un filtro per la ricerca generica su cognome o matricola.
     * @details Verifica se la stringa fornita è contenuta (parzialmente) nel
     * cognome o nella matricola dell'utente. La ricerca è case-insensitive.
     * Se l'input è nullo, vuoto o composto solo da spazi, il filtro accetta tutti gli elementi.
     *
     * @param[in] stringaFiltro La stringa da cercare.
     *
     * @return Un filtro che restituisce true se la stringa è contenuta nel
     * cognome o nella matricola dell'utente.
     */
    public static InterfacciaFiltro<Utente> ricerca(String stringaFiltro) {
        // Gestione del caso limite: se la stringa è nulla o vuota, non filtra nulla.
        if (stringaFiltro == null || stringaFiltro.trim().isEmpty()) {
            return l -> true;
        }
        
        // FIltraggio
        return l -> l.getCognome().toLowerCase().contains(stringaFiltro.toLowerCase()) //controlla per cognome
                || l.getMatricola().contains(stringaFiltro);                           //controlla per matricola
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
        return l -> ! l.getPrestitiAttivi().isEmpty(); //se la lista dei prestiti attivi non è vuota allora è attivo
    }
}
