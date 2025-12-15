/**
 * @file Archivio.java
 * @author gruppocinque
 * @version 1.0
 */
package gruppocinque.bibliosoft.archivi;

import gruppocinque.bibliosoft.archivi.filtri.InterfacciaFiltro;
import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.modelli.Prestito;
import gruppocinque.bibliosoft.modelli.Utente;
import java.util.List;

/**
 * @brief Classe principale per la gestione dell'archivio della biblioteca.
 * @details Agisce da facciata per i sotto-archivi di Libri, Utenti e Prestiti,
 * centralizzando l'accesso ai dati.
 *
 * @invariant {@code libri != null}
 * @invariant {@code utenti != null}
 * @invariant {@code prestiti != null}
 * @invariant Nessun libro presente due volte (verifica su ISBN).
 * @invariant Nessun utente presente due volte (verifica su matricola).
 */
public class Archivio {
    private final Sottoarchivio<Libro> libri = new Sottoarchivio<>(); //Inizializzo il sottoarchivio libro
    private final Sottoarchivio<Utente> utenti = new Sottoarchivio<>(); //Inizializzo il sottoarchivio utente
    private final Sottoarchivio<Prestito> prestiti = new Sottoarchivio<>(); //Inizializzo il sottoarchivio prestito

    
    
    // GESTIONE LIBRO
    
    
    
    /**
     * @brief Aggiunge un nuovo libro all'archivio.
     * @details Implementa il caso d'uso "Inserimento nuovo libro" (UC3).
     *
     * @param[in] libro Il libro da aggiungere.
     * @pre {@code libro != null}
     * @pre Il libro non deve essere già presente (verifica su ISBN).
     * @post L'archivio contiene l'elemento aggiunto.
     * 
     * @throws IllegalStateException se il libro è già presente (propagata dal sottoarchivio libri)
     * @throws NullPointerException se {@code libro == null} (propagata dal sottoarchivio libri)
     * 
     * @see Sottoarchivio
     */
    public void aggiungiLibro(Libro libro) {
        libri.aggiungi(libro);
    }

    /**
     * @brief Modifica i dati di un libro esistente.
     * @details Implementa il caso d'uso "Modifica dati libro" (UC4).
     *
     * @param[in] libro L'oggetto libro con i dati aggiornati.
     *
     * @pre {@code libro != da null}
     * @pre Il libro deve esistere all'interno dell'archivio.
     * @post Il libro nell'archivio è aggiornato con i nuovi dati.
     * 
     * @throws NoSuchElementException se il libro da modificare non esiste (propagata dal sottoarchivio libri)
     * @throws NullPointerException se {@code libro == null} (propagata dal sottoarchivio libri)
     * 
     * @see Sottoarchivio
     */
    public void modificaLibro(Libro libro) {
        libri.modifica(libro);
    }

    /**
     * @brief Rimuove un libro dall'archivio.
     * @details Implementa il caso d'uso "Cancellazione libro" (UC5).
     *
     * @param[in] libro Il libro da rimuovere.
     *
     * @pre {@code libro != null}
     * @pre Il libro deve essere presente nell'archivio.
     * @pre Nessun prestito attivo associato a questo libro
     * @post L'archivio non contiene più il libro specificato.
     * 
     * @throws NoSuchElementException se il libro da rimuovere non esiste (propagata dal sottoarchivio libri)
     * @throws NullPointerException se {@code libro == null} (propagata dal sottoarchivio libri)
     * 
     * @see Sottoarchivio
     */
    public void rimuoviLibro(Libro libro) {
        libri.rimuovi(libro);
    }

    /**
     * @brief Restituisce la lista di tutti i libri.
     * @details Implementa il requisito funzionale 3.1.1.4 "Lista ordinata".
     *
     * @return Lista di oggetti Libro.
     * @post {@code risultato != null}
     */
    public List<Libro> listaLibri() {
        return libri.lista();
    }

