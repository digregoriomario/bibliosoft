/**
 * @file ControllerUtenti.java
 * @author gruppocinque
 * @version 1.0
 */
package gruppocinque.bibliosoft.controller;

import gruppocinque.bibliosoft.modelli.Prestito;
import static gruppocinque.bibliosoft.modelli.StatoPrestito.CONCLUSO;
import static gruppocinque.bibliosoft.modelli.StatoPrestito.IN_CORSO;
import static gruppocinque.bibliosoft.modelli.StatoPrestito.IN_RITARDO;
import gruppocinque.bibliosoft.modelli.Utente;
import gruppocinque.bibliosoft.servizi.ServizioPrestiti;
import gruppocinque.bibliosoft.servizi.ServizioUtenti;
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
    
    /**
     * @brief Lista osservabile che funge da model per la TableView.
     * @details Contiene i dati degli utenti filtrati e aggiornati, pronti per
     * essere visualizzati secondo RF 3.4.2.
     */
    private final ObservableList<Utente> dati = FXCollections.observableArrayList();

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
        //imposto i servizi necessari:
        this.servizioUtenti = servizioUtenti;
        this.servizioPrestiti = servizioPrestiti;
        
        inizializzaTabella();   //inizializzo la tabella
        aggiorna(); //aggiorno la vista
    }

    /**
     * @brief Configura le colonne della tabella utenti.
     * @details Implementa la visualizzazione tabellare richiesta da RF 3.4.2.
     * Configura il binding tra le colonne (Matricola, Cognome, Nome, Email,
     * Prestiti Attivi) e le proprietà dell'oggetto `Utente`.
     */
    private void inizializzaTabella() {
        //inizializzo le colonne della tabella con le informazioni giuste:
        colonnaMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colonnaCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colonnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colonnaPrestiti.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPrestitiAttivi().size()));

        tabellaUtenti.setPlaceholder(new Label("Nessun utente presente"));   //placeholder nel caso in cui non ci sono utenti (anche dopo la ricerca)
        tabellaUtenti.setItems(dati);   //popolo la tabella
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
        String filtro = campoRicerca.getText(); ///prelevo la stringa filtro dalla text field
        List<Utente> risultati = servizioUtenti.cerca(filtro); //chiedo al servizio utenti di cercare in base al filtro
        dati.setAll(risultati); //popolo la tabella
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
        Dialog<Utente> finestraDialogo = creaDialogUtente(null);   //creo un nuovo dialog
        finestraDialogo.setTitle("Nuovo utente"); //titolo del dialog
        finestraDialogo.showAndWait().ifPresent(utente -> {   //aspetto che venga premuto OK per prendere i dati
            try {
                servizioUtenti.aggiungiUtente(utente); //chiedo al servizio utenti di aggiungere l'utente
                ControllerPrincipale.modificheEffettuate = true;    //registro la modifica
                aggiorna(); //aggiorno tutta la vista
            } catch (Exception ex) {
                mostraErrore(ex.getMessage()); //eventuale errore di validazione dell'utente
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
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem(); //prendo l'utente che il bibliotecario ha selezionato dalla tabella
        if (selezionato == null) {  //se non ha selezionato nulla stampo un messaggio
            mostraErrore("Seleziona un utente.");
            return;
        }
        Dialog<Utente> finestraDialogo = creaDialogUtente(selezionato);    //creo un dialog passando l'utente selezionato (così le text field saranno già popolate)
        finestraDialogo.setTitle("Modifica utente");  //titolo del dialog
        finestraDialogo.showAndWait().ifPresent(utente -> {   //aspetto che l'utente prema OK e prelevo i dati
            try {
                servizioUtenti.modificaUtente(utente); //chiedo al servizio utenti di registrare le modifiche sull'utente
                ControllerPrincipale.modificheEffettuate = true;    //registro la modifica
                aggiorna(); //aggiorno tutta la vista
            } catch (Exception ex) {
                mostraErrore(ex.getMessage());    //eventuale errore di validazione dell'utente
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
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();   //prendo il libro che il bibliotecario ha selezionato dalla tabella
        if (selezionato == null) {  //controllo il caso in cui il bibliotecario non ha selezionato nulla  
            mostraErrore("Seleziona un utente.");
            return;
        }
        //***il seguente controllo è effettuato anche al livello di servizio ma lo ripeto qui affinchè l'alert di conferma non venga mostrato affatto in caso di errore***
        if (selezionato.haPrestitiAttivi()) {    //controllo se l'utente ha prestiti attivi (se ha prestiti attivi non può essere eliminato)
            mostraErrore("L'utente selezionato ha dei prestiti attivi");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi davvero eliminare l'utente selezionato?", ButtonType.YES, ButtonType.NO);   //creo un allert con un messaggio di conferma e due bottoni (cancella e ok)
        alert.setHeaderText("Conferma eliminazione");   //intestazione dell'alert
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/stile_dialog_alert.css").toExternalForm()); ///collego il relativo css
        alert.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) { //se il bibliotecario ha premuto ok...
                try {
                    servizioUtenti.eliminaUtente(selezionato);    //chiedo al servizio utenti di eliminare l'utente selezionato
                    ControllerPrincipale.modificheEffettuate = true;    //registro la modifica
                    aggiorna();//aggiorno tutta la vista
                } catch (Exception ex) {
                    mostraErrore(ex.getMessage());  //eventuali errori
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
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();   //prendo l'utente che il bibliotecario ha selezionato dalla tabella
        if (selezionato == null) {  //se non ha selezionato nessun'utente mostro un errore
            mostraErrore("Seleziona un utente.");
            return;
        }
        List<Prestito> storico = servizioPrestiti.storico(selezionato); //chiedo al servizio prestiti di generarmi lo storico per l'utente selezionato
        StringBuilder sb = new StringBuilder(); //creo una string builder
        
        for (Prestito prestito : storico) { ///per ogni prestito della lista:
            switch (prestito.getStato()) {  //in base allo stato del prestito cambiano i parametri da stampare...
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
        Label contenuto = new Label(storico.isEmpty() ? "Nessun prestito effettuato." : sb.toString()); //label (se lo storico è vuoto stampo un messaggio altrimente stampo la string builder contenente lo storico)
        contenuto.setWrapText(false);   //il testo non deve andare a capo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);   //creo l'alert

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/stile_dialog_alert.css").toExternalForm()); //collego il relativo css
        alert.getDialogPane().setContent(contenuto);    //aggiungo la label all'alert
        alert.setHeaderText("Storico prestiti di " + selezionato);  //intestazione dell'alert
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);   //l'alert deve avere l'altezza minima per mostrare ogni prestito dello storico
        alert.showAndWait();    //mostro l'alert
    }

    /**
     * @brief Ricarica la lista degli utenti dal servizio.
     * @details Utile per sincronizzare la vista dopo modifiche o annullamenti
     * di filtri.
     */
    public void aggiorna() {
        dati.setAll(servizioUtenti.listaUtenti());  //popolo la tabella
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
        Dialog<Utente> dialog = new Dialog<>();  //creo una nuova finestra di dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);   //aggiungo alla finestra il pulsante annulla e ok
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/stile_dialog_alert.css").toExternalForm());    //collego il css relativo ai dialog

        //creo tutte le textfield necessarie e aggiungo i relativi placeholder:
        TextField matricolaField = new TextField();
        matricolaField.setPromptText("Es. 123456");
        TextField nomeField = new TextField();
        nomeField.setPromptText("Es. Mario");
        TextField cognomeField = new TextField();
        cognomeField.setPromptText("Es. Rossi");
        TextField emailField = new TextField();
        emailField.setPromptText("Es. mario.rossi@studenti.unisa.it");

        //se c'è un'utente iniziale (quindi  si tratta di una modifica):
        if (iniziale != null) {
            //presetto tutte le textfield:
            matricolaField.setText(iniziale.getMatricola());
            matricolaField.setDisable(true); //la matricola non può e non deve essere modificata
            nomeField.setText(iniziale.getNome());
            cognomeField.setText(iniziale.getCognome());
            emailField.setText(iniziale.getEmail());
        }

        VBox contenitore = new VBox(20);
        contenitore.setPadding(new Insets(10, 20, 10, 20));

        //riga 1: matricola
        VBox rigaMatricola = new VBox(5);
        Label labelMatricola = new Label("Matricola:");
        labelMatricola.setMinWidth(150);
        rigaMatricola.getChildren().addAll(labelMatricola, matricolaField);

        ///riga 2: nome e cognmome
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

        //riga 3: email
        VBox rigaEmail = new VBox(5);
        Label labelEmail = new Label("Email:");
        labelEmail.setMinWidth(150);
        rigaEmail.getChildren().addAll(labelEmail, emailField);

        //aggiungo le 3 righe al contenitore principale
        contenitore.getChildren().addAll(
                rigaMatricola,
                rigaNomeCognome,
                rigaEmail
        );

        dialog.getDialogPane().setContent(contenitore); //metto il contenitore nella finestra di dialogo
        Node bottoneOk = dialog.getDialogPane().lookupButton(ButtonType.OK); //prelevo il bottone di conferma per fare il binding

        //faccio il binding del bottone con le textfield (il bottone è disabilitato a meno che tutte le textfield contengono qualcosa)
        bottoneOk.disableProperty().bind(matricolaField.textProperty().isEmpty()
                .or(nomeField.textProperty().isEmpty())
                .or(cognomeField.textProperty().isEmpty())
                .or(emailField.textProperty().isEmpty()));

        dialog.setResultConverter(bt -> {   //aspetto che il bibliotecario prema qualcosa
            if (bt == ButtonType.OK) {  //se preme ok
                try {
                    //prelevo i dati:
                    String matricola = matricolaField.getText();
                    String nome = nomeField.getText();
                    String cognome = cognomeField.getText();
                    String email = emailField.getText();
                    return new Utente(matricola, nome, cognome, email); //restituisco il nuovo utente
                } catch (Exception ex) {
                    mostraErrore("Dati non validi: " + ex.getMessage());    //eventuali errori
                    return null;
                }
            }
            return null;   //se il bibliotecario non ha premuto ok restituisco null
        });

        return dialog;  //restituisco la finestra di dialog
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
        Alert alert = new Alert(Alert.AlertType.ERROR, messaggio, ButtonType.OK);   //creo un alert per gli errori con pulsante ok
        alert.setHeaderText("Errore");  //intestazione
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/stile_dialog_alert.css").toExternalForm()); //collego il relativo css
        alert.showAndWait();    //mostro l'alert
    }
}
