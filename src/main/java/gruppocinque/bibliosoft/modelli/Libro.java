/**
 * @file Libro.java
 * @author gruppocinque
 * @version 1.0
 */
package gruppocinque.bibliosoft.modelli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Classe che rappresenta l'entità Libro nel dominio dell'applicazione.
 * @details Questa classe gestisce lo stato di un libro, garantendo la coerenza
 * tra le copie fisiche totali e quelle attualmente disponibili per il prestito.
 * Implementa l'interfaccia Comparable per l'ordinamento visuale e Serializable
 * per il salvataggio su file.
 *
 * @invariant {@code isbn != null && !isbn.isEmpty()}
 * @invariant {@code copieTotali >= 1} (Deve esistere almeno una copia fisica
 * all'atto della creazione )
 * @invariant {@code 0 <= copieDisponibili <= copieTotali}
 */
public class Libro implements Serializable, Comparable<Libro> {

    private final String isbn;

    private String titolo;

    private List<String> autori = new ArrayList<>();

    private int annoPubblicazione;

    private int copieTotali;

    private int copieDisponibili;

    /**
     * @brief Costruttore della classe Libro.
     *
     * Inizializza un nuovo libro nel sistema. Effettua un controllo preventivo
     * sui dati per mantenere l'integrità del dominio.
     *
     * @param[in] isbn Codice univoco (ISBN).
     * @param[in] titolo Titolo dell'opera.
     * @param[in] autori Lista degli autori.
     * @param[in] annoPubblicazione Anno di pubblicazione.
     * @param[in] copieTotali Numero totale di copie fisiche acquisite.
     *
     * @pre {@code isbn != null && !isbn.isEmpty() && titolo != null && !titolo.isEmpty()} (ISBN e Titolo obbligatori e non vuoti).
     * @pre {@code copieTotali >= 1} (Consistenza fisica minima).
     * 
     * @post copieDisponibili = copieTotali (Tutte le copie sono inizialmente disponibili).
     */
    public Libro(String isbn, String titolo, List<String> autori, int annoPubblicazione, int copieTotali) {
        this.isbn = isbn;
        this.titolo = titolo;
        this.autori = new ArrayList<>(autori);
        this.annoPubblicazione = annoPubblicazione;
        this.copieTotali = copieTotali;
        this.copieDisponibili = copieTotali;
    }

    public String getIsbn() {
         return isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * @brief Restituisce la lista degli autori.
     * @details Fornisce una copia della lista per preservare l'incapsulamento:
     * le modifiche apportate alla lista restituita non avranno effetto sul libro.
     * 
     * @return Una nuova lista contenente le stringhe degli autori.
     */
    public List<String> getAutori() {
        return new ArrayList<>(autori);
    }

    /**
     * @brief Imposta la lista degli autori.
     * @details Memorizza una copia indipendente della lista passata.
     * Questo garantisce che eventuali modifiche future alla lista originale
     * non cambino gli autori memorizzati in questo libro.
     * 
     * @param[in] autori La lista degli autori da associare al libro.
     */
    public void setAutori(List<String> autori) {
        this.autori = new ArrayList<>(autori);
    }

    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public int getCopieTotali() {
        return copieTotali;
    }

    public void setCopieTotali(int copieTotali) {
         this.copieTotali = copieTotali;
    }

    public int getCopieDisponibili() {
        return copieDisponibili;
    }

    public void setCopieDisponibili(int copieDisponibili) {
        this.copieDisponibili = copieDisponibili;
    }

    public int getCopieInPrestito() { 
        return this.copieTotali - this.copieDisponibili;
    }

    /**
     * @brief Verifica la disponibilità del libro per un nuovo prestito.
     * @details Controlla se vi sono copie fisiche non attualmente in prestito.
     * Prerequisito per la "Registrazione prestiti".
     *
     * @return true se {@code copieDisponibili > 0} , false altrimenti.
     */
    public boolean isDisponibile() {
         return copieDisponibili > 0;
    }

    /**
     * @brief Verifica se esistono copie del libro attualmente in prestito.
     * @details Funge da "guardia" per l'operazione di cancellazione. Il sistema
     * impedisce la cancellazione se il libro risulta in prestito.
     *
     * @return true se {@code copieDisponibili != copieTotali}
     */
    public boolean haPrestitiAttivi() {
        return copieDisponibili != copieTotali;
    }

    /**
     * @brief Verifica l'uguaglianza logica tra due libri.
     * @details Basata sull'ISBN univoco.
     *
     * @param[in] oggetto L'oggetto da confrontare.
     *
     * @return true se l'oggetto è un Libro con lo stesso ISBN.
     */
    public boolean equals(Object oggetto) {
       if(this == oggetto)
            return true;
        
        if(oggetto == null || getClass() != oggetto.getClass())
            return false;
        
        return isbn.equals(((Libro) oggetto).getIsbn());
    }

    /**
     * @brief Definisce l'ordinamento dei libri per le liste visualizzate.
     * @details Ordinamento alfabetico per Titolo, poi per ISBN.
     *
     * @param[in] libro Il libro da comparare.
     *
     * @return Valore negativo, zero o positivo.
     *
     * @pre {@code libro != null}
     */
    
    @Override
    public int compareTo(Libro libro) {
        if(isbn.compareToIgnoreCase(libro.getIsbn()) == 0)
            return 0;
        
        int cmp = titolo.compareToIgnoreCase(libro.getTitolo());
        return (cmp != 0)? cmp : isbn.compareToIgnoreCase(libro.getIsbn());
    }

    /**
     * @brief Verifica se il libro è stato scritto da un determinato autore.
     * @details La ricerca è case-insensitive.
     *
     * @param[in] filtroAutore Stringa contenente il nome da cercare.
     *
     * @return true se corrisponde.
     *
     * @pre {@code filtroAutore != null}
     */
    public boolean contieneAutore(String filtroAutore) {
       for(String autore : autori)
            if(autore.toLowerCase().contains(filtroAutore.toLowerCase()))return true;
        
        return false;
    }
    
     @Override
    public String toString() {
        return titolo + " (" + isbn + ")";
    }
}
