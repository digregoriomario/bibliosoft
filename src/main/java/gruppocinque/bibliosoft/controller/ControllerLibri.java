/**
 * @file ControllerLibri.java
 * @author gruppocinque
 * @version 1.0
 */
package gruppocinque.bibliosoft.controller;

import gruppocinque.bibliosoft.archivi.filtri.FiltroLibro;
import gruppocinque.bibliosoft.modelli.Libro;
import gruppocinque.bibliosoft.servizi.ServizioLibri;
import java.util.Arrays;
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
import javafx.scene.layout.VBox;

/**
 * @brief Controller per la gestione della vista "Libri" (RF 3.1.1).
 * @details Gestisce l'interfaccia utente per l'inserimento, modifica,
 * cancellazione e ricerca dei libri nel catalogo. Implementa i casi d'uso dal
 * UC2 al UC6.
 *
 * @invariant {@code servizioLibri != null}
 */
public class ControllerLibri {
    private ServizioLibri servizioLibri;    //servizio libri con cui interagire per i dati
    
    /**
     * @brief Lista osservabile che funge da model per la TableView.
     * @details Contiene i dati dei prestiti filtrati e aggiornati, pronti per
     * essere visualizzati secondo RF 3.4.2.
     */
    private final ObservableList<Libro> dati = FXCollections.observableArrayList();

    //attributi fxml:
    @FXML
    private TextField campoRicerca;
    @FXML
    private TableView<Libro> tabellaLibri;
    @FXML
    private TableColumn<Libro, String> colonnaIsbn;
    @FXML
    private TableColumn<Libro, String> colonnaTitolo;
    @FXML
    private TableColumn<Libro, List<String>> colonnaAutori;
    @FXML
    private TableColumn<Libro, Integer> colonnaAnno;
    @FXML
    private TableColumn<Libro, Integer> colonnaCopieTotali;
    @FXML
    private TableColumn<Libro, Integer> colonnaCopieDisponibili;

    /**
     * @brief Inizializza il controller e collega il servizio di backend.
     * @details
     * @param[in] servizioLibri Istanza del servizio per la gestione della
     * logica libri.
     *
     * @post La tabella viene popolata con i dati attuali.
     */
    public void impostaServizi(ServizioLibri servizioLibri) {
        this.servizioLibri = servizioLibri; //inizializzo il servizio libri
        inizializzaTabella();   //inizializzo la tabella
        aggiorna(); //aggiorno tutta la vista
    }

    /**
     * @brief Configura le colonne della tabella e il binding delle proprietà.
     * @details Implementa il requisito di visualizzazione lista ordinata (RF
     * 3.1.1.4).
     */
    private void inizializzaTabella() {
        //inizializzo le diverse colonne della tabella:
        colonnaIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colonnaTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colonnaAutori.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colonnaAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        colonnaCopieTotali.setCellValueFactory(new PropertyValueFactory<>("copieTotali"));
        colonnaCopieDisponibili.setCellValueFactory(new PropertyValueFactory<>("copieDisponibili"));

        tabellaLibri.setPlaceholder(new Label("Nessun libro presente"));    //placeholder nel caso in cui non ci sono libri (anche dopo la ricerca)
        tabellaLibri.setItems(dati);    //popola la tabella
    }

    /**
     * @brief Gestisce l'evento di ricerca.
     * @details Implementa il Caso d'Uso 6 (Ricerca libro) e il requisito RF
     * 3.1.1.5. Utilizza FiltroLibro per filtrare i risultati in base al testo
     * inserito.
     *
     * @post La tabella mostra solo i libri che soddisfano il criterio di
     * ricerca.
     */
    @FXML
    private void onRicerca() {
        String filtro = campoRicerca.getText(); //prelevo la stringa filtro dal campo di ricerca
        List<Libro> risultati = servizioLibri.cercaLibri(FiltroLibro.ricerca(filtro));  //chiedo al servizio libri di cercare tra i libri tramite il filtro
        dati.setAll(risultati); //popolo la tabella con i risultati
    }

    /**
     * @brief Gestisce l'apertura del dialog per inserire un nuovo libro.
     * @details Implementa il Caso d'Uso 2 (Inserimento nuovo libro). Se
     * l'operazione va a buon fine, imposta il flag modificheEffettuate su true,
     * per segnalare la modifica.
     */
    @FXML
    private void onAggiungi() {
        Dialog<Libro> dialog = creaDialogLibro(null);   //creo un nuovo dialog
        dialog.setTitle("Nuovo libro"); //titolo del dialog
        dialog.showAndWait().ifPresent(libro -> {   //aspetto che venga premuto OK per prendere i dati
            try {
                servizioLibri.aggiungiLibro(libro); //chiedo al servizio libri di aggiungere il libro
                ControllerPrincipale.modificheEffettuate = true;    //registro la modifica
                aggiorna(); //aggiorno tutta la vista
            } catch (Exception ex) {
                mostraErrore(ex.getMessage());  //eventuale errore di validazione del libro
            }
        });
    }