    /**
     * @brief Cerca libri in base a un filtro specifico.
     * @details Implementa il requisito funzionale 3.1.1.5 "Ricerca"e il caso d'uso "Ricerca libro" (UC6).
     *
     * @param[in] filtro Il filtro da applicare. Se null restituisce tutti ilibri.
     *
     * @return Lista dei libri trovati che soddisfano il filtro.
     * @post {@code risultato != null}
     */
    public List<Libro> cercaLibri(InterfacciaFiltro<Libro> filtro) {
        return libri.cerca(filtro);
    }

    /**
     * @brief Restituisce il numero totale di libri in archivio.
     *
     * @return Conteggio libri.
     *
     * @post {@code risultato >= 0}
     */
    public int contaLibri() {
         return libri.conta();
    }

    
    
    // GESTIONE UTENTE
    
    
    /**
     * @brief Registra un nuovo utente nel sistema.
     * @details Implementa il caso d'uso "Inserimento nuovo utente" (UC8).
     *
     * @param[in] utente L'utente da registrare.
     *
     * @pre {@code utente != null}
     * @pre L'utente non deve essere già presente (verifica su matricola).
     * @pre L'e-mail dell'utente deve essere conforme ai requisiti istituzionali (Req. 3.3.2).
     * @post L'archivio contiene l'utente aggiunto.
     * 
     * @throws IllegalStateException se l'utente è già presente (propagata dal sottoarchivio utenti)
     * @throws NullPointerException se {@code utente == null} (propagata dal sottoarchivio utenti)
     * 
     * @see Sottoarchivio
     */
    public void aggiungiUtente(Utente utente) {
        utenti.aggiungi(utente);
    }

    /**
     * @brief Modifica i dati di un utente.
     * @details Implementa il caso d'uso "Modifica dati utente" (UC9).
     *
     * @param[in] utente L'utente con i dati aggiornati.
     *
     * @pre {@code utente != null}
     * @pre L'utente deve esistere all'interno dell'archivio.
     * @post L'utente nell'archivio è aggiornato con i nuovi dati.
     * 
     * @throws NoSuchElementException se l'utente da modificare non esiste (propagata dal sottoarchivio utenti)
     * @throws NullPointerException se {@code utente == null} (propagata dal sottoarchivio utenti)
     * 
     * @see Sottoarchivio
     */
    public void modificaUtente(Utente utente) {
        utenti.modifica(utente);
    }

    /**
     * @brief Rimuove un utente dal sistema.
     * @details Implementa il caso d'uso "Cancellazione utente" (UC10).
     *
     * @param[in] utente L'utente da rimuovere.
     *
     * @pre {@code utente != null}
     * @pre L'utente deve essere presente nell'archivio.
     * @pre Nessun prestito attivo o in ritardo associato a questo utente.
     * @post L'archivio non contiene più l'utente specificato.
     * 
     * @throws NoSuchElementException se l'utente da rimuovere non esiste (propagata dal sottoarchivio utenti)
     * @throws NullPointerException se {@code utente == null} (propagata dal sottoarchivio utenti)
     * 
     * @see Sottoarchivio
     */
    public void rimuoviUtente(Utente utente) {
        utenti.rimuovi(utente);
    }

    /**
     * @brief Restituisce l'elenco completo degli utenti registrati.
     * @details Implementa il requisito funzionale 3.1.2.4 "Lista ordinata".
     *
     * @return Lista di oggetti Utente.
     * @post {@code risultato >= 0}
     */
    public List<Utente> listaUtenti() {
        return utenti.lista();
    }

    /**
     * @brief Cerca utenti tramite filtro (es matricola, nome).
     * @details Implementa il requisito funzionale 3.1.2.5 "Ricerca" e il caso d'uso "Ricerca utente" (UC11).
     *
     * @param[in] filtro Filtro di ricerca utenti.
     *
     * @return Lista degli utenti trovati.
     * @post {@code risultato >= 0}
     */
    public List<Utente> cercaUtenti(InterfacciaFiltro<Utente> filtro) {
        return utenti.cerca(filtro);
    }

