/**
 * @file ControllerPrestiti.java
 * @author gruppocinque
 * @version 1.0
 */
package gruppocinque.bibliosoft.controller;

import gruppocinque.bibliosoft.servizi.ServizioLibri;
import gruppocinque.bibliosoft.servizi.ServizioUtenti;
import gruppocinque.bibliosoft.servizi.ServizioPrestiti;
import gruppocinque.bibliosoft.archivi.filtri.FiltroPrestito;
import gruppocinque.bibliosoft.modelli.Prestito;
import gruppocinque.bibliosoft.archivi.filtri.InterfacciaFiltro;
import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.modelli.StatoPrestito;
import gruppocinque.bibliosoft.modelli.Utente;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @brief Controller per la gestione della sezione "Prestiti".
 * @details Questa classe gestisce l'interazione tra l'interfaccia utente e la
 * logica di business relativa ai prestiti. Implementa le funzionalità descritte
 * nei casi d'uso: * UC 12: Visualizzazione monitoraggio prestiti. * UC 14:
 * Registrazione prestito. * UC 15: Registrazione restituzione prestito.
 *
 * Si occupa inoltre della colorazione condizionale delle righe in base allo
 * stato del prestito (In Corso, Concluso, In Ritardo) come richiesto da RF
 * 3.1.3.2.
 *
 * @invariant servizioPrestiti != null
 * @invariant servizioUtenti != null
 * @invariant servizioLibri != null
 */
public class ControllerPrestiti {

    /**
     * @brief Strategia di filtro corrente per la visualizzazione della tabella.
     * @details Determina quali prestiti mostrare (Attivi, Conclusi o Tutti)
     * invocando i filtri definiti in FiltroPrestito.
     */
    private InterfacciaFiltro<Prestito> filtroCorrente;

    //servizi per interagire con i dati:
    private ServizioPrestiti servizioPrestiti;
    private ServizioUtenti servizioUtenti;
    private ServizioLibri servizioLibri;

    //attributi FXML:
    @FXML
    private ComboBox<Utente> comboUtente;
    @FXML
    private ComboBox<Libro> comboLibro;
    @FXML
    private DatePicker dataPrevista;
    @FXML
    private TableView<Prestito> tabellaPrestiti;
    @FXML
    private TableColumn<Prestito, String> colonnaUtente;
    @FXML
    private TableColumn<Prestito, String> colonnaLibro;
    @FXML
    private TableColumn<Prestito, LocalDate> colonnaDataInizio;
    @FXML
    private TableColumn<Prestito, LocalDate> colonnaDataPrevista;
    @FXML
    private TableColumn<Prestito, LocalDate> colonnaDataEffettiva;
    @FXML
    private TableColumn<Prestito, StatoPrestito> colonnaStato;
    @FXML
    private Button bottoneAttivi;
    @FXML
    private Button bottoneConclusi;
    @FXML
    private Button bottoneTutti;

    /**
     * @brief Lista osservabile che funge da model per la TableView.
     * @details Contiene i dati dei prestiti filtrati e aggiornati, pronti per
     * essere visualizzati secondo RF 3.4.2.
     */
    private final ObservableList<Prestito> datiPrestiti = FXCollections.observableArrayList();

    /**
     * @brief Inizializza i servizi e configura lo stato iniziale della vista.
     * @details Inietta le dipendenze necessarie, configura le colonne della
     * tabella e imposta il filtro predefinito sui prestiti attivi (In Corso/In
     * Ritardo).
     *
     * @param[in] servizioPrestiti Gestore della logica di business dei
     * prestiti.
     * @param[in] servizioUtenti Gestore per il recupero degli utenti.
     * @param[in] servizioLibri Gestore per il recupero dei libri.
     *
     * @pre I servizi passati non devono essere null.
     * @post La tabella è inizializzata e popolata con i dati correnti.
     */
    public void impostaServizi(ServizioPrestiti servizioPrestiti, ServizioUtenti servizioUtenti, ServizioLibri servizioLibri) {
        this.servizioPrestiti = servizioPrestiti;
        this.servizioUtenti = servizioUtenti;
        this.servizioLibri = servizioLibri;
        inizializzaTabella();

        selezionaFiltro(bottoneAttivi);
        aggiorna();
    }

