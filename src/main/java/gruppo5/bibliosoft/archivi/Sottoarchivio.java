package gruppo5.bibliosoft.archivi;

public class Sottoarchivio implements InterfacciaSottoarchivio<T> {

    protected Set<T> elementi;

    public void aggiungi(T elemento) {
    }

    public void rimuovi(T elemento) {
    }

    public List<T> cerca(InterfacciaFiltro<T> filtro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<T> lista() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int conta() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void modifica(T elemento) {
    }
}
