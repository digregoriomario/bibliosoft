package gruppo5.bibliosoft.modelli;

public class Utente {

    private final String matricola;

    private String nome;

    private String cognome;

    private String email;

    private List<Prestito> prestitiAttivi;

    public Utente(String matricola, String nome, String cognome, String email) {
    }

    public String getMatricola() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNome(String nome) {
    }

    public String getCognome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCognome(String cognome) {
    }

    public String getEmail() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEmail(String email) {
    }

    public List<Prestito> getPrestitiAttivi() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void aggiungiPrestito(Prestito prestito) {
    }

    public void rimuoviPrestito(Prestito prestito) {
    }

    public boolean haPrestitiAttivi() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean equals(Object oggetto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(Utente utente) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
