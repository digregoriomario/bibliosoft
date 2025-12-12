/**
 * @file ControllerUtenti.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.controller;

import gruppo5.bibliosoft.modelli.Prestito;
import static gruppo5.bibliosoft.modelli.StatoPrestito.CONCLUSO;
import static gruppo5.bibliosoft.modelli.StatoPrestito.IN_CORSO;
import static gruppo5.bibliosoft.modelli.StatoPrestito.IN_RITARDO;
import gruppo5.bibliosoft.modelli.Utente;
import gruppo5.bibliosoft.servizi.ServizioPrestiti;
import gruppo5.bibliosoft.servizi.ServizioUtenti;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * @brief Controller per la gestione della sezione "Utenti".
 * @details Questa classe gestisce l'interazione tra l'interfaccia utente
 * (tabella utenti, dialoghi di inserimento/modifica) e la logica di business.
 *
 * Implementa le funzionalità descritte nei seguenti casi d'uso: * UC 7:
 * Visualizzazione lista utenti. * UC 8: Inserimento nuovo utente. * UC 9:
 * Modifica dati utente. * UC 10: Cancellazione utente. * UC 11: Ricerca utente.
 * * UC 13: Storico prestiti.
 *
 * @invariant {@code servizioUtenti != null}
 * @invariant {@code servizioPrestiti != null}
 */
public class ControllerUtenti {

    //servizi per interagire con i dati:
    private ServizioUtenti servizioUtenti;
    private ServizioPrestiti servizioPrestiti;

    //attributi FXML:
    @FXML
    private TextField campoRicerca;
    @FXML
    private TableView<Utente> tabellaUtenti;
    @FXML
    private TableColumn<Utente, String> colonnaMatricola;
    @FXML
    private TableColumn<Utente, String> colonnaCognome;
    @FXML
    private TableColumn<Utente, String> colonnaNome;
    @FXML
    private TableColumn<Utente, String> colonnaEmail;
    @FXML
    private TableColumn<Utente, Number> colonnaPrestiti;

    /**
     * @brief Lista osservabile che funge da model per la TableView.
     * @details Contiene i dati degli utenti filtrati e aggiornati, pronti per
     * essere visualizzati secondo RF 3.4.2.
     */
    private final ObservableList<Utente> dati = FXCollections.observableArrayList();

    /**
     * @brief Inizializza i servizi e configura lo stato iniziale della vista.
     * @details Inietta le dipendenze necessarie, configura le colonne della
     * tabella e carica la lista iniziale degli utenti.
     *
     * @param[in] servizioUtenti Gestore della logica di business relativa agli
     * utenti.
     * @param[in] servizioPrestiti Gestore necessario per recuperare lo storico
     * prestiti (UC 13).
     *
     * @pre I servizi passati non devono essere null.
     * @post La tabella è inizializzata e popolata con i dati correnti.
     */
    public void impostaServizi(ServizioUtenti servizioUtenti, ServizioPrestiti servizioPrestiti) {
        this.servizioUtenti = servizioUtenti;
        this.servizioPrestiti = servizioPrestiti;
        inizializzaTabella();
        aggiorna();
    }

    /**
     * @brief Configura le colonne della tabella utenti.
     * @details Implementa la visualizzazione tabellare richiesta da RF 3.4.2.
     * Configura il binding tra le colonne (Matricola, Cognome, Nome, Email,
     * Prestiti Attivi) e le proprietà dell'oggetto `Utente`.
     */
    private void inizializzaTabella() {
        colonnaMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colonnaCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colonnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colonnaPrestiti.setCellValueFactory(c
                -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPrestitiAttivi().size()));