    /**
     * @brief Gestisce la modifica di un libro esistente.
     * @details Implementa il Caso d'Uso 3 (Modifica dati libro). Precarica i
     * dati del libro selezionato nel dialog.
     *
     * @pre Deve essere selezionato un libro nella tabella.
     * @post I dati del libro selezionato vengono aggiornati nel modello.
     */
    @FXML
    private void onModifica() {
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem(); //prendo il libro che il bibliotecario ha selezionato dalla tabella
        if (selezionato == null) {  //se non ha selezionato nulla stampo un messaggio
            mostraErrore("Seleziona un libro dalla tabella.");
            return;
        }

        Dialog<Libro> finestraDialog = creaDialogLibro(selezionato);    //creo un dialog passando il libro selezionato (così le text field saranno già popolate)
        finestraDialog.setTitle("Modifica libro");  //titolo del dialog
        finestraDialog.showAndWait().ifPresent(libro -> {   //aspetto che il bibliotecario prema OK e prelevo i dati
            try {
                servizioLibri.modificaLibro(libro); //chiedo al servizio libri di registrare le modifiche sul libro
                ControllerPrincipale.modificheEffettuate = true;    //registro la modifica
                aggiorna(); //aggiorno tutta la vista
            } catch (Exception ex) {
                mostraErrore(ex.getMessage());    //eventuale errore di validazione del libro
            }
        });
    }

