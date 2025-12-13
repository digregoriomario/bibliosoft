/**
 * @file Utente.java
 * @author gruppocinque
 * @version 1.0
 */

package gruppocinque.bibliosoft.modelli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Classe che rappresenta l'entità Utente nel dominio dell'applicazione.
 * @details
 * Questa classe gestisce i dati anagrafici di uno studente universitario e lo stato
 * dei suoi prestiti correnti.
 * Rappresenta uno dei componenti fondamentali per l'associazione nei processi di prestito.
 *
 * La classe garantisce il rispetto dei vincoli di dominio, in particolare riguardo
 * al formato della mail istituzionale e al numero massimo di prestiti consentiti.
 *
 * @invariant {@code matricola != null && !matricola.isEmpty()}
 * @invariant {@code email != null && email.endsWith("@studenti.unisa.it")}
 * @invariant {@code prestitiAttivi != null}
 * @invariant {@code prestitiAttivi.size() <= 3} (Vincolo di business sui prestiti contemporanei).
 */
public class Utente implements Serializable, Comparable<Utente> {

    private final String matricola;

    private String nome;

    private String cognome;

    private String email;

    private List<Prestito> prestitiAttivi = new ArrayList<>();

    /**
     * @brief Costruttore della classe Utente.
     * @details
     * Inizializza un nuovo utente nel sistema con i dati anagrafici forniti.
     * Include controlli rigorosi sui formati dei dati (in particolare l'email istituzionale).
     * All'atto della creazione, la lista dei prestiti attivi viene inizializzata vuota.
     *
     * @param[in] matricola Codice identificativo univoco (Matricola).
     * @param[in] nome Nome dell'utente.
     * @param[in] cognome Cognome dell'utente.
     * @param[in] email Indirizzo e-mail istituzionale (deve terminare con "@studenti.unisa.it").
     *
     * @pre {@code matricola != null && Validator.isValidMatricola(matricola)} (Matricola valida e conforme al formato).
     * @pre {@code email != null && Validator.isValidEmail(email)} (Email valida e conforme al formato).
     *
     * @post {@code prestitiAttivi.isEmpty() = true} (Nuovo utente senza prestiti).
     */
    public Utente(String matricola, String nome, String cognome, String email) {
          this.matricola = matricola;
          this.nome = nome;
          this.cognome = cognome;
          this.email = email;
    } 

    public String getMatricola() {
         return matricola;
    }

    public String getNome() {
         return nome;
    }

    public void setNome(String nome) {
         this.nome = nome;
    }

    public String getCognome() {
         return cognome;
    }

    public void setCognome(String cognome) {
         this.cognome = cognome;
    }

    public String getEmail() {
         return email;
    }

    public void setEmail(String email) {
         this.email = email;
    }

    /**
     * @brief Restituisce una copia della lista dei prestiti correnti dell'utente.
     * @details
     * Recupera l'elenco dei libri che l'utente ha attualmente in carico
     * (in stato "In Corso" o "In Ritardo").
     * Restituisce una nuova lista per preservare l'incapsulamento.
     *
     * @return Lista di oggetti Prestito.
     */
    public List<Prestito> getPrestitiAttivi() {
        return new ArrayList<>(prestitiAttivi);
    }

    /**
     * @brief Associa un nuovo prestito all'utente.
     * @details
     * Aggiunge un prestito alla lista dei prestiti attivi.
     * Questa operazione è parte del processo di "Registrazione prestiti".
     *
     * @param[in] prestito L'oggetto Prestito da aggiungere.
     *
     * @pre {@code prestito != null}
     * @pre {@code prestitiAttivi.size() < 3} (L'utente non deve avere già 3 prestiti attivi).
     *
     */
    public void aggiungiPrestito(Prestito prestito) {
        prestitiAttivi.add(prestito);
    }

    /**
     * @brief Rimuove un prestito dalla lista dell'utente.
     * @details
     * Operazione richiamata durante la fase di "Restituzione libri".
     * Quando un prestito viene concluso, non deve più figurare tra i prestiti attivi dell'utente
     * ai fini del conteggio del limite massimo.
     *
     * @param[in] prestito Il prestito da rimuovere (che passa allo stato Concluso).
     * @pre {@code prestito != null}
     */
    public void rimuoviPrestito(Prestito prestito) {
        prestitiAttivi.remove(prestito);
    }

    /**
     * @brief Verifica se l'utente possiede prestiti non ancora conclusi.
     * @details
     * Metodo di utilità fondamentale per validare la cancellazione di un utente.
     * Secondo i requisiti, non è possibile eliminare un utente se ha ancora prestiti pendenti ("In Corso" o "In Ritardo").
     *
     * @return true se {@code prestitiAttivi.size() > 0}, false altrimenti.
     */
    public boolean haPrestitiAttivi() {
        return !prestitiAttivi.isEmpty();
    }

    /**
     * @brief Verifica l'uguaglianza logica tra due utenti.
     * @details
     * L'uguaglianza è determinata esclusivamente dalla matricola univoca,
     * indipendentemente da nome o cognome (che potrebbero presentare omonimie).
     *
     * @param[in] oggetto L'oggetto da confrontare.
     * 
     * @return true se l'oggetto è un Utente con la stessa matricola.
     */
    public boolean equals(Object oggetto) {
         if(this == oggetto)
            return true;
        
        if(oggetto == null || getClass() != oggetto.getClass())
            return false;
        
        return matricola.equals(((Utente) oggetto).getMatricola());
    }

    /**
     * @brief Definisce l'ordinamento naturale degli utenti.
     * @details
     * Implementa la logica di comparazione per visualizzare le liste ordinate.
     * I requisiti specificano esplicitamente un ordinamento alfabetico basato
     * su Cognome e poi Nome. La matricola è usata come criterio terziario per stabilità.
     *
     * @param[in] utente L'utente da comparare.
     * 
     * @return Valore negativo, zero o positivo in base all'ordine lessicografico.
     * 
     * @pre {@code utente != null}
     */
    
     @Override
    public int compareTo(Utente utente) {
         if(matricola.compareToIgnoreCase(utente.getMatricola()) == 0)
            return 0;
        
        if(email.compareToIgnoreCase(utente.getEmail()) == 0)
            return 0;
        
        int cmp = cognome.compareToIgnoreCase(utente.getCognome());
        cmp = (cmp != 0)? cmp : nome.compareToIgnoreCase(utente.getNome());
        return (cmp != 0) ? cmp : matricola.compareToIgnoreCase(utente.getMatricola());
    }
    
     @Override
    public String toString() {
        return cognome + " " + nome + " (" + matricola + ")";
    }
}
