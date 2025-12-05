package gruppo5.bibliosoft.modelli;

public class Prestito {

    private String id;

    private Utente utente;

    private Libro libro;

    private LocalDate dataInizio;

    private LocalDate dataPrevista;

    private LocalDate dataRestituzioneEffettiva;

    private StatoPrestito stato;

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

    public void setDataRestituzioneEffettiva(LocalDate dataRestituzioneEffettiva) {
    }

    public StatoPrestito getStato() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setStato(StatoPrestito stato) {
    }

    public void aggiornaStato(LocalDate oggi) {
    }

    public boolean equals(Object oggetto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(Prestito prestito) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
