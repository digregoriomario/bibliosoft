package gruppo5.bibliosoft.modelli;

/**
 * @file StatoPrestito.java
 * 
 * @brief Enumerazione che definisce i possibili stati del ciclo di vita di un prestito.
 *
 * Questa enum mappa rigorosamente i valori di dominio definiti nel requisito "Stato prestito".
 * Viene utilizzata dalla classe Prestito per tracciare l'evoluzione temporale dell'associazione
 * tra utente e libro.
 *
 * I valori ammessi sono tassativi e derivano direttamente dalle specifiche di analisi.
 */
public enum StatoPrestito {

    IN_CORSO, IN_RITARDO, CONCLUSO
    
    /**
     * @brief Costruttore dell'enum.
     *
     * Assegna una descrizione testuale utile per la visualizzazione
     * nelle tabelle dell'interfaccia grafica.
     *
     * @param descrizione Stringa formattata per l'UI (es. "In Corso").
     */
      StatoPrestito(String descrizione) {

    }
}
