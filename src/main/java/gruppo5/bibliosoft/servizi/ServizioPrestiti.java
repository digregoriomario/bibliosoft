package gruppo5.bibliosoft.servizi;

public class ServizioPrestiti {

    private final Archivio archivio;

    public ServizioPrestiti(Archivio archivio) {
    }

    public Prestito registraPrestito(Utente utente, Libro libro, LocalDate dataPrevista) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void registraRestituzione(Prestito prestito) {
    }

    public void aggiornaRitardi() {
    }

    public List<Prestito> lista() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Prestito> cerca(InterfacciaFiltro<Prestito> filtro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Prestito> storico(Utente utente) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPrestitiInRitardo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPrestitiConclusi() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPrestitiInCorso() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
