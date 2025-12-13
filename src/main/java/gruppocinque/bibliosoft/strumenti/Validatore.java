/**
 * @file Validatore.java
 * @author gruppocinque
 * @version 1.0
 */
package gruppocinque.bibliosoft.strumenti;

import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.modelli.Utente;
import java.time.Year;

/**
 * @brief Classe di utilità per la validazione dei dati di input.
 * @details Questa classe fornisce metodi statici per verificare l'integrità e
 * la correttezza dei dati relativi alle entità Libro e Utente prima che vengano
 * elaborati o persistiti. Implementa controlli formali e logici.
 */
public class Validatore {

    /**
     * @brief Valida i dati di un oggetto Libro.
     * @details Implementa i controlli di formato e logici per l'inserimento (UC3).
     * @param[in] libro L'oggetto Libro da validare.
     *
     * @pre {@code libro != null}
     * @pre "isbn": Deve essere numerico e lungo 10 o 13 caratteri.
     * @pre {@code (titolo != null && titolo non vuoto)}
     * @pre {@code copieTotali >= 1}.
     * @pre {@code annoPublicazione < annoCorrente}.
     * @pre "autori": Verifica che i nomi contengano solo lettere, spazi o punti.
     *
     * @throws IllegalArgumentException Se uno qualsiasi dei dati non rispetta le precondizioni.
     * @throws NullPointerException Se l'oggetto libro è null.
     */
    public static void validaLibro(Libro libro) {
        if (libro == null) { //Controllo libro null
            throw new NullPointerException("L'oggetto Libro non può essere null.");
        }
        // Controllo ISBN
        if (libro.getIsbn() == null || libro.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN obbligatorio");
        }

        if (!libro.getIsbn().matches("[0-9]+")) {
            throw new IllegalArgumentException("ISBN non valido");
        }

        if (libro.getIsbn().length() != 10 && libro.getIsbn().length() != 13) {
            throw new IllegalArgumentException("Lunghezza ISBN non valida (ISBN10 o ISBN13)");
        }

        // Controlli titolo
        if (libro.getTitolo() == null || libro.getTitolo().isBlank()) {
            throw new IllegalArgumentException("Titolo obbligatorio");
        }

        // Controlli copietotali
        if (libro.getCopieTotali() <= 0) {
            throw new IllegalArgumentException("Numero copie deve essere >= 1");
        }

        // Controlli anno
        if (libro.getAnnoPubblicazione() > Year.now().getValue()) {
            throw new IllegalArgumentException("L'anno deve essere precedente o pari al " + Year.now().getValue());
        }

        // Controlli autori
        for (String autore : libro.getAutori()) {
            if (autore == null || autore.isBlank()) {
                throw new IllegalArgumentException("Autore vuoto");
            }

            if (!autore.matches("^[A-Za-zÀ-ÖØ-öø-ÿ .]+(,[A-Za-zÀ-ÖØ-öø-ÿ .]+)*$")) {
                throw new IllegalArgumentException("Autore non valido");
            }

        }
    }

    /**
     * @brief Valida i dati di un oggetto Utente.
     * @details
     * @param[in] utente L'oggetto Utente da validare.
     *
     * @pre {@code utente != null}
     * @pre 'matricola' deve essere composta solo da numeri.
     * @pre 'nome' e 'cognome' devono contenere solo caratteri alfabetici.
     * @pre 'email' deve terminare con "@studenti.unisa.it".
     *
     * @throws IllegalArgumentException Se i dati non rispettano il formato richiesto delle precondizioni.
     * @throws NullPointerException Se l'oggetto libro è null.
     */
    public static void validaUtente(Utente utente) {
        if (utente == null) { //Controllo utente null
            throw new NullPointerException("L'oggetto Utente non può essere null.");
        }
        // Controllo matricola
        if (utente.getMatricola() == null || utente.getMatricola().isBlank()) {
            throw new IllegalArgumentException("Matricola obbligatoria");
        }

        if (!utente.getMatricola().matches("[0-9]+")) {
            throw new IllegalArgumentException("Matricola non valida");
        }

        // Controllo nome
        if (utente.getNome() == null || utente.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome obbligatorio");
        }

        if (!utente.getNome().matches("^[A-Za-zÀ-ÖØ-öø-ÿ .'-]+(,[A-Za-zÀ-ÖØ-öø-ÿ .'-]+)*$")) {
            throw new IllegalArgumentException("Nome non valido");
        }

        // Controllo cognome
        if (utente.getCognome() == null || utente.getCognome().isBlank()) {
            throw new IllegalArgumentException("Cognome obbligatorio");
        }

        if (!utente.getCognome().matches("^[A-Za-zÀ-ÖØ-öø-ÿ .'-]+(,[A-Za-zÀ-ÖØ-öø-ÿ .'-]+)*$")) {
            throw new IllegalArgumentException("Cognome non valido");
        }

        // Controllo email
        if (utente.getEmail() == null || utente.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email obbligatoria");
        }
        if (!utente.getEmail().toLowerCase().matches("^[A-Za-z0-9._%+-]+@studenti\\.unisa\\.it$")) {
            throw new IllegalArgumentException("Email istituzionale non valida");
        }
    }
}