        tabellaUtenti.setPlaceholder(new Label("Nessun utente presente"));
        tabellaUtenti.setItems(dati);
    }

    /**
     * @brief Esegue la ricerca degli utenti (UC 11 - Ricerca utente, RF 3.1.2.5
     * - Ricerca).
     * @details Filtra la lista visualizzata in base alla stringa inserita nel
     * campo di ricerca. La ricerca avviene per cognome o matricola.
     *
     */
    @FXML
    private void onRicerca() {
        String filtro = campoRicerca.getText();
        List<Utente> result = servizioUtenti.cerca(filtro);
        dati.setAll(result);
    }

    /**
     * @brief Gestisce l'inserimento di un nuovo utente (UC 8 - Inserimento
     * nuovo utente, RF 3.1.2.1 - Inserimento dati).
     * @details Apre una finestra di dialogo modale per l'input dei dati. Se
     * l'operazione va a buon fine, aggiorna la lista e imposta il flag
     * `ControllerPrincipale.modificheEffettuate` a true.
     *
     * @post Un nuovo utente è aggiunto all'archivio se i dati sono validi.
     */
    @FXML
    private void onAggiungi() {
        Dialog<Utente> finestraDialogo = creaDialogUtente(null);
        finestraDialogo.setTitle("Nuovo utente");
        finestraDialogo.showAndWait().ifPresent(u -> {
            try {
                servizioUtenti.aggiungiUtente(u);
                ControllerPrincipale.modificheEffettuate = true;
                aggiorna();
            } catch (Exception ex) {
                mostraErrore(ex.getMessage());
            }
        });
    }

    /**
     * @brief Gestisce la modifica di un utente esistente (UC 9 - Modifica dati
     * utente, RF 3.1.2.2 - Modifica dati).
     * @details Recupera l'utente selezionato e apre il dialogo con i campi
     * pre-compilati. Aggiorna i dati in memoria e imposta il flag
     * `ControllerPrincipale.modificheEffettuate` a true.
     *
     *
     * @pre Deve essere selezionato un utente dalla tabella.
     */
    @FXML
    private void onModifica() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Seleziona un utente.");
            return;
        }
        Dialog<Utente> finestraDialogo = creaDialogUtente(selezionato);
        finestraDialogo.setTitle("Modifica utente");
        finestraDialogo.showAndWait().ifPresent(u -> {
            try {
                selezionato.setNome(u.getNome());
                selezionato.setCognome(u.getCognome());
                selezionato.setEmail(u.getEmail());
                servizioUtenti.modificaUtente(selezionato);
                ControllerPrincipale.modificheEffettuate = true;
                aggiorna();
            } catch (Exception ex) {
                mostraErrore(ex.getMessage());
            }
        });
    }

    /**
     * @brief Gestisce la cancellazione di un utente (UC 10 - Cancellazione
     * utente, RF 3.1.2.3 - Cancellazione dati).
     * @details Chiede conferma all'operatore prima di procedere. Implementa il
     * vincolo di integrità: impedisce l'eliminazione se l'utente ha prestiti
     * attivi (RF 3.1.2.3 Vincolo).
     *
     *
     * @post Se l'utente non ha prestiti attivi, viene rimosso dall'archivio.
     * @post Se l'utente ha prestiti attivi, viene mostrato un errore.
     */
    @FXML
    private void onElimina() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Seleziona un utente.");
            return;
        }
        if (selezionato.haPrestitiAttivi()) {
            mostraErrore("L'utente selezionato ha dei prestiti attivi");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Vuoi davvero eliminare l'utente selezionato?",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Conferma eliminazione");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/css/stile_viste.css").toExternalForm()
        );
        alert.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                try {
                    servizioUtenti.eliminaUtente(selezionato);
                    ControllerPrincipale.modificheEffettuate = true;
                    aggiorna();
                } catch (Exception ex) {
                    mostraErrore(ex.getMessage());
                }
            }
        });
    }

    /**
     * @brief Visualizza lo storico dei prestiti per l'utente selezionato (UC 13
     * - Storico prestiti).
     * @details Recupera la lista di tutti i prestiti (attivi e conclusi)
     * associati all'utente tramite `servizioPrestiti` e li mostra in una
     * finestra di dialogo informativa.
     *
     * RF 3.1.3.2 Monitoraggio prestiti (storico specifico utente).
     *
     * @pre Deve essere selezionato un utente dalla tabella.
     */
    @FXML
    private void onStorico() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Seleziona un utente.");
            return;
        }
        List<Prestito> storico = servizioPrestiti.storico(selezionato);
        StringBuilder sb = new StringBuilder();
        //In corso, ritardo -> Titolo - Inzio - Prevista - Stato
        //Concluso -> Titolo - Inzio - Prevista  - Effettiva - Stato
        for (Prestito prestito : storico) {
            switch (prestito.getStato()) {
                case IN_CORSO:
                case IN_RITARDO:
                    sb.append(prestito.getLibro().getTitolo())
                            .append(" - Inizio: ").append(prestito.getDataInizio())
                            .append(" Prevista: ").append(prestito.getDataPrevista())
                            .append(" Stato: ").append(prestito.getStato())
                            .append("\n\n");
                    break;

                case CONCLUSO:
                    sb.append(prestito.getLibro().getTitolo())
                            .append(" - Inizio: ").append(prestito.getDataInizio())
                            .append(" Prevista: ").append(prestito.getDataPrevista())
                            .append(" Effetiva: ").append(prestito.getDataRestituzioneEffettiva())
                            .append(" Stato: ").append(prestito.getStato())
                            .append("\n\n");
                    break;
            }
        }
        Label contenuto = new Label(sb.length() == 0 ? "Nessun prestito effettuato." : sb.toString());
        contenuto.setWrapText(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/css/stile_viste.css").toExternalForm()
        );
        alert.getDialogPane().setContent(contenuto);
        alert.setHeaderText("Storico prestiti di " + selezionato);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * @brief Ricarica la lista degli utenti dal servizio.
     * @details Utile per sincronizzare la vista dopo modifiche o annullamenti
     * di filtri.
     */
    public void aggiorna() {
        dati.setAll(servizioUtenti.listaUtenti());
    }

    /**
     * @brief Metodo helper per creare e configurare il dialog di input utente.
     * @details Costruisce il form contenente i campi Matricola, Nome, Cognome
     * ed Email. Se viene passato un utente esistente, i campi vengono
     * pre-popolati (per la modifica) e la matricola viene resa non
     * modificabile.
     *
     * @param[in] iniziale L'utente da modificare, o null per un nuovo
     * inserimento.
     * @return Un'istanza di Dialog configurata per restituire un oggetto
     * Utente.
     */
    private Dialog<Utente> creaDialogUtente(Utente iniziale) {
        Dialog<Utente> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/css/stile_viste.css").toExternalForm()
        );

        TextField matricolaField = new TextField();
        TextField nomeField = new TextField();
        TextField cognomeField = new TextField();
        TextField emailField = new TextField();

        if (iniziale != null) {
            matricolaField.setText(iniziale.getMatricola());
            matricolaField.setDisable(true);
            nomeField.setText(iniziale.getNome());
            cognomeField.setText(iniziale.getCognome());
            emailField.setText(iniziale.getEmail());
        }

        VBox contenitore = new VBox(10);
        contenitore.setPadding(new Insets(10, 20, 10, 20));

        VBox colNome = new VBox(5);
        Label labelNome = new Label("Nome:");
        labelNome.setMinWidth(150);
        colNome.getChildren().addAll(labelNome, nomeField);

        VBox colCognome = new VBox(5);
        Label labelCognome = new Label("Cognome:");
        labelCognome.setMinWidth(150);
        colCognome.getChildren().addAll(labelCognome, cognomeField);

        HBox rigaNomeCognome = new HBox(20);
        rigaNomeCognome.getChildren().addAll(colNome, colCognome);

        VBox colMatricola = new VBox(5);
        Label labelMatricola = new Label("Matricola:");
        labelMatricola.setMinWidth(150);
        colMatricola.getChildren().addAll(labelMatricola, matricolaField);

        VBox colEmail = new VBox(5);
        Label labelEmail = new Label("Email istituzionale:");
        labelEmail.setMinWidth(150);
        colEmail.getChildren().addAll(labelEmail, emailField);

        HBox rigaMatricolaEmail = new HBox(20);
        rigaMatricolaEmail.getChildren().addAll(colMatricola, colEmail);

        contenitore.getChildren().addAll(
                rigaNomeCognome,
                rigaMatricolaEmail
        );

        dialog.getDialogPane().setContent(contenitore);

       
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);

        okButton.disableProperty().bind(matricolaField.textProperty().isEmpty()
                .or(nomeField.textProperty().isEmpty())
                .or(cognomeField.textProperty().isEmpty())
                .or(emailField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    String m = matricolaField.getText();
                    String n = nomeField.getText();
                    String c = cognomeField.getText();
                    String e = emailField.getText();
                    return new Utente(m, n, c, e);
                } catch (Exception ex) {
                    mostraErrore("Dati non validi: " + ex.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    /**
     * @brief Mostra una finestra di dialogo di errore.
     * @details Implementa il requisito non funzionale di feedback errore (RF
     * 4.1.2). Utilizzato per segnalare dati non validi (es. email errata) o
     * violazioni di vincoli (es. cancellazione utente con prestiti).
     *
     * @param[in] messaggio Il testo dell'errore da visualizzare.
     */
    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR, messaggio, ButtonType.OK);
        alert.setHeaderText("Errore");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/css/stile_viste.css").toExternalForm()
        );
        alert.showAndWait();
    }
}
