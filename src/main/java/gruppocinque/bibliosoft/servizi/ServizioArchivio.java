/**
 * @file ServizioArchivio.java
 * @author gruppocinque
 * @version 1.0
 */
package gruppocinque.bibliosoft.servizi;

import gruppocinque.bibliosoft.modelli.Utente;
import gruppocinque.bibliosoft.modelli.Prestito;
import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.archivi.Archivio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @brief Gestisce la persistenza dei dati su file binario.
 * @details Questa classe si occupa di serializzare e deserializzare l'intero
 * stato dell'applicazione (Libri, Utenti, Prestiti) su un file locale.
 *
 * @invariant {@code fileArchivio != null}
 * @invariant {@code archivio != null}
 */

public class ServizioArchivio {
    private final String fileArchivio;  //attributo contenente il percorso dove si desidera salvare o caricare il file
    private final Archivio archivio;    //attributo archivio

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
        this.fileArchivio = fileArchivio;
        this.archivio = archivio;
    }

    /**
     * @brief Carica i dati dal file all'avvio dell'applicazione.
     * @details
     * Tenta di leggere il file specificato. Se esiste, deserializza le liste di Libri,
     * Utenti e Prestiti e popola l'archivio in memoria.
     *
     * @pre Il file, se esiste, deve contenere una Map serializzata compatibile.
     * @post L'archivio in memoria contiene i dati letti dal file.
     */
    public void carica(){
        File file = new File(fileArchivio); //creo il file usando il percorso specificato
        if (!file.exists())   //se il file non esiste...
            return; // nessun dato da caricare, è normale al primo avvio
        
        
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){//creo l'ois per poter estrarre gli oggetti dal file input stream overro lo stream dal file (con questo tipo di try lo stream viene chiuso automaticamente)
            //legge dal file un oggetto serializzato e lo interpreta come Map<String, Object> (assumendo che nel file sia stato salvato proprio una Map con chiavi "libri", "utenti", "prestiti")
            Map<String, Object> mappa = (Map<String, Object>) ois.readObject();
            
            List<Libro> libri = (List<Libro>) mappa.get("libri");    //estrae dalla mappa il valore associato alla chiave "libri" e lo converte in List<Libro>
            List<Utente> utenti = (List<Utente>) mappa.get("utenti");    //estrae dalla mappa il valore associato alla chiave "utenti" e lo converte in List<Utente>
            List<Prestito> prestiti = (List<Prestito>) mappa.get("prestiti");    //estrae dalla mappa il valore associato alla chiave "prestiti" e lo converte in List<Prestito>
            
            if (libri != null)  //se la lista di libri non è null...
                for(Libro libro : libri)    //ogni libro della lista...
                    archivio.aggiungiLibro(libro);  //viene aggiunto all'archivio
            
            if (utenti != null)  //se la lista di utenti non è null...
                for(Utente utente : utenti)    //ogni utente della lista...
                    archivio.aggiungiUtente(utente);  //viene aggiunto all'archivio
            
            if (prestiti != null)  //se la lista di prestiti non è null...
                for(Prestito prestito : prestiti)    //ogni prestito della lista..
                    archivio.aggiungiPrestito(prestito);  //viene aggiunto all'archivio
        }catch(ClassNotFoundException | IOException | ClassCastException e){
            //potrebbe essere lanciata una IOException se il file è inesistente, non leggibile, o ci sono problemi di permessi ecc.
            //potrebbe essere lanciata una ClassNotFoundException se una delle classi serializzate non è disponibile
            //potrebbe essere lanciata una ClassCastException se il tipo nel cast non è quello atteso
            e.printStackTrace();
        }
    }

    /**
     * @brief Salva lo stato corrente dell'archivio su file.
     * @details
     * Implementa il Caso d'Uso 16 (Salvataggio dati).
     * Serializza le liste correnti di Libri, Utenti e Prestiti.
     *
     * @pre L'archivio è in uno stato consistente.
     * @post Se non si verifica alcun errore di I/O, il file su disco
     *       riflette esattamente il contenuto della memoria
     */
    public void salva(){
        Map<String, Object> mappa = new HashMap<>(); //creo una mappa che associa a una stringa(es.libri) un oggetto (es.lista di libri)
        mappa.put("libri", archivio.listaLibri());   // "libri" -> lista di libri
        mappa.put("utenti", archivio.listaUtenti());   // "utenti" -> lista di utenti
        mappa.put("prestiti", archivio.listaPrestiti());   // "prestiti" -> lista di prestiti
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileArchivio))){   //creo un oos per scrivere su file l'oggetto, ovvero la mappa (con questo tipo di try lo stream viene chiuso automaticamente)
            oos.writeObject(mappa); //scrivo la mappa sul file
        }catch(IOException e){
            //potrebbe lanciare una IOException se il file è bloccato o ci sono problemi di permessi
            e.printStackTrace();
        }
    }
}
