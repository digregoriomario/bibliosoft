/**
 * @file ControllerLibri.java
 * @author gruppo5
 * @version 1.0
 */
package gruppo5.bibliosoft.controller;

import gruppo5.bibliosoft.archivi.filtri.FiltroLibro;
import gruppo5.bibliosoft.modelli.Libro;
import gruppo5.bibliosoft.servizi.ServizioLibri;
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

    private final ObservableList<Libro> dati = FXCollections.observableArrayList();   //lista osservabile per poter popolare la tabella

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
        aggiorna(); //agiorno tutto (dati ecc.)
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
    private void onAggiungi() {
        Dialog<Libro> dialog = creaDialogLibro(null);   //creo un nuovo dialog
        dialog.setTitle("Nuovo libro"); //titolo del dialog
        dialog.showAndWait().ifPresent(libro -> {   //aspetto che venga premuto OK per prendere id ati
            try {
                servizioLibri.aggiungiLibro(libro); //chiedo al servizio libri di aggiungere il libro
                ControllerPrincipale.modificheEffettuate = true;
                aggiorna(); //aggiorno tutto
            } catch (Exception ex) {
                mostraErrore(ex.getMessage());
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
    private void onModifica() {
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem(); //prendo il libro che il bibliotecario ha selezionato dalla tabella
        if (selezionato == null) {  //se non ha selezionato nulla stampo un messaggio
            mostraErrore("Seleziona un libro dalla tabella.");
            return;
        }
        
        Dialog<Libro> finestraDialog = creaDialogLibro(selezionato);    //creo un dialog passando il libro selezionato (così le text field saranno già popolate)
        finestraDialog.setTitle("Modifica libro");  //titolo del dialog
        finestraDialog.showAndWait().ifPresent(libro -> {   //aspetto che l'utente prema OK e prelevo i dati
            try {
                /*selezionato.setTitolo(libro.getTitolo());   //setto la modifica al titolo
                selezionato.setAutori(libro.getAutori());   //setto la modifica agli autori
                selezionato.setAnnoPubblicazione(libro.getAnnoPubblicazione()); //setto la modifica all'anno di pubblicazione*/
                
                if(libro.getCopieTotali() < selezionato.getCopieInPrestito())   //quest controllo è effettuato anche dal livello di servizio ma lo ripeto qui affinchè in caso di errore il dialog per modificare non venga mostrato affatto
                    throw new IllegalStateException("Devi inserire un numero di copie totali > " + selezionato.getCopieInPrestito() + " (copie disponibili)");
                
                /*selezionato.setCopieDisponibili(selezionato.getCopieDisponibili() + libro.getCopieTotali() - selezionato.getCopieTotali());
                selezionato.setCopieTotali(libro.getCopieTotali());*/
                
                servizioLibri.modificaLibro(libro);
                ControllerPrincipale.modificheEffettuate = true;
                aggiorna();
            } catch (Exception ex) {
                mostraErrore(ex.getMessage());
            }
        });
    }

    /**
     * @brief Gestisce l'eliminazione di un libro.
     * @details Implementa il Caso d'Uso 4 (Cancellazione libro). Verifica
     * preventivamente se il libro ha prestiti attivi (violazione vincoli).
     * Chiede conferma all'utente prima di procedere.
     *
     * @pre Deve essere selezionato un libro.
     * @pre Il libro non deve avere prestiti attivi.
     * @post Se l'operazione ha successo, il libro è rimosso e la tabella
     * aggiornata.
     * @post Se si verifica un errore (es. prestiti attivi), viene mostrato un
     * Alert all'utente.
     */
    private void onElimina() {
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Seleziona un libro da eliminare.");
            return;
        }
        if (selezionato.haPrestitiAttivi()) {    //aggiunto prima (anche se presente nel servizioLibri) per mostrare prima l'errore
            mostraErrore("Impossibile eliminare: libro in prestito o senza copie disponibili");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Vuoi davvero eliminare il libro selezionato?",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Conferma eliminazione");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/gruppo5/bibliosoft/css/stile_viste.css").toExternalForm()
        );
        alert.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                try {
                    servizioLibri.eliminaLibro(selezionato);
                    ControllerPrincipale.modificheEffettuate = true;
                    aggiorna();
                } catch (Exception ex) {
                    mostraErrore(ex.getMessage());
                }
            }
        });
    }

    /**
     * @brief Aggiorna la vista recuperando la lista completa dei libri.
     * @details Invocato dopo ogni modifica o cambio tab.
     */
    public void aggiorna() {
        dati.setAll(servizioLibri.listaLibri());
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
        Dialog<Libro> finestraDialog = new Dialog<>();
        finestraDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        finestraDialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/gruppo5/bibliosoft/css/stile_viste.css").toExternalForm()
        );

        TextField isbnField = new TextField();
        TextField titoloField = new TextField();
        TextField autoriField = new TextField();
        TextField annoField = new TextField();
        TextField copieField = new TextField();

        if (iniziale != null) {
            isbnField.setText(iniziale.getIsbn());
            isbnField.setDisable(true);
            titoloField.setText(iniziale.getTitolo());
            autoriField.setText(String.join(", ", iniziale.getAutori()));
            annoField.setText(String.valueOf(iniziale.getAnnoPubblicazione()));
            copieField.setText(String.valueOf(iniziale.getCopieTotali()));
        }

        VBox contenitore = new VBox(20);
        contenitore.setPadding(new Insets(10, 20, 10, 20));

        // === RIGA 1: solo ISBN ===
        VBox rigaIsbn = new VBox(5);
        Label labelIsbn = new Label("ISBN:");
        labelIsbn.setMinWidth(150);
        rigaIsbn.getChildren().addAll(labelIsbn, isbnField);

        // === RIGA 2: Titolo + Autori affiancati ===
        VBox colTitolo = new VBox(5);
        Label labelTitolo = new Label("Titolo:");
        labelTitolo.setMinWidth(150);
        colTitolo.getChildren().addAll(labelTitolo, titoloField);

        VBox colAutori = new VBox(5);
        Label labelAutori = new Label("Autori (separati da ,):");
        labelAutori.setMinWidth(150);
        colAutori.getChildren().addAll(labelAutori, autoriField);

        HBox rigaTitoloAutori = new HBox(20);
        rigaTitoloAutori.getChildren().addAll(colTitolo, colAutori);

        // === RIGA 3: Anno + Copie totali affiancati ===
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

        // Aggiungo solo le 3 righe al contenitore principale
        contenitore.getChildren().addAll(
                rigaIsbn,
                rigaTitoloAutori,
                rigaAnnoCopie
        );

        finestraDialog.getDialogPane().setContent(contenitore);

        // === BINDING: disabilita OK finché non sono tutti pieni ===
        Node okButton = finestraDialog.getDialogPane().lookupButton(ButtonType.OK);

        okButton.disableProperty().bind(
                isbnField.textProperty().isEmpty()
                        .or(titoloField.textProperty().isEmpty())
                        .or(autoriField.textProperty().isEmpty())
                        .or(annoField.textProperty().isEmpty())
                        .or(copieField.textProperty().isEmpty())
        );
        // =========================================

        finestraDialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    String isbn = isbnField.getText();
                    String titolo = titoloField.getText();
                    List<String> autori = Arrays.asList(autoriField.getText().split("\\s*,\\s*"));
                    int anno = Integer.parseInt(annoField.getText());
                    int copie = Integer.parseInt(copieField.getText());
                    return new Libro(isbn, titolo, autori, anno, copie);
                } catch (Exception ex) {
                    mostraErrore("Dati non validi: " + ex.getMessage());
                    return null;
                }
            }
            return null;
        });

        return finestraDialog;
    }

    /**
     * @brief Mostra un alert di errore standardizzato.
     * @details Implementa il Requisito Non Funzionale 4.1.2 (Feedback errore).
     *
     * @param[in] messaggio Il testo dell'errore da mostrare all'utente.
     */
    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR, messaggio, ButtonType.OK);
        alert.setHeaderText("Errore");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/gruppo5/bibliosoft/css/stile_viste.css").toExternalForm()
        );
        alert.showAndWait();
    }
}
