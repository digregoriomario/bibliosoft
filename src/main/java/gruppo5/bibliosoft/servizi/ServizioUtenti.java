/**
 * @file ServizioUtenti.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.servizi;

import gruppo5.bibliosoft.archivi.Archivio;
import gruppo5.bibliosoft.archivi.filtri.FiltroUtente;
import gruppo5.bibliosoft.modelli.Utente;
import gruppo5.bibliosoft.strumenti.Validatore;
import java.util.List;

/**
 * @brief Gestisce la logica di business relativa agli Utenti (RF 3.1.2 -
 * Gestione utenti).
 * @details Questa classe funge da intermediario tra il Controller e l'Archivio
 * per le operazioni riguardanti i tesserati della biblioteca. Si occupa della
 * validazione dei dati anagrafici e del rispetto dei vincoli di integrità.
 *
 * @invariant {@code archivio != null}
 */
public class ServizioUtenti {

    private final Archivio archivio;    //attributo archivio

    /**
     * @brief Costruttore del servizio utenti.
     * @details
     * @param[in] archivio Riferimento all'archivio centrale condiviso.
     *
     * @pre {@code archivio != null}
     * @post Attributi correttamente inizializzati.
     */
    public ServizioUtenti(Archivio archivio) {
        this.archivio = archivio;
    }

    /**
     * @brief Registra un nuovo utente nel sistema.
     * @details Implementa il Caso d'Uso 8 (Inserimento nuovo utente) e RF
     * 3.1.2.1 (Inserimento dati). Valida i dati anagrafici tramite la classe
     * Validatore prima dell'inserimento.
     *
     * @param[in] utente L'oggetto Utente da registrare.
     *
     * @pre {@code utente != null}
     * @pre I dati (Matricola, Nome, Cognome, Email) devono rispettare il
     * formato corretto.
     * @pre Non deve esistere un altro utente con la stessa matricola.
     * @post L'utente è aggiunto all'archivio.
     *
     * @throws IllegalArgumentException Se la validazione fallisce.
     * @throws IllegalStateException Se la matricola è già presente.
     *
     * @see Validatore
     */
    public void aggiungiUtente(Utente utente) {
        Validatore.validaUtente(utente);    //se il validatore valida l'utetene (tutti i parametri sono validi)...
        //aggiungi eventuali controlli per vedere se l'utente può essere aggiunto  (nulla penso)
        archivio.aggiungiUtente(utente);    //...allora aggiungo l'utente all'archivio
    }

    /**
     * @brief Modifica i dati di un utente esistente.
     * @details Implementa il Caso d'Uso 9 (Modifica dati utente) e RF 3.1.2.2
     * (Modifica dati).
     *
     * @param[in] utente L'utente con i dati aggiornati.
     *
     * @pre {@code utente != null}
     * @pre I nuovi dati devono essere validi.
     * @pre L'utente deve esistere in archivio.
     * @post I dati dell'utente sono aggiornati nel sistema.
     *
     * @throws IllegalArgumentException Se i nuovi dati non sono validi.
     */
    public void modificaUtente(Utente utente) {
        Validatore.validaUtente(utente);    //se il validatore valida l'utetene (tutti i parametri sono validi)...
        //aggiungi eventuali controlli per vedere se l'utente può essere modificato (nulla penso)
        archivio.modificaUtente(utente);    //...allora modifico l'utente sull'archivio
    }

    /**
     * @brief Rimuove un utente dal sistema.
     * @details Implementa il Caso d'Uso 10 (Cancellazione utente) e RF 3.1.2.3 (Cancellazione dati).
     *
     * Verifica la regola di business: Non è possibile eliminare un utente che
     * ha ancora libri in prestito (prestiti attivi o in ritardo).
     *
     * @param[in] utente L'utente da eliminare.
     *
     * @pre {@code utente != null}
     * @pre {@code utente.haPrestitiAttivi() == false} (Nessun prestito in
     * corso)
     * @post L'utente è rimosso dall'archivio.
     *
     * @throws IllegalStateException Se l'utente ha prestiti attivi (violazione
     * vincolo).
     */
    public void eliminaUtente(Utente utente) {
        if (utente.haPrestitiAttivi())  //se l'utente ha prestiti attivi...
            throw new IllegalStateException("Utente con prestiti attivi: eliminazione negata"); //...lancio un'eccezione di tipo IllegalStateException
        
        archivio.rimuoviUtente(utente); //rimuovo l'utente dall'archivio
    }

    /**
     * @brief Restituisce la lista completa degli utenti.
     * @details Implementa il Caso d'Uso 10 (Visualizzazione lista utenti) e RF
     * 3.1.2.4 (Lista ordinata).
     *
     * @return Lista di tutti gli utenti registrati.
     */
    public List<Utente> listaUtenti() {
        return archivio.listaUtenti();  //prendo l'intera lista dei prestiti dall'archivio
    }

    /**
     * @brief Cerca utenti in base a una stringa di testo.
     * @details Implementa il Caso d'Uso 11 (Ricerca utente) e RF 3.1.2.5
     * (Ricerca). La ricerca avviene per matricola o cognome.
     *
     * @param[in] filtro Stringa di ricerca. Se null o vuota, restituisce tutti
     * gli utenti.
     *
     * @return Lista degli utenti che corrispondono al criterio.
     * @see FiltroUtente
     */
    public List<Utente> cerca(String filtro) {
        return archivio.cercaUtenti(FiltroUtente.ricerca(filtro));  //chiedo all'archivio di cercare tramite il filtro
    }

    /**
     * @brief Restituisce il numero totale di utenti registrati.
     * @details Utilizzato per il Pannello di Controllo (UC1 - Visualizzazione
     * Pannello di Controllo).
     *
     * @return Conteggio utenti.
     */
    public int getUtentiTotali() {
        return archivio.contaUtenti();  //chiedo all'archivio di contare tutti gli utenti
    }

    /**
     * @brief Restituisce il numero di utenti che hanno almeno un prestito
     * attivo.
     * @details Utilizzato per il Pannello di Controllo (UC1 - Visualizzazione
     * Pannello di Controllo).
     *
     * @return Numero di utenti con prestiti in corso o in ritardo.
     * @see FiltroUtente
     */
    public int getUtentiAttivi() {
        return archivio.cercaUtenti(FiltroUtente.ricercaUtentiAttivi()).size(); //chiedo all'archivio di cercare gli utenti attivi (quindi con prestiti "IN_CORSO" o "IN_RITARDO") e li conto
    }
}
