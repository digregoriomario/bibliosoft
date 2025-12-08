/**
 * @file StatoPrestito.java
 * @author gruppo5
 * @version 1.0
 */

package gruppo5.bibliosoft.modelli;

/**
 * @brief Enumerazione che definisce i possibili stati del ciclo di vita di un prestito.
 * @details
 * Questa enum mappa rigorosamente i valori di dominio definiti nel requisito "Stato prestito".
 * Viene utilizzata dalla classe Prestito per tracciare l'evoluzione temporale dell'associazione
 * tra utente e libro.
 *
 * I valori ammessi sono tassativi e derivano direttamente dalle specifiche di analisi.
 */
public enum StatoPrestito {
    private final String descrizione;

    IN_CORSO, IN_RITARDO, CONCLUSO
    
    /**
     * @brief Costruttore dell'enum.
     * @details
     * Assegna una descrizione testuale utile per la visualizzazione
     * nelle tabelle dell'interfaccia grafica.
     *
     * @param descrizione Stringa formattata per l'UI (es. "In Corso").
     */
    StatoPrestito(String descrizione) {

    }
}
