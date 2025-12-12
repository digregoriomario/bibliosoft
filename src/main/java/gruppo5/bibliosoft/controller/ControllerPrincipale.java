/**
 * @file ControllerPrincipale.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.controller;

import gruppo5.bibliosoft.servizi.ServizioArchivio;
import gruppo5.bibliosoft.servizi.ServizioLibri;
import gruppo5.bibliosoft.servizi.ServizioPrestiti;
import gruppo5.bibliosoft.servizi.ServizioUtenti;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

/**
 * @brief Controller principale dell'applicazione.
 * @details Gestisce la navigazione tramite TabPane e orchestra le operazioni
 * globali come il salvataggio dei dati e la chiusura dell'applicazione. Collega
 * i servizi di backend ai controller delle singole viste (Libri, Utenti,
 * Prestiti).
 *
 * @invariant {@code tabPane != null}
 * @invariant {@code root != null}
 */
public class ControllerPrincipale {

    /**
     * @brief Flag globale per tracciare lo stato delle modifiche.
     * @details Viene impostato a true ogni volta che un'operazione di aggiunta,
     * modifica o rimozione viene eseguita. Fondamentale per il Caso d'Uso 17
     * (Chiusura applicazione).
     */
    public static boolean modificheEffettuate = false;  //attributo booleano per rilevare le modifiche effettuate

    //servizi utili a interagire con i dati:
    private ServizioLibri servizioLibri;
    private ServizioUtenti servizioUtenti;
    private ServizioPrestiti servizioPrestiti;
    private ServizioArchivio servizioArchivio;

    //controller secondari:
    private ControllerDashboard controllerDashboard;
    private ControllerLibri controllerLibri;
    private ControllerUtenti controllerUtenti;
    private ControllerPrestiti controllerPrestiti;
    
    //attributi fxml:
    @FXML
    private BorderPane root;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabDashboard;
    @FXML
    private Tab tabLibri;
    @FXML
    private Tab tabUtenti;
    @FXML
    private Tab tabPrestiti;

    /**
     * @brief Gestisce il cambio di tab nell'interfaccia.
     * @details Invocato quando l'utente seleziona una scheda diversa. Aggiorna
     * tutte le viste per garantire coerenza dei dati visualizzati.
     */
    public void cambioTab() {
        aggiornaTutto();    //ogni volta che viene cambiata tab bisogna aggiornare tutto (tabelle ecc.)
    }

    /**
     * @brief Inizializza i servizi e carica le viste secondarie.
     * @details Metodo di iniezione delle dipendenze. Carica i file FXML per le
     * tab (Dashboard, Libri, Utenti, Prestiti) e passa i servizi ai rispettivi
     * controller.
     *
     * @param[in] servizioLibri Gestore logica libri.
     * @param[in] servizioUtenti Gestore logica utenti.
     * @param[in] servizioPrestiti Gestore logica prestiti.
     * @param[in] servizioArchivio Gestore persistenza dati.
     *
     * @pre {@code servizi != null}
     * @post Le viste secondarie sono caricate e popolate.
     */
    public void inizializzaServizi(ServizioLibri servizioLibri, ServizioUtenti servizioUtenti, ServizioPrestiti servizioPrestiti, ServizioArchivio servizioArchivio) {
        //inizializzo i servizi passati dal main:
        this.servizioLibri = servizioLibri;
        this.servizioUtenti = servizioUtenti;
        this.servizioPrestiti = servizioPrestiti;
        this.servizioArchivio = servizioArchivio;

        //carico tutte le viste:
        caricaVistaDashboard();
        caricaVistaLibri();
        caricaVistaUtenti();
        caricaVistaPrestiti();
    }

