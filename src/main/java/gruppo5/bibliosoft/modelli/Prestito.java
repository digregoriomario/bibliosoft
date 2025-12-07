package gruppo5.bibliosoft.modelli;

/**
 * @file Prestito.java
 * @brief Classe che rappresenta l'associazione temporale tra un Utente e un Libro.
 *
 * Gestisce il ciclo di vita del prestito, dalla creazione alla restituzione,
 * monitorando le scadenze e lo stato corrente.
 *
 * @invariant id != null && id != ""
 * @invariant utente != null
 * @invariant libro != null
 * @invariant dataInizio != null && dataPrevista != null
 * @invariant dataInizio <= dataPrevista
 */

public class Prestito {

    private String id;

    private Utente utente;

    private Libro libro;

    private LocalDate dataInizio;

    private LocalDate dataPrevista;

    private LocalDate dataRestituzioneEffettiva;

    private StatoPrestito stato;

    /**
     * @brief Costruttore della classe Prestito.
     *
     * Crea una nuova istanza di prestito attiva.
     *
     * @param[in] utente L'utente che richiede il prestito.
     * @param[in] libro Il libro oggetto del prestito.
     * @param[in] dataInizio La data di inizio del prestito.
     * @param[in] dataPrevista La data entro cui il libro deve essere restituito.
     *
     * @post stato != null (Lo stato iniziale è impostato di default).
     */
    public Prestito(Utente utente, Libro libro, LocalDate dataInizio, LocalDate dataPrevista) {
    }

    public String getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Utente getUtente() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setUtente(Utente utente) {
    }

    public Libro getLibro() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLibro(Libro libro) {
    }

    public LocalDate getDataInizio() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDataInizio(LocalDate dataInizio) {
    }

    public LocalDate getDataPrevista() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDataPrevista(LocalDate dataPrevista) {
    }

    public LocalDate getDataRestituzioneEffettiva() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Imposta la data di restituzione effettiva.
     * 
     * @param[in] dataRestituzioneEffettiva La data in cui il libro è rientrato (o null).
     */
    public void setDataRestituzioneEffettiva(LocalDate dataRestituzioneEffettiva) {
    }

    public StatoPrestito getStato() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setStato(StatoPrestito stato) {
    }

    /**
     * @brief Aggiorna lo stato del prestito in base alla data odierna.
     *
     * Verifica se il prestito è scaduto o restituito e aggiorna il campo 'stato'.
     *
     * @param[in] oggi La data di riferimento per il calcolo.
     * @pre oggi != null
     */
    public void aggiornaStato(LocalDate oggi) {
    }

    /**
     * @brief Verifica l'uguaglianza logica tra due prestiti.
     *
     * Basata sull'identificativo univoco (ID) del prestito.
     *
     * @param[in] oggetto L'oggetto da confrontare.
     * @return true se gli ID coincidono.
     */
    public boolean equals(Object oggetto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @brief Confronta due prestiti per ordinamento.
     *
     * Definisce un criterio di ordine, utile per visualizzare liste di prestiti.
     * L'ordinamento primario è sulla data prevista di restituzione (priorità alle scadenze).
     *
     * @param[in] prestito Il prestito con cui confrontare l'istanza corrente.
     * @return Valore negativo, zero o positivo in base alla data prevista.
     */
    public int compareTo(Prestito prestito) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