    /**
     * @brief Configura le colonne della tabella e la formattazione
     * condizionale.
     * @details Implementa RF 3.1.3.2: * Associa le proprietà del modello alle
     * colonne. * Definisce una CellFactory personalizzata per la colonna
     * "Stato" per colorare il testo (Rosso per ritardi, Arancione per in corso,
     * Verde per conclusi).
     */
    private void inizializzaTabella() {
        colonnaUtente.setCellValueFactory(new PropertyValueFactory<>("utente"));
        colonnaLibro.setCellValueFactory(new PropertyValueFactory<>("libro"));
        colonnaDataInizio.setCellValueFactory(new PropertyValueFactory<Prestito, java.time.LocalDate>("dataInizio"));
        colonnaDataPrevista.setCellValueFactory(new PropertyValueFactory<Prestito, java.time.LocalDate>("dataPrevista"));
        colonnaDataEffettiva.setCellValueFactory(new PropertyValueFactory<Prestito, java.time.LocalDate>("dataRestituzioneEffettiva"));
        colonnaStato.setCellValueFactory(new PropertyValueFactory<Prestito, StatoPrestito>("stato"));

        tabellaPrestiti.setPlaceholder(new Label("Nessun prestito presente"));
        tabellaPrestiti.setItems(datiPrestiti);
    }

    /**
     * @brief Imposta il filtro per visualizzare solo i prestiti attivi.
     * @details Gestisce l'evento di click sul bottone "In Corso". Mostra
     * prestiti con stato "In Corso" e "In Ritardo" (UC 12).
     *
     * @param[in] event L'evento generato dal click sul bottone.
     */
    @FXML
    private void mostraAttivi(ActionEvent event) {
        filtroCorrente = FiltroPrestito.filtraAttivi();
        selezionaFiltro(bottoneAttivi);
        aggiorna();
    }

    /**
     * @brief Imposta il filtro per visualizzare solo i prestiti storici.
     * @details Gestisce l'evento di click sul bottone "Conclusi".
     *
     * @param[in] event L'evento generato dal click sul bottone.
     */
    @FXML
    private void mostraConclusi(ActionEvent event) {
        filtroCorrente = FiltroPrestito.filtraConclusi();
        selezionaFiltro(bottoneConclusi);
        aggiorna();
    }

    /**
     * @brief Rimuove i filtri visualizzando l'intero storico prestiti.
     * @details
     * @param[in] event L'evento generato dal click sul bottone.
     */
    @FXML
    private void mostraTutti(ActionEvent event) {
        filtroCorrente = null;
        selezionaFiltro(bottoneTutti);
        aggiorna();
    }

    /**
     * @brief Gestisce lo stile visivo dei bottoni di filtro.
     * @details Evidenzia il bottone del filtro attualmente attivo per fornire
     * feedback all'utente.
     *
     * @param[in] attivo Il bottone che è stato appena premuto.
     */
    private void selezionaFiltro(Button attivo) {
        // rimuovo lo stile "attivo" da tutti
        bottoneAttivi.getStyleClass().remove("bottoni_filtro_attivo");
        bottoneConclusi.getStyleClass().remove("bottoni_filtro_attivo");
        bottoneTutti.getStyleClass().remove("bottoni_filtro_attivo");

        // aggiungo lo stile "attivo" solo a quello selezionato
        if (!attivo.getStyleClass().contains("bottoni_filtro_attivo")) {
            attivo.getStyleClass().add("bottoni_filtro_attivo");
        }
    }

    /**
     * @brief Aggiorna i dati nelle ComboBox di selezione Utente e Libro.
     * @details Recupera le liste aggiornate dai rispettivi servizi per
     * garantire che si possano creare prestiti solo per utenti e libri
     * esistenti.
     */
    private void aggiornaCombo() {
        comboUtente.setItems(FXCollections.observableArrayList(servizioUtenti.listaUtenti()));
        comboLibro.setItems(FXCollections.observableArrayList(servizioLibri.listaLibri()));
    }

