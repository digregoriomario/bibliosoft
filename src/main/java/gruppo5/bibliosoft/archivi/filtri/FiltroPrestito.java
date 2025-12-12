/**
 * @file FiltroPrestito.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.archivi.filtri;

import gruppo5.bibliosoft.modelli.Prestito;
import gruppo5.bibliosoft.modelli.StatoPrestito;

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
       return l -> l.getStato() == StatoPrestito.CONCLUSO;
    }

    /**
     * @brief Crea un filtro per i prestiti attualmente in corso.
     * @details
     * @return Un filtro che seleziona i prestiti con stato IN_CORSO.
     */
    public static InterfacciaFiltro<Prestito> filtraInCorso() {
       return l -> l.getStato() == StatoPrestito.IN_CORSO;
    }

    /**
     * @brief Crea un filtro per i prestiti in ritardo.
     * @details
     * @return Un filtro che seleziona i prestiti con stato IN_RITARDO.
     */
    public static InterfacciaFiltro<Prestito> filtraInRitardo() {
       return l -> l.getStato() == StatoPrestito.IN_RITARDO;
    }

    /**
     * @brief Crea un filtro per tutti i prestiti attivi (In Corso oppure In Ritardo).
     * @details Il filtro sfrutta la negazione dello stato CONCLUSO.
     *
     * @return Un filtro che seleziona i prestiti non ancora conclusi.
     */
    public static InterfacciaFiltro<Prestito> filtraAttivi() {
        return l -> l.getStato() != StatoPrestito.CONCLUSO;  //Se non concluso allora è attivo
    }

    /**
     * @brief Crea un filtro per i prestiti associati a una specifica matricola utente.
     * @details Se la matricola fornita è nulla, vuota o contiene solo spazi, il filtro 
     * accetta tutti i prestiti.
     * @param[in] matricola La matricola dell'utente.
     *
     * @return Un filtro che seleziona i prestiti di un determinato utente.
     */
    public static InterfacciaFiltro<Prestito> ricercaMatricola(String matricola) {
        // Gestione del caso limite: se la stringa è nulla o vuota, non filtra nulla.
        if (matricola == null || matricola.trim().isEmpty()) {
            return l -> true;
        }
        // Filtraggio
        return l -> l.getUtente().getMatricola().equals(matricola); //controllo per matricola
    }

    /**
     * @brief Crea un filtro per i i prestiti attivi di una specifica matricola.
     * @details Combina il filtro per matricola e il filtro per prestiti attivi.
     * Utile per verificare se un utente ha libri ancora da restituire.
     * Se la matricola è nulla o vuota, il filtro restituisce tutti i prestiti attivi.
     * 
     * @param[in] matricola La matricola dell'utente.
     *
     * @return Un filtro che seleziona i prestiti attivi dell'utente
     * specificato.
     */
    public static InterfacciaFiltro<Prestito> ricercaAttiviMatricola(String matricola) {
        InterfacciaFiltro<Prestito> ricercaMatricola = ricercaMatricola(matricola); //Sfrutta ricercaMatricola per controllare se Matricola è null e restituire la lista completa
        InterfacciaFiltro<Prestito> filtraAttivi = filtraAttivi();

        return l -> ricercaMatricola.filtra(l) && filtraAttivi.filtra(l); //controlla per prestiti attivi e matricola 
    }
}
