/**
 * @file ServizioLibri.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.servizi;

import gruppo5.bibliosoft.modelli.Libro;
import gruppo5.bibliosoft.archivi.Archivio;
import gruppo5.bibliosoft.archivi.filtri.FiltroLibro;
import gruppo5.bibliosoft.archivi.filtri.InterfacciaFiltro;
import gruppo5.bibliosoft.strumenti.Validatore;
import java.util.List;

/**
 * @brief Gestisce la logica di business relativa ai Libri (RF 3.1.1 - Gestione
 * Libri).
 * @details Questa classe funge da livello di servizio tra l'interfaccia utente
 * (Controller) e lo strato di persistenza (Archivio). Si occupa di validare gli
 * input, applicare le regole di dominio e coordinare le operazioni principali
 * sull'archivio.
 *
 * @invariant {@code archivio != null}
 */
public class ServizioLibri {

    private final Archivio archivio;    //attributo archivio

    /**
     * @brief Costruttore del servizio.
     * @details
     * @param[in] archivio Riferimento all'archivio centrale condiviso.
     *
     * @pre {@code archivio != null}
     * @post attributi correttamente inizializzati.
     */
    public ServizioLibri(Archivio archivio) {
        this.archivio = archivio;
    }

    /**
     * @brief Aggiunge un nuovo libro al catalogo.
     * @details Implementa il Caso d'Uso 2 (Inserimento nuovo libro) e RF
     * 3.1.1.1 (Inserimento dati). Prima dell'inserimento, valida i dati tramite
     * la classe Validatore.
     *
     * @param[in] libro L'oggetto Libro da inserire.
     *
     * @pre {@code libro != null}
     * @pre I dati del libro devono essere validi.
     * @pre Non deve esistere un altro libro con lo stesso ISBN.
     * @post Il libro è aggiunto all'archivio.
     *
     * @throws IllegalArgumentException Se la validazione fallisce.
     * @throws IllegalStateException Se il libro è già presente.
     *
     * @see Validatore
     * 
     */
    public void aggiungiLibro(Libro libro) {
        Validatore.validaLibro(libro);  //se il validatore valida libro da aggiungere(tutti i parametri sono validi)...
        //aggiungi eventuali controlli per vedere se il libro può essere aggiunto  (nulla penso)
        archivio.aggiungiLibro(libro);  //...allora aggiungo il libro all'archivio
    }

    /**
     * @brief Modifica i dati di un libro esistente.
     * @details Implementa il Caso d'Uso 3 (Modifica dati libro) e RF 3.1.1.2
     * (Modifica dati). Valida i nuovi dati prima di salvare le modifiche.
     *
     * @param[in] libro Il libro con i dati aggiornati.
     *
     * @pre {@code libro != null}
     * @pre I nuovi dati devono essere validi.
     * @pre Il libro deve esistere in archivio.
     * @post I dati nel sistema sono aggiornati.
     *
     * @throws IllegalArgumentException Se i dati non sono validi.
     */
    public void modificaLibro(Libro libro) {
        if(libro == null)
            throw new IllegalArgumentException("Libro nullo");
        
        Validatore.validaLibro(libro);  //se il validatore valida libro da modificare(tutti i parametri sono validi)...
        List<Libro> risultati = archivio.cercaLibri(FiltroLibro.ricercaIsbn(libro.getIsbn()));
        
        if(risultati.isEmpty())
            throw new IllegalArgumentException("Libro non trovato");
        
        Libro libroDaModificare = risultati.get(0);
        
        if(libro.getCopieTotali() < libroDaModificare.getCopieInPrestito())
            throw new IllegalArgumentException("Copie totali troppo basso. (" + libroDaModificare.getCopieInPrestito() + " copie attualmente in prestito)");
        
        libro.setCopieDisponibili(libroDaModificare.getCopieDisponibili() + libro.getCopieTotali() - libroDaModificare.getCopieTotali());
        
        archivio.modificaLibro(libro);  //...allora modifico il libro nell'archivio
    }

    /**
     * @brief Elimina un libro dal catalogo.
     * @details Implementa il Caso d'Uso 4 (Cancellazione libro) e RF 3.1.1.3
     * (Cancellazione dati). Verifica la regola di business: non è possibile
     * eliminare un libro se ci sono copie attualmente in prestito o se le copie
     * non sono tutte rientrate.
     *
     * @param[in] libro Il libro da eliminare.
     *
     * @pre {@code libro != null}
     * @pre {@code libro.haPrestitiAttivi() == false}
     * @post Il libro è rimosso dall'archivio.
     *
     * @throws IllegalStateException Se si tenta di eliminare un libro con
     * prestiti attivi.
     */
    public void eliminaLibro(Libro libro) {
        if (libro.haPrestitiAttivi())   //se il libro  ha prestiti attivi...
            throw new IllegalStateException("Impossibile eliminare: libro in prestito");  //lancio l'eccezione
        archivio.rimuoviLibro(libro);   //...allora rimuovo il libro dall'archivio
    }

    /**
     * @brief Restituisce la lista completa dei libri.
     * @details Implementa il Caso d'Uso 5 (Visualizzazione lista libri) e RF
     * 3.1.1.4 (Lista ordinata).
     *
     * @return Lista di tutti i libri presenti nel catalogo.
     */
    public List<Libro> listaLibri() {
        return archivio.listaLibri();   //restituisco l'intera lista di libri
    }

    /**
     * @brief Cerca libri che soddisfano un determinato filtro.
     * @details Implementa il Caso d'Uso 6 (Ricerca libro) e RF 3.1.1.5
     * (Ricerca).
     *
     * @param[in] filtro Criterio di filtraggio. Se null, restituisce tutto.
     *
     * @return Lista dei libri trovati.
     *
     * @see FiltroLibro
     */
    public List<Libro> cercaLibri(InterfacciaFiltro<Libro> filtro) {
        return archivio.cercaLibri(filtro); //retuisco la lista filtrata
    }

    /**
     * @brief Restituisce il numero totale di titoli unici (ISBN) nel catalogo.
     * @details Utilizzato per la Dashboard (UC1 - Visualizzazione
     * Dashboard).
     *
     * @return Conteggio titoli.
     */
    public int getLibriTotali() {
        return archivio.contaLibri();   //restituisco il numero di copie totali
    }

    /**
     * @brief Calcola il numero totale di copie fisiche possedute dalla
     * biblioteca.
     * @details Utilizzato per la Dashboard (UC1 - Visualizzazione
     * Dashboard).
     *
     * Itera su tutti i libri sommando il campo 'copieTotali'.
     *
     * @return Somma totale delle copie.
     */
    public int getCopieTotali() {
        int contatore = 0;  //contatore
        for (Libro libro : archivio.listaLibri())   //per ogni libro dell'archivio...
            contatore += libro.getCopieTotali();    //...conto quante copie totali ha
        

        return contatore;   //restituisco il conteggio
    }

    /**
     * @brief Calcola il numero totale di copie attualmente disponibili in
     * biblioteca.
     * @details Utilizzato per la Dashboard (UC1 - Visualizzazione
     * Dashboard).
     *
     * Itera su tutti i libri sommando il campo 'copieDisponibili'.
     *
     * @return Somma delle copie disponibili al prestito.
     */
    public int getCopieDisponibili() {
        int contatore = 0;  //contatore
        for (Libro libro : archivio.listaLibri())   //per ogni libro dell'archivio...
            contatore += libro.getCopieDisponibili();    //...conto quante copie disponibili ha
        

        return contatore;   //restituisco il conteggio
    }
}