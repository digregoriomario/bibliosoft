/**
 * @file ControllerDashboard.java
 * @author gruppocinque
 * @version 1.0
 */
package gruppocinque.bibliosoft.controller;

import gruppocinque.bibliosoft.servizi.ServizioLibri;
import gruppocinque.bibliosoft.servizi.ServizioUtenti;
import gruppocinque.bibliosoft.servizi.ServizioPrestiti;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * @brief Controller per la gestione della Dashboard.
 * @details Implementa la logica del Caso d'Uso 1 (Visualizzazione Dashboard). 
 * Fornisce una visione d'insieme dello stato della biblioteca,
 * mostrando statistiche aggiornate su libri, utenti e prestiti.
 */
public class ControllerDashboard {
    //servizi per interagire con i dati:
    private ServizioPrestiti servizioPrestiti;
    private ServizioUtenti servizioUtenti;
    private ServizioLibri servizioLibri;
    
    //attributi FXML:
    @FXML
    private Label prestitiInCorso;
    @FXML
    private Label prestitiInRitardo;
    @FXML
    private Label prestitiConclusi;
    @FXML
    private Label utentiTotali;
    @FXML
    private Label utentiPrestitiAttivi;
    @FXML
    private Label libriTotali;       
    @FXML
    private Label copieTotali;
    @FXML
    private Label copieDisponibili;

    /**
     * @brief Inizializza i servizi necessari e popola la vista.
     * @details
     * @param[in] servizioPrestiti Servizio per il recupero dati sui prestiti.
     * @param[in] servizioUtenti Servizio per il recupero dati sugli utenti.
     * @param[in] servizioLibri Servizio per il recupero dati sui libri.
     *
     * @pre I servizi passati non devono essere null. 
     * @post Le etichette della GUI sono aggiornate con i valori correnti.
     */
    public void impostaServizi(ServizioPrestiti servizioPrestiti, ServizioUtenti servizioUtenti, ServizioLibri servizioLibri) {
        //imposto i servizi e aggiorno:
        this.servizioPrestiti = servizioPrestiti;
        this.servizioUtenti = servizioUtenti;
        this.servizioLibri = servizioLibri;
        aggiorna();
    }

    /**
     * @brief Aggiorna tutte le sezioni statistiche della dashboard.
     * @details Metodo che invoca l'aggiornamento specifico per prestiti, utenti
     * e libri. Viene chiamato all'inizializzazione e ogni volta che la tab
     * riceve il focus.
     */
    public void aggiorna() {
        //aggiorno tutte le tipologie di statistiche:
        aggiornaStatistichePrestiti();
        aggiornaStatisticheUtenti();
        aggiornaStatisticheLibri();
    }

    /**
     * @brief Aggiorna i contatori relativi ai prestiti.
     * @details Visualizza il numero di prestiti in ritardo, conclusi e in
     * corso. Utile per monitorare lo stato di salute dei rientri (RF 3.1.3 - Gestione Prestiti).
     */
    public void aggiornaStatistichePrestiti() {
        //riempo tutte le label con le relative statistiche:
        prestitiInRitardo.setText(Integer.toString(servizioPrestiti.getPrestitiInRitardo()));
        prestitiConclusi.setText(Integer.toString(servizioPrestiti.getPrestitiConclusi()));
        prestitiInCorso.setText(Integer.toString(servizioPrestiti.getPrestitiInCorso()));
    }

    /**
     * @brief Aggiorna i contatori relativi agli utenti.
     * @details Visualizza il totale degli utenti registrati e quanti di essi
     * hanno almeno un prestito attivo.
     */
    public void aggiornaStatisticheUtenti() {
        //riempo tutte le label con le relative statistiche:
        utentiTotali.setText(Integer.toString(servizioUtenti.getUtentiTotali()));
        utentiPrestitiAttivi.setText(Integer.toString(servizioUtenti.getUtentiAttivi()));
    }

    /**
     * @brief Aggiorna i contatori relativi al catalogo libri.
     * @details Mostra il numero di titoli unici, il totale delle copie fisiche
     * possedute e quante di queste sono attualmente disponibili per il
     * prestito.
     */
    public void aggiornaStatisticheLibri() {
        //riempo tutte le label con le relative statistiche:
        libriTotali.setText(Integer.toString(servizioLibri.getLibriTotali()));
        copieTotali.setText(Integer.toString(servizioLibri.getCopieTotali()));
        copieDisponibili.setText(Integer.toString(servizioLibri.getCopieDisponibili()));
    }
}