    /**
     * @brief Carica la vista e il controller della Dashboard (UC1 - Visualizzazione Dashboard).
     */
    private void caricaVistaDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vista_dashboard.fxml"));
            Node content = loader.load();
            controllerDashboard = loader.getController();
            controllerDashboard.impostaServizi(servizioPrestiti, servizioUtenti, servizioLibri);
            tabDashboard.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Carica la vista e il controller per la Gestione Libri (RF 3.1.1 - Gestione Libri).
     */
    private void caricaVistaLibri() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vista_libri.fxml"));
            Node content = loader.load();
            controllerLibri = loader.getController();
            controllerLibri.impostaServizi(servizioLibri);
            tabLibri.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Carica la vista e il controller per la Gestione Utenti (RF 3.1.2 - Gestione Utenti).
     */
    private void caricaVistaUtenti() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vista_utenti.fxml"));
            Node content = loader.load();
            controllerUtenti = loader.getController();
            controllerUtenti.impostaServizi(servizioUtenti, servizioPrestiti);
            tabUtenti.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Carica la vista e il controller per la Gestione Prestiti (RF 3.1.3 - Gestione Prestiti).
     */
    private void caricaVistaPrestiti() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vista_prestiti.fxml"));
            Node content = loader.load();
            controllerPrestiti = loader.getController();
            controllerPrestiti.impostaServizi(servizioPrestiti, servizioUtenti, servizioLibri);
            tabPrestiti.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Aggiorna i dati visualizzati in tutti i sotto-controller.
     * @details Utile per sincronizzare le viste dopo modifiche ai dati
     * condivisi.
     */
    public void aggiornaTutto() {
        if (controllerDashboard != null) {
            controllerDashboard.aggiorna();
        }
        if (controllerLibri != null) {
            controllerLibri.aggiorna();
        }
        if (controllerUtenti != null) {
            controllerUtenti.aggiorna();
        }
        if (controllerPrestiti != null) {
            controllerPrestiti.aggiorna();
        }
    }

    /**
     * @brief Gestisce il salvataggio dei dati su disco.
     * @details Implementa il Caso d'Uso 16 (Salvataggio dati). Delega al
     * servizioArchivio la persistenza e resetta il flag di modifica.
     *
     * @param[in] event L'evento scatenante.
     *
     * @post {modificheEffettuate = false}
     * @post I dati sono persistiti su file.
     */
    private void onSalvaArchivio(ActionEvent event) {
        try {
            servizioArchivio.salva();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Salvataggio completato con successo");
            modificheEffettuate = false;
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore durante il salvataggio.");
            alert.setHeaderText("Errore");
            alert.showAndWait();
        }
    }

    /**
     * @brief Gestisce la richiesta di chiusura dell'applicazione.
     * @details Implementa il Caso d'Uso 17 (Chiusura applicazione). Controlla
     * se ci sono modifiche alla flag modificheEffettuate. In caso positivo,
     * chiede conferma all'utente se salvare, scartare o annullare.
     *
     * @param[in] event L'evento di chiusura.
     *
     * @post L'applicazione viene chiusa (con o senza salvataggio) oppure
     * l'operazione viene annullata.
     */
    public void chiudiApplicazione(Event event) {
        // Se l'utente ha fatto ANNULLA o c'è stato errore -> non chiudere
        if (!chiediSeChiudere()) {
            if (event != null) {
                event.consume(); // vale sia per WindowEvent che per ActionEvent
            }
            return;
        }

        // Arrivati qui: utente ha scelto YES (salvato) o NO (non salvare ma uscire)
        if (event instanceof WindowEvent) {
            // è una richiesta di chiusura finestra (X o CMD+Q via JavaFX):
            // NON faccio nulla -> lascio che JavaFX chiuda da solo la Stage
            return;
        }

        // Evento da MenuItem / pulsante: chiudo io la finestra
        root.getScene().getWindow().hide();
    }
    
    private boolean chiediSeChiudere() {
        if (!modificheEffettuate) {
            return true; // nessuna modifica -> si può chiudere subito
        }

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Ci sono modifiche non salvate. Vuoi salvare prima di uscire?",
                ButtonType.CANCEL, ButtonType.NO, ButtonType.YES
        );

        alert.setHeaderText("Conferma chiusura");
        alert.initModality(Modality.WINDOW_MODAL);

        ButtonType risposta = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (risposta == ButtonType.CANCEL) {
            return false; // non chiudere
        }

        if (risposta == ButtonType.YES) {
            try {
                servizioArchivio.salva();
                modificheEffettuate = false;
                return true; // salvataggio ok -> chiudi
            } catch (Exception ex) {
                ex.printStackTrace();
                return false; // errore nel salvataggio -> meglio non chiudere
            }
        }

        // risposta == NO -> non salvare ma chiudi comunque
        return true;
    }
}