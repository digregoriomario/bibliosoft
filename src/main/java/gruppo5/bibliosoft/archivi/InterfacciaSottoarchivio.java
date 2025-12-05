package gruppo5.bibliosoft.archivi;

public interface InterfacciaSottoarchivio {

    void aggiungi(T elemento);

    void modifica(T elemento);

    void rimuovi(T elemento);

    List<T> lista();

    List<T> cerca(InterfacciaFiltro<T> filtro);

    int conta();
}
