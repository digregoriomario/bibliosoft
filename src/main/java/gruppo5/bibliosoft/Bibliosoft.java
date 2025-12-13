/**
 * @file Bibliosoft.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft;

import gruppo5.bibliosoft.archivi.*;
import gruppo5.bibliosoft.servizi.*;
import gruppo5.bibliosoft.controller.*;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @brief Classe principale dell'applicazione "Bibliosoft".
 * @details Questa classe gestisce il ciclo di vita dell'applicazione JavaFX. Le
 * sue responsabilità principali sono: * Preparazione iniziale: Inizializzazione
 * degli archivi e dei servizi. * Configurazione delle dipendenze: Collegamento
 * dei servizi al Controller Principale. * Gestione Finestra: Configurazione
 * dello Stage primario e caricamento dell'interfaccia grafica. * Persistenza
 * all'avvio: Caricamento automatico dello stato precedente dell'archivio. *
 * Chiusura Controllata: Intercettazione della richiesta di uscita per prevenire
 * perdite di dati.
 */
public class Bibliosoft extends Application {

    private static Scene scena; //scena principale

    /**
     * @brief Metodo di avvio dell'applicazione JavaFX.
     * @details Configura l'ambiente di esecuzione. Esegue sequenzialmente: *
     * Istanziazione dell'Archivio e dei Servizi (Libri, Utenti, Prestiti). *
     * Caricamento del layout FXML principale. * Iniezione delle dipendenze nel
     * Controller. * Configurazione dei vincoli della finestra (Titolo,
     * Dimensioni minime). * Avvio della procedura di caricamento dati da file.
     *
     * @param[in] stage Lo stage primario fornito dalla piattaforma JavaFX.
     *
     *
     * @pre {@code stage != null}
     * @post L'applicazione è visibile a video e i dati sono caricati in
     * memoria.
     * @throws IOException Se il caricamento del file FXML fallisce.
     */
    @Override
    public void start(Stage stage) throws IOException {
        //instanzio gli archivi:
        Archivio archivio = new Archivio();

        //instanzio i servizi:
        ServizioLibri servizioLibri = new ServizioLibri(archivio);
        ServizioUtenti servizioUtenti = new ServizioUtenti(archivio);
        ServizioPrestiti servizioPrestiti = new ServizioPrestiti(archivio);
        ServizioArchivio servizioArchivio = new ServizioArchivio("archivio.dat", archivio);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vista_principale.fxml"));   //carico il file fxml contenente la menubar e le tab
        Scene scena = new Scene(loader.load()); //carico la scena

        //scena.getStylesheets().add(getClass().getResource("/css/stile_principale.css").toExternalForm());   //aggiungo il foglio di stile alla scena

        ControllerPrincipale controller = loader.getController();   //instanzio il controller principale
        controller.inizializzaServizi(servizioLibri, servizioUtenti, servizioPrestiti, servizioArchivio);   //inizializzo i diversi servizi nel controller principale

        stage.setOnCloseRequest(e -> {
            controller.chiudiApplicazione(null);
        });
        stage.setTitle("Bibliosoft - Gestionale Biblioteca");   //do un titolo allo stage
        stage.setScene(scena);  //setto la scena nello stage
        stage.setMinWidth(900); //imposto una larghezza minima per lo stage (più di questo non può essere rimpicciolito
        stage.setMinHeight(600); //imposto un'altezza minima per lo stage (più di questo non può essere rimpicciolito
        stage.show();   //mostro lo stage

        try {
            servizioArchivio.carica();  //carico il servizio che si preoccupa di caricare o salvare i dati in un file oggetto (archivio.dat)
            controller.aggiornaTutto(); //dico al controller di aggiornare tutto
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    

    /**
     * @brief Metodo main standard.
     * @details Garantisce la portabilità su Windows, macOS e Linux come
     * definito nei requisiti.
     *
     * @param args Argomenti da riga di comando.
     */
    public static void main(String[] args) {
        //il main lancia l'applicazione
        launch(args);   //lancio l'applicazione

    }
}
