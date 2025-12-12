/**
 * @file Prestito.java
 * @author gruppo5
 * @version 1.0
 */

package gruppo5.bibliosoft.modelli;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @brief Classe che rappresenta l'associazione temporale tra un Utente e un Libro.
 * @details
 * Questa classe gestisce il ciclo di vita del prestito, monitorando le scadenze
 * e lo stato corrente (In Corso, In Ritardo, Concluso).
 * Rappresenta l'entità fondamentale per il tracciamento delle operazioni di business.
 *
 * Implementa l'interfaccia Comparable per soddisfare il requisito di visualizzazione
 * ordinata delle scadenze.
 *
 * @invariant {@code id != null && !id.isEmpty()}
 * @invariant {@code utente != null}
 * @invariant {@code libro != null}
 * @invariant {@code dataInizio != null && dataPrevista != null}
 * @invariant {@code !dataPrevista.isBefore(dataInizio)} (La scadenza non può essere antecedente all'inizio)
 */

public class Prestito implements Serializable, Comparable<Prestito>{

    private final String id;
    
    private final Utente utente;
    
    private final Libro libro;
    
    private final LocalDate dataInizio;
    
    private final LocalDate dataPrevista;
    
    private LocalDate dataRestituzioneEffettiva;
    
    private StatoPrestito stato;

    /**
     * @brief Costruttore della classe Prestito.
     * @details
     * Crea una nuova istanza di prestito attiva.
     * Il sistema genera automaticamente un identificativo univoco (UUID) e
     * imposta lo stato iniziale a "IN_CORSO", decrementando le copie del libro
     * nel contesto del servizio chiamante.
     *
     *
     * @param[in] utente L'utente che richiede il prestito (deve essere abilitato).
     * @param[in] libro Il libro oggetto del prestito(deve essere disponibile).
     * @param[in] dataInizio La data di inizio del prestito.
     * @param[in] dataPrevista La data entro cui il libro deve essere restituito.
     *
     *
     * @pre {@code utente != null && libro != null} (Utente e libro non devono essere null).
     * @pre {@code !dataPrevista.isBefore(dataInizio)} (Verifica a carico del chiamante).
     *
     * 
     * @post {@code stato = StatoPrestito.IN_CORSO}
     * @post {@code id != null}
     * 
     */
    public Prestito(Utente utente, Libro libro, LocalDate dataInizio, LocalDate dataPrevista) {
        this.id = UUID.randomUUID().toString();
        this.utente = utente;
        this.libro = libro;
        this.dataInizio = dataInizio;
        this.dataPrevista = dataPrevista;
        this.stato = StatoPrestito.IN_CORSO;
    }

    public String getId() {
       return id;
    }

    public Utente getUtente() {
        return utente;
    }


    public Libro getLibro() {
        return libro;
    }


    public LocalDate getDataInizio() {
        return dataInizio;
    }


    public LocalDate getDataPrevista() {
       return dataPrevista;
    }


    public LocalDate getDataRestituzioneEffettiva() {
        return dataRestituzioneEffettiva;
    }

    public void setDataRestituzioneEffettiva(LocalDate dataRestituzioneEffettiva) {
        this.dataRestituzioneEffettiva = dataRestituzioneEffettiva;
    }

    public StatoPrestito getStato() {
          return stato;
    }

    public void setStato(StatoPrestito stato) {
        this.stato = stato;
    }

    /**
     * @brief Aggiorna lo stato del prestito (Calcolo dei ritardi).
     * @details
     * Implementa la logica automatica richiesta dal requisito "Calcolo dei ritardi".
     * Se la data corrente è successiva alla data prevista e il prestito è ancora in corso,
     * il sistema identifica il prestito come "In Ritardo".
     *
     * @param[in] oggi La data di riferimento (solitamente la data di sistema all'avvio o visualizzazione).
     * 
     * @pre {@code oggi != null}
     * 
     * @post Se ({@code oggi > dataPrevista AND stato == IN_CORSO}) allora {@code stato = IN_RITARDO}
     * @throws IllegalArgumentException Se la data passata è null.
     */
    public void aggiornaStato(LocalDate oggi) {
        if (stato == StatoPrestito.IN_CORSO && oggi.isAfter(dataPrevista)) {
            stato = StatoPrestito.IN_RITARDO;
        }
    }

    /**
     * @brief Verifica se il prestito è attualmente in ritardo.
     * @details
     * Metodo di utilità che esegue l'aggiornamento dello stato prima di restituire il booleano.
     * Utile per l'interfaccia utente che deve "Evidenziare i prestiti in ritardo".
     *
     * @param[in] oggi La data corrente.
     * 
     * @return true se lo stato risulta IN_RITARDO.
     * 
     * @pre {@code oggi != null} (Delegato a aggiornaStato()).
     */
    public boolean eInRitardo(LocalDate oggi) {
         aggiornaStato(oggi);
        return stato == StatoPrestito.IN_RITARDO;
    }
    
    /**
     * @brief Verifica l'uguaglianza logica tra due prestiti.
     * @details
     * Basata sull'identificativo univoco (ID) generato (UUID).
     *
     * @param[in] oggetto L'oggetto da confrontare.
     * 
     * @return true se gli ID coincidono.
     */
    public boolean equals(Object oggetto) {
      if (this == oggetto) {
            return true;
        }

        if (oggetto == null || getClass() != oggetto.getClass()) {
            return false;
        }

        
        return id.equals(((Prestito) oggetto).getId());
    }

   /**
     * @brief Confronta due prestiti per ordinamento.
     * @details
     * Implementa il requisito 3.1.3.2: "Ordinare automaticamente i prestiti attivi 
     * per data di restituzione (crescente)".
     * In caso di date uguali, utilizza l'ID come discriminante per garantire stabilità.
     *
     * @param[in] prestito Il prestito con cui confrontare l'istanza corrente.
     * 
     * @return Valore negativo se questo prestito scade prima dell'altro.
     * @pre {@code prestito != null}
     * @throws NullPointerException Se l'oggetto prestito passato è null.
     */
    
    @Override
    public int compareTo(Prestito prestito) {
        int cmp = dataPrevista.compareTo(prestito.getDataPrevista());
        return (cmp != 0) ? cmp : id.compareTo(prestito.getId());
    }
}
