/**
 * @file Sottoarchivio.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.archivi;

import gruppo5.bibliosoft.archivi.filtri.InterfacciaFiltro;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * @brief Implementazione concreta di un sotto-archivio in memoria.
 * @details Utilizza un TreeSet per mantenere gli elementi ordinati
 * automaticamente in base al loro ordinamento naturale (interfaccia
 * Comparable). Gestisce la persistenza in memoria volatile delle entità.
 *
 * @invariant {@code elementi != null}
 * @invariant {@code elementi non contiene duplicati}
 *
 * @tparam T Tipo dell'elemento, deve implementare Comparable per l'ordinamento nel TreeSet.
 * @see InterfacciaSottoarchivio
 */
public class Sottoarchivio<T> implements InterfacciaSottoarchivio<T> {

    /**
     * @brief Collezione ordinata degli elementi.
     * @details Utilizza un TreeSet per garantire l'ordinamento e l'unicità.
     */
    protected Set<T> elementi = new TreeSet<>(); ;

    /**
     * @brief Aggiunge un elemento alla collezione verificando l'unicità.
     * @details
     * @param[in] elemento L'elemento da aggiungere.
     *
     * @pre {@code elemento != null}
     * @pre elemnti non deve contenere elemento.
     * @post L'archivio contiene l'elemento aggiunto.
     *
     * @throws IllegalStateException se l'elemento è già presente.
     * @throws NullPointerException se l'elemento è null.
     */
    public void aggiungi(T elemento) {
        if (elemento == null) { // GESTIONE NULL
            throw new NullPointerException("Impossibile aggiungere un elemento nullo.");
        }
        
        // Uso il valore di ritorno di add() per verificare l'unicità in modo efficiente
        if (!elementi.add(elemento)) {
            throw new IllegalStateException(elemento.getClass().getSimpleName() + " già presente.");
        }
    }

    /**
     * @brief Rimuove un elemento dalla collezione.
     * @details
     * @param[in] elemento L'elemento da rimuovere.
     *
     * @pre {@code elemento != null}
     * @pre L'elemento deve essere presente nell'archivio.
     * @post L'archivio non contiene più l'elemento specificato.
     * 
     * @throws NoSuchElementException Se l'elemento da rimuovere non esiste.
     * @throws NullPointerException se l'elemento è null.
     */
    public void rimuovi(T elemento) {
         if (elemento == null) { // Gestione elemento null
            throw new NullPointerException("Impossibile rimuovere un elemento nullo.");
        }
        
        // Controllo se l'elemento è stato rimosso (cioè se era presente)
        if (!elementi.remove(elemento)) {
            throw new NoSuchElementException(elemento.getClass().getSimpleName() + " non trovato nell'archivio.");
        }
    }

    /**
     * @brief Esegue una ricerca filtrata sugli elementi.
     * @details Itera sulla collezione e applica il metodo filtra
     * dell'interfaccia InterfacciaFiltro.
     * @param[in] filtro criterio di filtraggio. Se null, restituisce tutti gli
     * elementi.
     *
     * @return Lista degli elementi che soddisfano il filtro.
     * @post {@code risultato >= 0}
     */
    public List<T> cerca(InterfacciaFiltro<T> filtro) {
        if (filtro == null) { // Gestione filtro null
            return lista();
        }
         List<T> risultati = new ArrayList<>();  //creo una lista per il risultato della ricerca
        
        for(T elemento : elementi)  //per ogni elemento della collezzione...
            if(filtro.filtra(elemento))  //controllo se rispetta il filtro
                risultati.add(elemento);    //lo aggiungo alla lista dei risultati
        
        return risultati;
    }

    /**
     * @brief Restituisce una copia della lista degli elementi.
     * @details
     * @return ArrayList contenente gli elementi ordinati.
     * @post {@code risultato != null}
     */
    public List<T> lista() {
        return new ArrayList<>(elementi);
    }

    /**
     * @brief Restituisce il numero di elementi nel sotto-archivio.
     * @details
     * @return Numero di elementi presenti.
     *
     * @post {@code risultato >= 0}
     */
    public int conta() {
        return elementi.size();
    }

    /**
     * @brief Modifica un elemento aggiornandone i dati.
     * @details Poiché gli elementi sono in un Set, la modifica richiede la
     * rimozione del vecchio oggetto e l'inserimento del nuovo per aggiornare
     * l'ordinamento.
     *
     * @param[in] elemento L'elemento aggiornato.
     *
     * @pre {@code elemento != null}
     * @pre L'elemento deve esistere nell'archivio
     * @post L'elemento nell'archivio è aggiornato con i nuovi dati.
     * 
     * @throws NoSuchElementException se l'elemento da modificare non esiste.
     * @throws NullPointerException se l'elemento è null.
     */
    public void modifica(T elemento) {
        if (elemento == null) { // Controllo elemento null
            throw new NullPointerException("Impossibile modificare un elemento nullo.");
        }

        // Rimuovo il vecchio oggetto. Se remove ritorna false, l'oggetto non esisteva.
        if (!elementi.remove(elemento)) {
             throw new NoSuchElementException(elemento.getClass().getSimpleName() + " non trovato nell'archivio.");
        }

        //Aggiungo il nuovo oggetto
        elementi.add(elemento);
    }
}
