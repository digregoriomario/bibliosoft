/**
 * @file StatoPrestito.java
 * @author gruppocinque
 * @version 1.0
 */

package gruppocinque.bibliosoft.modelli;

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
    IN_CORSO("In Corso"),
    IN_RITARDO("In Ritardo"),
    CONCLUSO("Concluso");

    private final String descrizione;
    /**
     * @brief Costruttore dell'enum.
     * @details
     * Assegna una descrizione testuale utile per la visualizzazione
     * nelle tabelle dell'interfaccia grafica.
     *
     * @param descrizione Stringa formattata per l'UI (es. "In Corso").
     */
   StatoPrestito(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}
