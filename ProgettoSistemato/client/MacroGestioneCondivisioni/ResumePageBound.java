package client.MacroGestioneCondivisioni;

import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import client.GeneralClasses.Entities.ContenutoEntity;

public class ResumePageBound extends Application {
    
    private List<ContenutoEntity> contenutiSelezionati;
    private VBox rootContainer;
    private TextField txtDestinatario;
    private TextField txtOggetto;
    private TextArea txtMessaggio;
    private Button btnInviaDefinitivo;
    private Button btnAnnulla;
    private VBox allegatiContainer;

    // Costruttore che riceve già i file selezionati dalla HomePageBoundary
    public ResumePageBound(List<ContenutoEntity> contenutiSelezionati) {
        this.contenutiSelezionati = contenutiSelezionati;
        initUI();
    }

    // Costruttore vuoto di default richiesto da JavaFX Application
    public ResumePageBound() {
        initUI();
    }

    private void initUI() {
        this.rootContainer = new VBox(20);
        this.rootContainer.setPadding(new Insets(30));
        this.rootContainer.setStyle("-fx-background-color: #F4F7FB;"); // Stesso sfondo luminoso della Home

        // --- SCHEDA/PANNELLO CENTRALE (Stile Foglio Email) ---
        VBox emailForm = new VBox(15);
        emailForm.setPadding(new Insets(30));
        emailForm.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(9,27,51,0.06), 15, 0, 0, 5);");
        VBox.setVgrow(emailForm, Priority.ALWAYS);

        // --- TITOLO FINESTRA ---
        Label lblTitolo = new Label("Condividi Contenuti");
        lblTitolo.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-padding: 0 0 10 0;");
        emailForm.getChildren().add(lblTitolo);

        // --- CAMPO: A (DESTINATARIO) ---
        HBox rowDestinatario = new HBox(15);
        rowDestinatario.setAlignment(Pos.CENTER_LEFT);
        Label lblTo = new Label("A:");
        lblTo.setPrefWidth(60);
        lblTo.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #556E8A; -fx-font-size: 14px;");
        
        this.txtDestinatario = new TextField();
        this.txtDestinatario.setPromptText("inserisci l'email del destinatario...");
        this.txtDestinatario.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #C4D1DF transparent; -fx-border-width: 1; -fx-padding: 6 0; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        HBox.setHgrow(this.txtDestinatario, Priority.ALWAYS);
        
