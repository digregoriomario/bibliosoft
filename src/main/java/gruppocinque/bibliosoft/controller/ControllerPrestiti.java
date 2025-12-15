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
import javafx.scene.control.TableCell;
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
    private InterfacciaFiltro<Prestito> filtroCorrente = FiltroPrestito.filtraAttivi();

    //servizi per interagire con i dati:
    private ServizioPrestiti servizioPrestiti;
    private ServizioUtenti servizioUtenti;
    private ServizioLibri servizioLibri;
    
    /**
     * @brief Lista osservabile che funge da model per la TableView.
     * @details Contiene i dati dei prestiti filtrati e aggiornati, pronti per
     * essere visualizzati secondo RF 3.4.2.
     */
    private final ObservableList<Prestito> dati = FXCollections.observableArrayList();

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
        //inizializzo i servizi:
        this.servizioPrestiti = servizioPrestiti;
        this.servizioUtenti = servizioUtenti;
        this.servizioLibri = servizioLibri;
        
        inizializzaTabella(); //inizializzo la tabella

        selezionaFiltro(bottoneAttivi); //il filtro iniziale deve essere "Attivi"
        
        aggiorna();//aggiorno tutta la vista
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
        //inizializzo le diverse colonne della tabella:
        colonnaUtente.setCellValueFactory(new PropertyValueFactory<>("utente"));
        colonnaLibro.setCellValueFactory(new PropertyValueFactory<>("libro"));
        colonnaDataInizio.setCellValueFactory(new PropertyValueFactory<>("dataInizio"));
        colonnaDataPrevista.setCellValueFactory(new PropertyValueFactory<>("dataPrevista"));
        colonnaDataEffettiva.setCellValueFactory(new PropertyValueFactory<>("dataRestituzioneEffettiva"));
        colonnaStato.setCellValueFactory(new PropertyValueFactory<>("stato"));
        //funzione anonima per poter dare un colore diverso allo stato (Concluso -> verde, In corso -> arancione, In ritardo -> rosso):
        colonnaStato.setCellFactory(col -> new TableCell<Prestito, StatoPrestito>() {
            @Override
            protected void updateItem(StatoPrestito elemento, boolean vuoto) {
                super.updateItem(elemento, vuoto);

                if (vuoto || elemento == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(elemento.toString());
                setStyle("");

                switch (elemento) {
                    case IN_RITARDO ->
                        setStyle("-fx-text-fill: red;");

                    case IN_CORSO ->
                        setStyle("-fx-text-fill: orange;");

                    case CONCLUSO ->
                        setStyle("-fx-text-fill: green;");

                }
            }
        });

        tabellaPrestiti.setPlaceholder(new Label("Nessun prestito presente"));  //placeholder nel caso in cui non ci sono prestiti (anche dopo averli filtrati)
        tabellaPrestiti.setItems(dati);    //popola la tabella
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
        filtroCorrente = FiltroPrestito.filtraAttivi(); //cambio il filtro corrente e lo imposto su "Attivi"
        selezionaFiltro(bottoneAttivi); 
        aggiorna(); //aggiorno tutta la vista
    }

    /**
     * @brief Imposta il filtro per visualizzare solo i prestiti storici.
     * @details Gestisce l'evento di click sul bottone "Conclusi".
     *
     * @param[in] event L'evento generato dal click sul bottone.
     */
    @FXML
    private void mostraConclusi(ActionEvent event) {
        filtroCorrente = FiltroPrestito.filtraConclusi(); //cambio il filtro corrente e lo imposto su "Conclusi"
        selezionaFiltro(bottoneConclusi);
        aggiorna(); //aggiorno tutta la vista
    }

    /**
     * @brief Rimuove i filtri visualizzando l'intero storico prestiti.
     * @details
     * @param[in] event L'evento generato dal click sul bottone.
     */
    @FXML
    private void mostraTutti(ActionEvent event) {
        filtroCorrente = null;  //cambio il filtro corrente e lo imposto null in modo che restituisca l'intera lista di prestiti
        selezionaFiltro(bottoneTutti);
        aggiorna(); //aggiorno tutta la vista
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
        if (! attivo.getStyleClass().contains("bottoni_filtro_attivo"))
            attivo.getStyleClass().add("bottoni_filtro_attivo");
    }

    /**
     * @brief Aggiorna i dati nelle ComboBox di selezione Utente e Libro.
     * @details Recupera le liste aggiornate dai rispettivi servizi per
     * garantire che si possano creare prestiti solo per utenti e libri
     * esistenti.
     */
    private void aggiornaCombo() {
        //popolo le due combo con i dati prelevati dai servizi:
        comboUtente.setItems(FXCollections.observableArrayList(servizioUtenti.listaUtenti()));
        comboLibro.setItems(FXCollections.observableArrayList(servizioLibri.listaLibri()));
    }

    /**
     * @brief Resetta i campi del form di inserimento prestito.
     * @details Pulisce la selezione delle ComboBox e il DatePicker.
     */
    public void pulisciCampi() {
        //pulisco le combo levando le selezioni:
        comboUtente.getSelectionModel().clearSelection();
        comboUtente.setValue(null);
        comboLibro.getSelectionModel().clearSelection();
        comboLibro.setValue(null);

        dataPrevista.setValue(null);    //pulisco il datepicker
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
     * @post Se errore: Viene mostrato un alert al bibliotecario.
     */
    @FXML
    private void onRegistraPrestito() {
        //prelevo i dati:
        Utente utente = comboUtente.getValue();
        Libro libro = comboLibro.getValue();
        LocalDate data = dataPrevista.getValue();
        if (utente == null) {   //se il bibliotecario non ha selezionato un utente mostro un errore
            mostraErrore("Seleziona utente");
            return;
        }

        if (libro == null) {   //se non ha selezionato un libro mostro un errore
            mostraErrore("Seleziona libro");
            return;
        }

        if (data == null) {   //se non ha selezionato una data mostro un errore
            mostraErrore("Seleziona data di restituzione prevista");
            return;
        }

        try {
            servizioPrestiti.registraPrestito(utente, libro, data); //chiedo al servizio prestiti di registrare il prestito
            ControllerPrincipale.modificheEffettuate = true;    //registro la modifica
            aggiorna(); //aggiorno tutta la vista
        } catch (Exception ex) {
            mostraErrore(ex.getMessage());  //eventuali errori(l'utente ha raggiunto il massimo dei prestiti attivi o il libro non ha copie disponibili)
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
        Prestito prestito = tabellaPrestiti.getSelectionModel().getSelectedItem();  //prendo il prestito che il bibliotecario ha selezionato dalla tabella
        if (prestito == null) { //se non ha selezionato nessun prestito stampo un errore
            mostraErrore("Seleziona un prestito da restituire.");
            return;
        }
        try {
            servizioPrestiti.registraRestituzione(prestito);    //chiedo al servizio prestiti di registrare la restituzione del prestito
            ControllerPrincipale.modificheEffettuate = true;    //registro la modifica
            aggiorna(); //aggiorno tutta la vista
        } catch (Exception ex) {
            mostraErrore(ex.getMessage());  //eventuali errori (il prestito era già concluso)
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
        servizioPrestiti.aggiornaRitardi(); //aggiorno i ritardi di ogni prestito
        dati.setAll(servizioPrestiti.cerca(filtroCorrente));    //popola la tabella filtrata in base al filtro corrente
        aggiornaCombo();    //aggiorno le combo con i dati sui libri e sugli utenti
        pulisciCampi(); //mi assicuro che i campi siano puliti (non ci siano già selezioni)

        dataPrevista.setDayCellFactory(dp -> new DateCell() {   //funzione anoniva per evitare la selezione di date pari a oggi o antecedenti
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
        Alert alert = new Alert(Alert.AlertType.ERROR, messaggio, ButtonType.OK);    //creo un alert per gli errori con pulsante ok  
        alert.setHeaderText("Errore");  //intestazione
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/stile_dialog_alert.css").toExternalForm());//collego il relativo css
        alert.showAndWait();    //mostro l'alert
    }
}
