/**
 * @file ServizioPrestiti.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.servizi;

import gruppo5.bibliosoft.archivi.Archivio;
import gruppo5.bibliosoft.archivi.filtri.InterfacciaFiltro;
import gruppo5.bibliosoft.archivi.filtri.FiltroPrestito;
import gruppo5.bibliosoft.modelli.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @brief Gestisce la logica di business relativa ai Prestiti (RF 3.1.3 -
 * Gestione Prestiti).
 * @details Questa classe coordina le operazioni di prestito e restituzione,
 * garantendo la coerenza tra la disponibilità dei libri e lo stato degli
 * utenti. Gestisce inoltre il monitoraggio dei ritardi.
 *
 * @invariant {@code archivio != null}
 */
public class ServizioPrestiti {
    public static final int MAX_PRESTITI_ATTIVI = 3;    //attributo costante statico che contiene il numero di prestiti attivi massimo per un utente
    private final Archivio archivio;    //attributo archivio

    /**
     * @brief Costruttore del servizio prestiti.
     * @details
     * @param[in] archivio L'archivio centrale su cui operare.
     *
     * @pre {@code archivio != null}
     * @post attributi correttamente inizializzati.
     */
    public ServizioPrestiti(Archivio archivio) {
        this.archivio = archivio;
    }

    /**
     * @brief Registra un nuovo prestito nel sistema.
     * @details Implementa il Caso d'Uso 13 (Registrazione prestito) e RF
     * 3.1.3.1 (Disponibilità libri). Verifica le regole di business:
     * disponibilità copie e limite prestiti utente.
     *
     * @param[in] utente L'utente che richiede il prestito.
     * @param[in] libro Il libro da prestare.
     * @param[in] dataPrevista La data prevista per la restituzione.
     *
     * @pre {@code utente != null && libro != null}
     * @pre {@code utente.getPrestitiAttivi().size() < 3} (Limite massimo
     * prestiti simultanei)
     * @pre {@code libro.isDisponibile() == true} (Deve esserci almeno una copia
     * fisica)
     *
     * @post {@code utente.getPrestitiAttivi()} contiene il nuovo prestito
     *
     * @throws IllegalStateException Se l'utente ha troppi prestiti o il libro
     * non è disponibile.
     */
    public void registraPrestito(Utente utente, Libro libro, LocalDate dataPrevista) {
        if(utente.getPrestitiAttivi().size() >= MAX_PRESTITI_ATTIVI)  //se l'utente ha raggiunto il numero di prestiti attivi massimo o più...
            throw new IllegalStateException("L'utente ha già " + MAX_PRESTITI_ATTIVI +" prestiti attivi");   //...lancio l'eccezione di tipo IllegalStateException
        
        if(! libro.isDisponibile()) //se il libro non è disponibile...
            throw new IllegalStateException("Copie non disponibili");   //...lancio l'eccezione di tipo IllegalStateException
        

        Prestito prestito = new Prestito(utente, libro, LocalDate.now(), dataPrevista); //creo l'istanza di Prestito con i parametri
        archivio.aggiungiPrestito(prestito);    //aggiungo il prestito all'archivio

        libro.setCopieDisponibili(libro.getCopieDisponibili() - 1); //decremento il numero di copie disponibili relative a quello specifo libro
        utente.aggiungiPrestito(prestito);  //aggiungo il prestito alla lista dei prestiti attivi del relativo utente (l'utente che ha effettuato il prestito)
    }

    /**
     * @brief Registra la restituzione di un libro.
     * @details Implementa il Caso d'Uso 14 (Registrazione restituzione) e RF
     * 3.1.3.2 (Monitoraggio prestiti).
     *
     * Chiude il prestito, aggiorna la data effettiva e ripristina la
     * disponibilità del libro.
     *
     * @param[in] prestito Il prestito da chiudere.
     *
     * @pre {@code prestito != null}
     * @pre {@code prestito.getStato() != CONCLUSO} (Non si può restituire due
     * volte)
     *
     * @post {@code prestito.getStato() = CONCLUSO}
     * @post Il prestito viene rimosso dalla lista dei prestiti attivi
     * dell'utente.
     *
     * @throws IllegalStateException Se il prestito è già concluso.
     */
    public void registraRestituzione(Prestito prestito) {
        if (prestito.getStato() == StatoPrestito.CONCLUSO)  //se lo stato del prestito è gia "CONCLUSO"...
            throw new IllegalStateException("Prestito concluso");   //...allora lancio l'eccezione di tipo IllegalStateException

        prestito.setDataRestituzioneEffettiva(LocalDate.now()); //registro la data di restituzione effettiva come oggi
        prestito.setStato(StatoPrestito.CONCLUSO);  //imposto lo stato del prestito su "CONCLUSO"
        prestito.getLibro().setCopieDisponibili(prestito.getLibro().getCopieDisponibili() + 1);  //incremento il numero di copie disponibili relative a quello specifo libro
        prestito.getUtente().rimuoviPrestito(prestito); //rimuovo il prestito dalla lista dei prestiti attivi del relativo utente
        archivio.modificaPrestito(prestito);    //per poter registrare le modifiche effettive devo modificare il prestito nell'archivio
    }