        // Effetto focus sulla linea inferiore del campo
        this.txtDestinatario.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) this.txtDestinatario.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #12305C transparent; -fx-border-width: 1.5; -fx-padding: 5 0; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
            else this.txtDestinatario.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #C4D1DF transparent; -fx-border-width: 1; -fx-padding: 6 0; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        });
        rowDestinatario.getChildren().addAll(lblTo, this.txtDestinatario);

        // --- CAMPO: OGGETTO ---
        HBox rowOggetto = new HBox(15);
        rowOggetto.setAlignment(Pos.CENTER_LEFT);
        Label lblSubject = new Label("Oggetto:");
        lblSubject.setPrefWidth(60);
        lblSubject.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #556E8A; -fx-font-size: 14px;");
        
        this.txtOggetto = new TextField();
        this.txtOggetto.setPromptText("Aggiungi un oggetto...");
        this.txtOggetto.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #C4D1DF transparent; -fx-border-width: 1; -fx-padding: 6 0; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        HBox.setHgrow(this.txtOggetto, Priority.ALWAYS);
        
        this.txtOggetto.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) this.txtOggetto.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #12305C transparent; -fx-border-width: 1.5; -fx-padding: 5 0; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
            else this.txtOggetto.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #C4D1DF transparent; -fx-border-width: 1; -fx-padding: 6 0; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        });
        rowOggetto.getChildren().addAll(lblSubject, this.txtOggetto);

        // --- CORPO DEL MESSAGGIO ---
        this.txtMessaggio = new TextArea();
        this.txtMessaggio.setPromptText("Scrivi qui il corpo del messaggio (opzionale)...");
        this.txtMessaggio.setPrefHeight(150);
        this.txtMessaggio.setWrapText(true);
        this.txtMessaggio.setStyle("-fx-background-color: transparent; -fx-viewport-background: transparent; -fx-border-color: #E8EFF5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        VBox.setVgrow(this.txtMessaggio, Priority.ALWAYS);

        // --- AREA FILE ALLEGATI (CONTENUTI INVIATI) ---
        VBox allegatiWrapper = new VBox(8);
        Label lblAllegati = new Label("Contenuti selezionati da inviare:");
        lblAllegati.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-font-size: 13px;");
        
        this.allegatiContainer = new VBox(8);
        this.allegatiContainer.setPadding(new Insets(10));
        this.allegatiContainer.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        // Popola dinamicamente i file allegati se la lista esiste ed è valorizzata
        popolaAllegati();

        ScrollPane scrollAllegati = new ScrollPane(this.allegatiContainer);
        scrollAllegati.setFitToWidth(true);
        scrollAllegati.setPrefHeight(100);
        scrollAllegati.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        
        allegatiWrapper.getChildren().addAll(lblAllegati, scrollAllegati);

        // Aggiunta dei componenti al form principale dell'email
        emailForm.getChildren().addAll(rowDestinatario, rowOggetto, this.txtMessaggio, allegatiWrapper);

        // --- CONTENITORE PULSANTI DI AZIONE (Bottom Bar) ---
        HBox actionsBar = new HBox(15);
        actionsBar.setAlignment(Pos.CENTER_RIGHT);

        this.btnAnnulla = new Button("Annulla");
        this.btnAnnulla.setStyle("-fx-background-color: transparent; -fx-border-color: #C4D1DF; -fx-border-radius: 20; -fx-padding: 10 22; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #556E8A; -fx-cursor: hand; -fx-font-size: 14px;");
        this.btnAnnulla.setOnMouseEntered(e -> this.btnAnnulla.setStyle("-fx-background-color: #F4F7FB; -fx-border-color: #556E8A; -fx-border-radius: 20; -fx-padding: 10 22; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-cursor: hand; -fx-font-size: 14px;"));
        this.btnAnnulla.setOnMouseExited(e -> this.btnAnnulla.setStyle("-fx-background-color: transparent; -fx-border-color: #C4D1DF; -fx-border-radius: 20; -fx-padding: 10 22; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #556E8A; -fx-font-size: 14px;"));

        // Bottone INVIA DEFINITIVO (Stile analogo al "CONDIVIDI CONTENUTI" primario della Home)
        this.btnInviaDefinitivo = new Button("Invia File");
        this.btnInviaDefinitivo.setStyle("-fx-background-color: linear-gradient(to right, #1A4073, #12305C); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 26; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 14px; -fx-effect: dropshadow(three-pass-box, rgba(18,48,92,0.3), 8, 0, 0, 3);");
        this.btnInviaDefinitivo.setOnMouseEntered(e -> this.btnInviaDefinitivo.setStyle("-fx-background-color: linear-gradient(to right, #235391, #1A4073); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 26; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 14px; -fx-effect: dropshadow(three-pass-box, rgba(18,48,92,0.45), 10, 0, 0, 4);"));
        this.btnInviaDefinitivo.setOnMouseExited(e -> this.btnInviaDefinitivo.setStyle("-fx-background-color: linear-gradient(to right, #1A4073, #12305C); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 26; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 14px; -fx-effect: dropshadow(three-pass-box, rgba(18,48,92,0.3), 8, 0, 0, 3);"));

        actionsBar.getChildren().addAll(this.btnAnnulla, this.btnInviaDefinitivo);
        this.rootContainer.getChildren().addAll(emailForm, actionsBar);
    }

    private void popolaAllegati() {
        this.allegatiContainer.getChildren().clear();
        if (this.contenutiSelezionati == null || this.contenutiSelezionati.isEmpty()) {
            Label lblVuoto = new Label("Nessun file selezionato.");
            lblVuoto.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-style: italic; -fx-text-fill: #8FA9C7;");
            this.allegatiContainer.getChildren().add(lblVuoto);
            return;
        }

        for (ContenutoEntity contenuto : this.contenutiSelezionati) {
            HBox allegatoRow = new HBox(10);
            allegatoRow.setAlignment(Pos.CENTER_LEFT);
            allegatoRow.setPadding(new Insets(6, 12, 6, 12));
            allegatoRow.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6;");

            // Icona segnaposto "File" (simulata con una graffetta o cerchietto estetico)
            Label iconaClip = new Label("📎");
            iconaClip.setStyle("-fx-text-fill: #12305C; -fx-font-size: 14px;");

            // Nome del file / Titolo del contenuto
            Label lblNomeFile = new Label(contenuto.getTitolo());
            lblNomeFile.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #2C4A73; -fx-font-weight: bold;");

            // Tipo di file in piccolo accanto
            Label lblTipo = new Label("(" + contenuto.getTipo().toUpperCase() + ")");
            lblTipo.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11px; -fx-text-fill: #8FA9C7;");

            allegatoRow.getChildren().addAll(iconaClip, lblNomeFile, lblTipo);
            this.allegatiContainer.getChildren().add(allegatoRow);
        }
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(this.rootContainer, 750, 600);
        stage.setScene(scene);
        stage.setTitle("Invia Contenuti - Canale AFAM");
        stage.show();
    }

    // Metodo per aggiornare la lista se passata in un secondo momento
    public void setContenutiSelezionati(List<ContenutoEntity> contenutiSelezionati) {
        this.contenutiSelezionati = contenutiSelezionati;
        popolaAllegati();
    }

    // --- GETTERS PER IL CONTROLLER (Per agganciare le funzionalità di business) ---
    public String getDestinatario() { return this.txtDestinatario.getText(); }
    public String getOggetto() { return this.txtOggetto.getText(); }
    public String getMessaggio() { return this.txtMessaggio.getText(); }
    public Button getBtnInviaDefinitivo() { return this.btnInviaDefinitivo; }
    public Button getBtnAnnulla() { return this.btnAnnulla; }
}