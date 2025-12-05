package gruppo5.bibliosoft.modelli;

public class Libro {

    private final String isbn;

    private String titolo;

    private List<String> autori;

    private int annoPubblicazione;

    private int copieTotali;

    private int copieDisponibili;

    public Libro(String isbn, String titolo, List<String> autori, int annoPubblicazione, int copieTotali) {
    }

    public String getIsbn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getTitolo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTitolo(String titolo) {
    }

    public List<String> getAutori() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAutori(List<String> autori) {
    }

    public int getAnnoPubblicazione() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
    }

    public int getCopieTotali() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCopieTotali(int copieTotali) {
    }

    public int getCopieDisponibili() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCopieDisponibili(int copieDisponibili) {
    }

    public boolean isDisponibile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean equals(Object oggetto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(Libro libro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean contieneAutore(String filtroAutore) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
