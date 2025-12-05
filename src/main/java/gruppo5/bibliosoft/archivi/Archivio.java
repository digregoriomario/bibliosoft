package gruppo5.bibliosoft.archivi;

public class Archivio {

    private final Sottoarchivio<Libro> libri;

    private final Sottoarchivio<Utente> utenti;

    private final Sottoarchivio<Prestito> prestiti;

    public void aggiungiLibro(Libro libro) {
    }

    public void modificaLibro(Libro libro) {
    }

    public void rimuoviLibro(Libro libro) {
    }

    public List<Libro> listaLibri() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Libro> cercaLibri(InterfacciaFiltro<Libro> filtro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int contaLibri() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void aggiungiUtente(Utente utente) {
    }

    public void modificaUtente(Utente utente) {
    }

    public void rimuoviUtente(Utente utente) {
    }

    public List<Utente> listaUtenti() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Utente> cercaUtenti(InterfacciaFiltro<Utente> filtro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int contaUtenti() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void aggiungiPrestito(Prestito prestito) {
    }

    public void modificaPrestito(Prestito prestito) {
    }

    public void rimuoviPrestito(Prestito prestito) {
    }

    public List<Prestito> listaPrestiti() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Prestito> cercaPrestiti(InterfacciaFiltro<Prestito> filtro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int contaPrestiti() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