    /**
     * @brief Aggiorna lo stato dei prestiti attivi.
     * @details Supporta il Caso d'Uso 15 (Monitoraggio prestiti).
     *
     * Scorre tutti i prestiti attivi e confronta la data odierna con la data
     * prevista.
     *
     * @post I prestiti scaduti vengono impostati allo stato IN_RITARDO.
     */
    public void aggiornaRitardi() {
        LocalDate oggi = LocalDate.now();   //prendo la data di oggi
        for(Prestito prestito : archivio.cercaPrestiti(FiltroPrestito.filtraAttivi())){ //per ogni prestito prendendo dall'archivio solo quelli attivi (quindi sia "IN_CORSO" e "IN_RITARDO")...
            prestito.aggiornaStato(oggi);   //...aggiorno lo stato passando la data di oggi
            archivio.modificaPrestito(prestito);    //...registro le eventuali modifiche allo stato
        }
    }

    /**
     * @brief Restituisce la lista completa dei prestiti.
     * @details Prima di restituire la lista, aggiorna i ritardi per garantire
     * dati freschi.
     *
     * @return Lista di tutti i prestiti presenti in archivio.
     */
    public List<Prestito> lista() {
        aggiornaRitardi();  //prima di fornire la lista devo aggiornare i ritardi dei singoli prestiti
        return archivio.listaPrestiti();    //restituisco l'intera lista dei prestiti presa dall'archivio
    }

    /**
     * @brief Cerca prestiti in base a un filtro specifico.
     * @details
     * @param[in] filtro Criterio di filtraggio.
     *
     * @return Lista dei prestiti che soddisfano il filtro.
     * @see archivi.filtri.FiltroPrestito
     */
    public List<Prestito> cerca(InterfacciaFiltro<Prestito> filtro) {
        return archivio.cercaPrestiti(filtro);  //restituisco la ricerca fatta dall'archivio tramite il filtro passato come parametro
    }

    /**
     * @brief Restituisce lo storico dei prestiti di un utente specifico.
     * @details Implementa il Caso d'Uso 12 (Storico prestiti).
     *
     * @param[in] utente L'utente di cui visualizzare lo storico.
     *
     * @return Lista dei prestiti associati alla matricola dell'utente.
     */
    public List<Prestito> storico(Utente utente) {
        return archivio.cercaPrestiti(FiltroPrestito.ricercaMatricola(utente.getMatricola())); //restituisco tutti i prestiti (di tutti e 3 i possibili stati) cercando nell'archivio tramite il filtro per matricola dell'utente
    }

    /**
     * @brief Conta i prestiti attualmente in ritardo.
     * @details Utilizzato per il Pannello di Controllo (UC1 - Visualizzazione
     * Pannello di Controllo).
     *
     * @return Numero di prestiti scaduti.
     */
    public int getPrestitiInRitardo() {
        return archivio.cercaPrestiti(FiltroPrestito.filtraInRitardo()).size(); //restituisco il numero di prestiti in ritardo cercando nell'archivio
    }

    /**
     * @brief Conta i prestiti conclusi.
     * @details Utilizzato per il Pannello di Controllo (UC1 - Visualizzazione
     * Pannello di Controllo).
     *
     * @return Numero di prestiti nello storico.
     */
    public int getPrestitiConclusi() {
        return archivio.cercaPrestiti(FiltroPrestito.filtraConclusi()).size(); //restituisco il numero di prestiti conclusi cercando nell'archivio
    }

    /**
     * @brief Conta i prestiti in corso.
     * @details Utilizzato per il Pannello di Controllo (UC1 - Visualizzazione
     * Pannello di Controllo).
     *
     * @return Numero di prestiti regolarmente in corso.
     */
    public int getPrestitiInCorso() {
        return archivio.cercaPrestiti(FiltroPrestito.filtraInCorso()).size(); //restituisco il numero di prestiti in corso cercando nell'archivio
    }
}
