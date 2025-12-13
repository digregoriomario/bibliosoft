/**
 * @file InterfacciaSottoarchivio.java
 * @author gruppocinque
 * @version 1.0
 */
package gruppocinque.bibliosoft.archivi;

import gruppocinque.bibliosoft.archivi.filtri.InterfacciaFiltro;
import java.util.List;

/**
 * @brief Interfaccia generica per la gestione di un sotto-archivio.
 * @details Definisce le operazioni di aggiunta, modifica, rimuovi fondamentali
 * e le funzionalità di ricerca e conteggio per una collezione di elementi
 * generici.
 * @tparam T Il tipo di elemento gestito dall'archivio (es. Libro, Utente,
 * Prestito).
 *
 */
public interface InterfacciaSottoarchivio<T> {

    /**
     * @brief Aggiunge un nuovo elemento all'archivio.
     * @details
     * @param[in] elemento L'elemento da inserire. Non deve essere null.
     *
     * @pre {@code elemento != null}
     * @pre L'elemento non deve essere già presente nell'archivio.
     * @post L'archivio contiene l'elemento aggiunto.
     *
     * @throws IllegalStateException Se l'elemento è già presente.
     * @throws NullPointerException se l'elemento è null.
     */
    void aggiungi(T elemento);

    /**
     * @brief Modifica un elemento esistente nell'archivio.
     * @details L'operazione sostituisce l'elemento esistente con la nuova
     * versione fornita. L'identificazione avviene solitamente tramite ID
     * univoco dell'elemento.
     *
     * @param[in] elemento L'elemento da aggiornare.
     *
     * @pre {@code elemento != null}
     * @pre L'elemento deve esistere nell'archivio.
     * @post L'elemento nell'archivio è aggiornato con i nuovi dati.
     *
     * @throws java.util.NoSuchElementException Se l'elemento da modificare non esiste.
     * @throws NullPointerException Se l'elemento è null.
     */
    void modifica(T elemento);

    /**
     * @brief Rimuove un elemento dall'archivio.
     * @details
     * @param[in] elemento L'elemento da rimuovere.
     *
     * @pre {@code elemento != null}
     * @pre L'elemento deve essere presente nell'archivio.
     * @post L'archivio non contiene più l'elemento specificato.
     *
     * @throws java.util.NoSuchElementException Se l'elemento da rimuovere non esiste.
     * @throws NullPointerException Se l'elemento è null.
     */
    void rimuovi(T elemento);

    /**
     * @brief Restituisce la lista completa degli elementi.
     * @details
     * @return Una lista contenente tutti gli elementi presenti nell'archivio.
     *
     * @post La lista restituita non è null ma può essere vuota.
     */
    List<T> lista();

    /**
     * @brief Cerca elementi nell'archivio in base a un filtro specificato.
     * @details
     * @param[in] filtro L'oggetto filtro che implementa la logica . Se null,
     * viene restituita l'intera lista.
     *
     *
     * @return Una lista di elementi che soddisfano il criterio del filtro.
     *
     * @post Il ritorno è diverso da null.
     *
     * @see filtri.InterfacciaFiltro
     */
    List<T> cerca(InterfacciaFiltro<T> filtro);

    /**
     * @brief Conta il numero totale di elementi presenti.
     * @details
     * @return Il numero intero di elementi nell'archivio.
     *
     * @post {@code risultato >= 0}
     */
    int conta();
}
