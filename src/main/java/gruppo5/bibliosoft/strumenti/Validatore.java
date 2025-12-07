package gruppo5.bibliosoft.strumenti;

/**
 * @file Validatore.java
 * 
 * @brief Classe di utilità per la validazione dei dati di input.
 * 
 * * Questa classe fornisce metodi statici per verificare l'integrità e la correttezza
 * dei dati relativi alle entità Libro e Utente prima che vengano elaborati o persistiti.
 * Implementa controlli formali e logici.
 */
public class Validatore {

    /**
     * @brief Valida i dati di un oggetto Libro.
     * 
     * @param[in] libro L'oggetto Libro da validare.
     * 
     * @pre Il parametro "libro" non deve essere null.
     * @pre "isbn": Deve essere numerico e lungo 10 o 13 caratteri.
     * @pre "titolo": Non deve essere nullo o vuoto.
     * @pre "copieTotali": Deve essere un numero positivo (>= 1).
     * @pre "annoPublicazione": Non può essere superiore all'anno corrente.
     * @pre "autori": Verifica che i nomi contengano solo lettere, spazi o punti.
     * 
     * @throws IllegalArgumentException Se uno qualsiasi dei dati non rispetta le precondizioni.
     * @throws NullPointerException Se l'oggetto libro è null.
     */
    public static void validaLibro(Libro libro) {
    }

    /**
     * @brief Valida i dati di un oggetto Utente.
     * 
     * @param[in] utente L'oggetto Utente da validare.
     * 
     * @pre Il parametro 'utente' non deve essere null.
     * @pre 'matricola' deve essere composta solo da numeri.
     * @pre 'nome' e 'cognome' devono contenere solo caratteri alfabetici.
     * @pre 'email' deve terminare con "@studenti.unisa.it".
     * 
     * @throws IllegalArgumentException Se i dati non rispettano il formato richiesto delle precondizioni.
     * @throws NullPointerException Se l'oggetto libro è null.
     */
    public static void validaUtente(Utente utente) {
    }
}
