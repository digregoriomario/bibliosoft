/**
 * @file ServizioArchivio.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.servizi;

/**
 * @brief Gestisce la persistenza dei dati su file binario.
 * @details Questa classe si occupa di serializzare e deserializzare l'intero
 * stato dell'applicazione (Libri, Utenti, Prestiti) su un file locale.
 *
 * @invariant {@code fileArchivio != null}
 * @invariant {@code archivio != null}
 */

public class ServizioArchivio {
    private final String fileArchivio;
    private final Archivio archivio;

    /**
     * @brief Costruisce il servizio di archiviazione.
     * @details
     * @param[in] fileArchivio Il percorso del file.
     * @param[in] archivio L'istanza dell'archivio centrale dell'applicazione.
     *
     * @pre {@code fileArchivio != null && !fileArchivio.isEmpty()}
     * @pre {@code archivio != null}
     */
    public ServizioArchivio(String fileArchivio, Archivio archivio) {
    }

    /**
     * @brief Carica i dati dal file all'avvio dell'applicazione.
     * @details
     * Tenta di leggere il file specificato. Se esiste, deserializza le liste di Libri,
     * Utenti e Prestiti e popola l'archivio in memoria.
     *
     * @pre Il file, se esiste, deve contenere una Map serializzata compatibile.
     * @post L'archivio in memoria contiene i dati letti dal file.
     *
     * @throws IOException Se si verificano errori di lettura.
     * @throws ClassNotFoundException Se la versione delle classi serializzate non corrisponde.
     */
    public void carica(){
        }
    }

    /**
     * @brief Salva lo stato corrente dell'archivio su file.
     * @details
     * Implementa il Caso d'Uso 16 (Salvataggio dati).
     * Serializza le liste correnti di Libri, Utenti e Prestiti.
     *
     * @pre L'archivio è in uno stato consistente.
     * @post Il file su disco riflette esattamente il contenuto della memoria.
     *
     * @throws IOException Se il file è bloccato o non scrivibile (vedi Flusso alternativo 4a UC16 - Salvataggio Dati).
     */
    public void salva(){
        }
    }
}