    /**
     * @brief Restituisce il numero totale di utenti registrati.
     * @details
     * @return Conteggio utenti.
     * @post {@code risultato >= 0}
     */
    public int contaUtenti() {
        return utenti.conta();
    }

    
    
    //GESTIONE PRESTITI
    
    
    /**
     * @brief Registra un nuovo prestito.
     * @details Implementa il caso d'uso "Registrazione prestito" (UC14).
     *
     * @param[in] prestito Il prestito da registrare.
     *
     * @pre {@code prestito != null}
     * @pre Ci sono libri e utenti in archivio.
     * @post Aggiunta prestito in archivio.
     * 
     * @throws IllegalStateException se il prestito è già presente (propagata dal sottoarchivio prestiti)
     * @throws NullPointerException se {@code prestito == null} (propagata dal sottoarchivio prestiti)
     * 
     * @see Sottoarchivio
     */
    public void aggiungiPrestito(Prestito prestito) {
        prestiti.aggiungi(prestito); 
    }

    /**
     * @brief Aggiorna i dati di un prestito.
     * @details Implementa il caso d'uso "Registrazione restituzione prestito" (UC15).
     *
     * @param[in] prestito Il prestito da aggiornare.
     *
     * @pre {@code prestito != null}
     * @pre Ci sono prestiti in archivio.
     * @post Modifica dello stato e della data effettiva del prestito.
     * 
     * @throws NoSuchElementException se il prestito da modificare non esiste (propagata dal sottoarchivio prestiti)
     * @throws NullPointerException se {@code prestito == null} (propagata dal sottoarchivio prestiti)
     * 
     * @see Sottoarchivio
     */
    public void modificaPrestito(Prestito prestito) {
         prestiti.modifica(prestito);
    }

    /**
     * @brief Rimuove un record di prestito (non per lo storico).
     * @details **Non utilizzare questo metodo per la registrazione della restituzione del libro(per quello usare {@code modificaPrestito(Prestito)} e aggiornare lo stato).**
     * @param[in] prestito Il prestito da rimuovere.
     *
     * @pre {@code prestito != null}
     * @pre Ci sono prestiti in archivio.
     * @post L'archivio non contiene il prestito selezionato.
     * 
     * @throws NoSuchElementException se il prestito da rimuovere non esiste (propagata dal sottoarchivio prestiti)
     * @throws NullPointerException se {@code prestito == null} (propagata dal sottoarchivio prestiti)
     * 
     * @see Sottoarchivio
     */
    public void rimuoviPrestito(Prestito prestito) {
        prestiti.rimuovi(prestito);
    }

    /**
     * @brief Restituisce lo storico completo dei prestiti.
     * @details Utile per l'analisi e lo storico complessivo.
     * @return Lista di tutti i prestiti (in corso e conclusi).
     * @post {@code risultato >= 0}
     */
    public List<Prestito> listaPrestiti() {
        return prestiti.lista();
    }

    /**
     * @brief Cerca prestiti o filtra per stato (es in ritardo, in corso).
     * @details Utilizzabile anche per l'implementazione dello "Storico prestiti" (UC13).
     * @param[in] filtro Filtro sui prestiti.
     *
     * @return Lista dei prestiti che soddisfano il criterio.
     * @post {@code risultato >= 0}
     */
    public List<Prestito> cercaPrestiti(InterfacciaFiltro<Prestito> filtro) {
        return prestiti.cerca(filtro);
    }

    /**
     * @brief Conta il numero totale di operazioni di prestito registrate.
     * @details 
     * @return Conteggio prestiti.
     * @post {@code risultato >= 0}
     */
    public int contaPrestiti() {
        return prestiti.conta();
    }
}