    /**
     * @brief Gestisce l'eliminazione di un libro.
     * @details Implementa il Caso d'Uso 4 (Cancellazione libro). Verifica
     * preventivamente se il libro ha prestiti attivi (violazione vincoli).
     * Chiede conferma al bibliotecario prima di procedere.
     *
     * @pre Deve essere selezionato un libro.
     * @pre Il libro non deve avere prestiti attivi.
     * @post Se l'operazione ha successo, il libro è rimosso e la tabella
     * aggiornata.
     * @post Se si verifica un errore (es. prestiti attivi), viene mostrato un
     * Alert al bibliotecario.
     */
    @FXML
    private void onElimina() {
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem(); //prendo il libro che il bibliotecario ha selezionato dalla tabella
        if (selezionato == null) {  //controllo il caso in cui il bibliotecario non ha selezionato nulla  
            mostraErrore("Seleziona un libro da eliminare.");
            return;
        }
        //***il seguente controllo è effettuato anche al livello di servizio ma lo ripeto qui affinchè l'alert di conferma non venga mostrato affatto in caso di errore***
        if (selezionato.haPrestitiAttivi()) {    //controllo se il libro ha prestiti attivi (se ha prestiti attivi non può essere eliminato)
            mostraErrore("Impossibile eliminare: libro in prestito o senza copie disponibili");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vuoi davvero eliminare il libro selezionato?", ButtonType.YES, ButtonType.NO);   //creo un allert con un messaggio di conferma e due bottoni (cancella e ok)
        alert.setHeaderText("Conferma eliminazione");   //intestazione dell'alert
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/stile_dialog_alert.css").toExternalForm()); //collego il css per gli alert
        alert.showAndWait().ifPresent(bt -> {   //aspetto che il bibliotecario prema un pulsante
            if (bt == ButtonType.YES) { //se il bibliotecario ha premuto ok...
                try {
                    servizioLibri.eliminaLibro(selezionato);    //chiedo al servizio libri di eliminare il libro selezionato
                    ControllerPrincipale.modificheEffettuate = true;    //registro la modifica
                    aggiorna(); //aggiorno tutta la vista
                } catch (Exception ex) {
                    mostraErrore(ex.getMessage());  //eventuali errori
                }
            }
        });
    }

    /**
     * @brief Aggiorna la vista recuperando la lista completa dei libri.
     * @details Invocato dopo ogni modifica o cambio tab.
     */
    public void aggiorna() {
        dati.setAll(servizioLibri.listaLibri());    //popolo la tabella con i nuovi dati
    }

    /**
     * @brief Crea una finestra di dialogo modale per inserimento/modifica
     * libro.
     * @details
     * @param[in] iniziale Il libro da modificare (null per un nuovo
     * inserimento).
     *
     * @return Dialog configurato con i campi di input e validazione base.
     */
    private Dialog<Libro> creaDialogLibro(Libro iniziale) {
        Dialog<Libro> finestraDialog = new Dialog<>();  //creo una nuova finestra di dialog
        finestraDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);   //aggiungo alla finestra il pulsante annulla e ok
        finestraDialog.getDialogPane().getStylesheets().add(getClass().getResource("/css/stile_dialog_alert.css").toExternalForm());    //collego il css relativo ai dialog

        //creo tutte le textfield necessarie e aggiungo i relativi placeholder:
        TextField isbnField = new TextField();
        isbnField.setPromptText("Es. 9788800000000");
        TextField titoloField = new TextField();
        titoloField.setPromptText("Es. Il nome della rosa");
        TextField autoriField = new TextField();
        autoriField.setPromptText("(autori separati dalla ,)");
        TextField annoField = new TextField();
        annoField.setPromptText("Es. 2020");
        TextField copieField = new TextField();
        copieField.setPromptText("Es. 5");
        
        //se c'è un libro iniziale (quindi  si tratta di una modifica):
        if (iniziale != null) {
            //presetto tutte le textfield:
            isbnField.setText(iniziale.getIsbn());
            isbnField.setDisable(true); //l'isbn non può e non deve essere modificato
            titoloField.setText(iniziale.getTitolo());
            autoriField.setText(String.join(", ", iniziale.getAutori()));
            annoField.setText(String.valueOf(iniziale.getAnnoPubblicazione()));
            copieField.setText(String.valueOf(iniziale.getCopieTotali()));
        }

        VBox contenitore = new VBox(20);    //contenitore generico
        contenitore.setPadding(new Insets(10, 20, 10, 20)); //padiing

        //riga 1: isbn
        VBox rigaIsbn = new VBox(5);
        Label labelIsbn = new Label("ISBN:");
        labelIsbn.setMinWidth(150);
        rigaIsbn.getChildren().addAll(labelIsbn, isbnField);

        ///riga 2: titolo e autori
        VBox colTitolo = new VBox(5);
        Label labelTitolo = new Label("Titolo:");
        labelTitolo.setMinWidth(150);
        colTitolo.getChildren().addAll(labelTitolo, titoloField);
        VBox colAutori = new VBox(5);
        Label labelAutori = new Label("Autori:");
        labelAutori.setMinWidth(150);
        colAutori.getChildren().addAll(labelAutori, autoriField);
        HBox rigaTitoloAutori = new HBox(20);
        rigaTitoloAutori.getChildren().addAll(colTitolo, colAutori);

        //riga 3: anno di pubblicazione e copie totali
        VBox colAnno = new VBox(5);
        Label labelAnno = new Label("Anno:");
        labelAnno.setMinWidth(150);
        colAnno.getChildren().addAll(labelAnno, annoField);
        VBox colCopie = new VBox(5);
        Label labelCopie = new Label("Copie totali:");
        labelCopie.setMinWidth(150);
        colCopie.getChildren().addAll(labelCopie, copieField);
        HBox rigaAnnoCopie = new HBox(20);
        rigaAnnoCopie.getChildren().addAll(colAnno, colCopie);

        //aggiungo le 3 righe al contenitore principale
        contenitore.getChildren().addAll(
                rigaIsbn,
                rigaTitoloAutori,
                rigaAnnoCopie
        );

        finestraDialog.getDialogPane().setContent(contenitore); //metto il contenitore nella finestra di dialogo

        Node bottoneOk = finestraDialog.getDialogPane().lookupButton(ButtonType.OK); //prelevo il bottone di conferma per fare il binding

        //faccio il binding del bottone con le textfield (il bottone è disabilitato a meno che tutte le textfield contengono qualcosa)
        bottoneOk.disableProperty().bind(
                isbnField.textProperty().isEmpty()
                        .or(titoloField.textProperty().isEmpty())
                        .or(autoriField.textProperty().isEmpty())
                        .or(annoField.textProperty().isEmpty())
                        .or(copieField.textProperty().isEmpty())
        );

        finestraDialog.setResultConverter(bt -> {   //aspetto che il bibliotecario prema qualcosa
            if (bt == ButtonType.OK) {  //se preme ok
                try {
                    //prelevo i dati e faccio i parsing e le conversioni necessarie:
                    String isbn = isbnField.getText();
                    String titolo = titoloField.getText();
                    List<String> autori = Arrays.asList(autoriField.getText().split("\\s*,\\s*"));
                    int anno = Integer.parseInt(annoField.getText());
                    int copie = Integer.parseInt(copieField.getText());
                    return new Libro(isbn, titolo, autori, anno, copie);    //restituisco il nuovo libro
                } catch (Exception ex) {
                    mostraErrore("Dati non validi: " + ex.getMessage());    //eventuali errodi (di parsing ecc.)
                    return null;
                }
            }
            return null;    //se il bibliotecario non ha premuto ok restituisco null
        });

        return finestraDialog;  //restituisco la finestra di dialog
    }

    /**
     * @brief Mostra un alert di errore standardizzato.
     * @details Implementa il Requisito Non Funzionale 4.1.2 (Feedback errore).
     *
     * @param[in] messaggio Il testo dell'errore da mostrare al bibliotecario.
     */
    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR, messaggio, ButtonType.OK);   //creo un alert per gli errori con pulsante ok
        alert.setHeaderText("Errore");  //intestazione
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/stile_dialog_alert.css").toExternalForm()); //collego il relativo css
        alert.showAndWait();    //mostro l'alert
    }
}