    /**
     * @brief Resetta i campi del form di inserimento prestito.
     * @details Pulisce la selezione delle ComboBox e il DatePicker.
     */
    public void pulisciCampi() {
        comboUtente.getSelectionModel().clearSelection();
        comboUtente.setValue(null);

        comboLibro.getSelectionModel().clearSelection();
        comboLibro.setValue(null);

        dataPrevista.setValue(null);
    }

    /**
     * @brief Gestisce la creazione di un nuovo prestito (UC 14 - Registrazione
     * prestito, RF 3.2.1 - Registrazione prestiti).
     * @details Verifica che i dati siano inseriti, delega la validazione
     * (disponibilità copie, max prestiti utente) al ServizioPrestiti e aggiorna
     * la vista. Imposta il flag globale
     * ControllerPrincipale.modificheEffettuate a true.
     *
     *
     * @post Se successo: Copie libro decrementate, nuovo prestito in lista.
     * @post Se errore: Viene mostrato un alert all'utente.
     */
    @FXML
    private void onRegistraPrestito() {
        Utente utente = comboUtente.getValue();
        Libro libro = comboLibro.getValue();
        LocalDate data = dataPrevista.getValue();
        if (utente == null) {
            mostraErrore("Seleziona utente");
            return;
        }

        if (libro == null) {
            mostraErrore("Seleziona libro");
            return;
        }

        if (data == null) {
            mostraErrore("Seleziona data di restituzione prevista");
            return;
        }

        try {
            servizioPrestiti.registraPrestito(utente, libro, data);
            ControllerPrincipale.modificheEffettuate = true;
            aggiorna();
        } catch (Exception ex) {
            mostraErrore(ex.getMessage());
        }
    }

    /**
     * @brief Gestisce la restituzione di un libro (UC 15 - Registrazione
     * restituzione prestito, RF 3.2.2 - Restituzione libri).
     * @details Recupera il prestito selezionato nella tabella e ne registra la
     * conclusione. Imposta il flag globale
     * ControllerPrincipale.modificheEffettuate a true.
     *
     *
     * @post Se successo: Stato prestito = Concluso, copie libro incrementate.
     */
    @FXML
    private void onRegistraRestituzione() {
        Prestito prestito = tabellaPrestiti.getSelectionModel().getSelectedItem();
        if (prestito == null) {
            mostraErrore("Seleziona un prestito da restituire.");
            return;
        }
        try {
            servizioPrestiti.registraRestituzione(prestito);
            ControllerPrincipale.modificheEffettuate = true;
            aggiorna();
        } catch (Exception ex) {
            mostraErrore(ex.getMessage());
        }
    }

    /**
     * @brief Aggiorna i dati visualizzati e ricalcola i ritardi.
     * @details Metodo centrale di refresh: 1. Invoca il calcolo automatico dei
     * ritardi (RF 3.2.3). 2. Recupera la lista filtrata dal servizio. 3.
     * Aggiorna la TableView. 4. Aggiorna le ComboBox e riconfigura il
     * DatePicker per disabilitare date passate.
     */
    public void aggiorna() {
        servizioPrestiti.aggiornaRitardi();
        datiPrestiti.setAll(servizioPrestiti.cerca(filtroCorrente));
        aggiornaCombo();
        pulisciCampi();

        dataPrevista.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate elemento, boolean vuoto) {
                super.updateItem(elemento, vuoto);
                LocalDate oggi = LocalDate.now();
                if (elemento != null && (elemento.isBefore(oggi) || elemento.isEqual(oggi))) {
                    setDisable(true);
                }

            }
        });
    }

    /**
     * @brief Mostra una finestra di dialogo di errore.
     * @details Utilizzato per feedback su validazione fallita o eccezioni di
     * business (es. utente con troppi prestiti o libro non disponibile).
     * Implementa RF 4.1.2 (Feedback errore).
     *
     * @param[in] messaggio Il testo dell'errore da visualizzare.
     */
    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR, messaggio, ButtonType.OK);
        alert.setHeaderText("Errore");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/css/stile_dialog_alert.css").toExternalForm()
        );
        alert.showAndWait();
    }
}
