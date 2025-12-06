package gruppo5.bibliosoft.archivi;

/**
 * @brief Interfaccia generica per la gestione di un sotto-archivio.
 * * Definisce le operazioni di aggiunta, modifica, rimuovi fondamentali
 * e le funzionalità di ricerca e conteggio per una collezione di elementi generici.
 * * @param <T> Il tipo di elemento gestito dall'archivio (es. Libro, Utente, Prestito).
 * 
 */
public interface InterfacciaSottoarchivio<T> {

    /**
     * @brief Aggiunge un nuovo elemento all'archivio.
     * 
     * * @param[in] elemento L'elemento da inserire. Non deve essere null.
     * 
     * @pre elemento != null.
     * @pre L'elemento non deve essere già presente nell'archivio.
     * @post L'archivio contiene l'elemento aggiunto.
     * 
     * @throws IllegalStateException Se l'elemento è già presente.
     * @throws NullPointerException se l'elemento è null.
     */
    void aggiungi(T elemento);

    /**
     * @brief Modifica un elemento esistente nell'archivio.
     * 
     * * L'operazione sostituisce l'elemento esistente con la nuova versione fornita.
     * * @param[in,out] elemento L'elemento da aggiornare.
     * 
     * @pre L'elemento deve esistere nell'archivio.
     * @post L'elemento nell'archivio è aggiornato con i nuovi dati.
     */
    void modifica(T elemento);

    /**
     * @brief Rimuove un elemento dall'archivio.
     * 
     * * @param[in] elemento L'elemento da rimuovere.
     * 
     * @pre L'elemento deve essere presente nell'archivio.
     * @post L'archivio non contiene più l'elemento specificato.
     */
    void rimuovi(T elemento);

    /**
     * @brief Restituisce la lista completa degli elementi.
     * 
     * * @return Una lista contenente tutti gli elementi presenti nell'archivio.
     * 
     * @post La lista restituita non è null ma può essere vuota.
     */
    List<T> lista();

    /**
     * @brief Cerca elementi nell'archivio in base a un filtro specificato.
     * 
     * * @param[in] filtro L'oggetto filtro che implementa la logica . Se null, viene restituita l'intera lista.
     * 
     * 
     * @return Una lista di elementi che soddisfano il criterio del filtro.
     * 
     * @see InterfacciaFiltro
     */
    List<T> cerca(InterfacciaFiltro<T> filtro);

    /**
     * @brief Conta il numero totale di elementi presenti.
     * .
     * * @return Il numero intero di elementi nell'archivio.
     */
    int conta();
}