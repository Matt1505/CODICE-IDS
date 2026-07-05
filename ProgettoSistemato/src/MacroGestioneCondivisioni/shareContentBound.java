package src.MacroGestioneCondivisioni;

import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.GeneralClasses.Entities.ContenutoEntity;
import src.GeneralClasses.Entities.StudenteEntity;
import src.MacroGestioneContenuti.HomePageBoundary;
import src.MacroGestioneContenuti.HomePageControl;

public class shareContentBound extends Application {

    private List<ContenutoEntity> contenutiSelezionati;
    private SendFileControl sc;
    private HomePageControl hc; // caso d'uso per ritorno alla homepage
    // Nodi grafici accessibili per il Controller (rimossi oggetto e messaggio)
    private VBox rootContainer;
    private TextField txtDestinatari;
    private Button btnConferma;
    private Button btnAnnulla;
    private VBox allegatiContainer;
    private String email;
    
    public shareContentBound(List<ContenutoEntity> a,String e,SendFileControl sc) {
        this.contenutiSelezionati = a;
        this.sc=sc;
        this.email=e;
        this.hc = new HomePageControl(e);
    }

    // design simile a invio di una email (versione compatta di riepilogo)
    public void visualizza(Object windowContext ) {
        // Inizializza la struttura grafica aggiornata
        
        costruisciInterfaccia();
        
        // Gestione del contesto dello Stage per mostrare la schermata
        if (windowContext instanceof Stage) {
            Stage stageCorrente = (Stage) windowContext;
            Scene scene = new Scene(this.rootContainer, 750, 500); // Altezza ridotta dato che ci sono meno campi
            stageCorrente.setScene(scene);
            stageCorrente.setTitle("Riepilogo Condivisione - Canale AFAM");
            stageCorrente.show();
        }
    }
    
    @Override
    public void start(Stage stage) {
        visualizza(stage);
    }

    /**
     * Configura la struttura pulita senza i campi Oggetto e Messaggio
     */
    private void costruisciInterfaccia() {
        this.rootContainer = new VBox(20);
        this.rootContainer.setPadding(new Insets(30));
        this.rootContainer.setStyle("-fx-background-color: #F4F7FB;"); // Sfondo chiaro della piattaforma

        // --- SCHEDA BIANCA DI RIEPILOGO ---
        VBox riepilogoForm = new VBox(20);
        riepilogoForm.setPadding(new Insets(30));
        riepilogoForm.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(9,27,51,0.06), 15, 0, 0, 5);");
        VBox.setVgrow(riepilogoForm, Priority.ALWAYS);

        // --- TITOLO DELLA PAGINA ---
        Label lblTitolo = new Label("Riepilogo Condivisione");
        lblTitolo.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-padding: 0 0 5 0;");
        riepilogoForm.getChildren().add(lblTitolo);

        // --- CAMPO: DESTINATARI (A:) ---
        HBox rowDestinatari = new HBox(15);
        rowDestinatari.setAlignment(Pos.CENTER_LEFT);
        Label lblTo = new Label("A:");
        lblTo.setPrefWidth(40);
        lblTo.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #556E8A; -fx-font-size: 14px;");
        
        this.txtDestinatari = new TextField();
        this.txtDestinatari.setPromptText("Inserisci una o più email dei destinatari (separate da virgola)...");
        this.txtDestinatari.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #C4D1DF transparent; -fx-border-width: 1; -fx-padding: 6 0; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        HBox.setHgrow(this.txtDestinatari, Priority.ALWAYS);
        
        // Effetto focus sulla linea inferiore del campo destinatario
        this.txtDestinatari.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) this.txtDestinatari.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #12305C transparent; -fx-border-width: 1.5; -fx-padding: 5 0; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
            else this.txtDestinatari.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #C4D1DF transparent; -fx-border-width: 1; -fx-padding: 6 0; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        });
        rowDestinatari.getChildren().addAll(lblTo, this.txtDestinatari);

        // --- AREA ELENCO RISORSE SELEZIONATE ---
        VBox allegatiWrapper = new VBox(8);
        VBox.setVgrow(allegatiWrapper, Priority.ALWAYS);
        
        Label lblAllegati = new Label("Contenuti selezionati da inviare:");
        lblAllegati.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-font-size: 13px;");
        
        this.allegatiContainer = new VBox(8);
        this.allegatiContainer.setPadding(new Insets(12));
        this.allegatiContainer.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8;");
        
        // Popola la lista mostrando solo i titoli dei contenuti
        popolaSezioneRisorse();

        ScrollPane scrollAllegati = new ScrollPane(this.allegatiContainer);
        scrollAllegati.setFitToWidth(true);
        scrollAllegati.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        VBox.setVgrow(scrollAllegati, Priority.ALWAYS);
        
        allegatiWrapper.getChildren().addAll(lblAllegati, scrollAllegati);

        // Composizione del form minimale (solo Destinatari e Allegati)
        riepilogoForm.getChildren().addAll(rowDestinatari, allegatiWrapper);

        // --- CONTENITORE PULSANTI (Bottom Bar) ---
        HBox actionsBar = new HBox(15);
        actionsBar.setAlignment(Pos.CENTER_RIGHT);

        this.btnAnnulla = new Button("Annulla");
        this.btnAnnulla.setStyle("-fx-background-color: transparent; -fx-border-color: #C4D1DF; -fx-border-radius: 20; -fx-padding: 10 22; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #556E8A; -fx-cursor: hand; -fx-font-size: 14px;");
        this.btnAnnulla.setOnMouseEntered(e -> this.btnAnnulla.setStyle("-fx-background-color: #F4F7FB; -fx-border-color: #556E8A; -fx-border-radius: 20; -fx-padding: 10 22; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-cursor: hand; -fx-font-size: 14px;"));
        this.btnAnnulla.setOnMouseExited(e -> this.btnAnnulla.setStyle("-fx-background-color: transparent; -fx-border-color: #C4D1DF; -fx-border-radius: 20; -fx-padding: 10 22; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #556E8A; -fx-font-size: 14px;"));
        this.btnAnnulla.setOnAction(e-> this.annullaCondivisione());
        // Bottone principale rinominato in "Conferma"
        this.btnConferma = new Button("Conferma");
        this.btnConferma.setStyle("-fx-background-color: linear-gradient(to right, #1A4073, #12305C); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 28; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 14px; -fx-effect: dropshadow(three-pass-box, rgba(18,48,92,0.3), 8, 0, 0, 3);");
        this.btnConferma.setOnMouseEntered(e -> this.btnConferma.setStyle("-fx-background-color: linear-gradient(to right, #235391, #1A4073); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 28; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 14px; -fx-effect: dropshadow(three-pass-box, rgba(18,48,92,0.45), 10, 0, 0, 4);"));
        this.btnConferma.setOnMouseExited(e -> this.btnConferma.setStyle("-fx-background-color: linear-gradient(to right, #1A4073, #12305C); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 28; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 14px; -fx-effect: dropshadow(three-pass-box, rgba(18,48,92,0.3), 8, 0, 0, 3);"));
        this.btnConferma.setOnAction(e->this.inviaDestinatari());

        actionsBar.getChildren().addAll(this.btnAnnulla, this.btnConferma);
        this.rootContainer.getChildren().addAll(riepilogoForm, actionsBar);
    }

    /**
     * Elenca visivamente i titoli dei file selezionati
     */
    private void popolaSezioneRisorse() {

        this.allegatiContainer.getChildren().clear();
        if (this.contenutiSelezionati == null || this.contenutiSelezionati.isEmpty()) {
            Label lblVuoto = new Label("Nessun contenuto in lista.");
            lblVuoto.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-style: italic; -fx-text-fill: #8FA9C7;");
            this.allegatiContainer.getChildren().add(lblVuoto);
            return;
        }

        for (ContenutoEntity contenuto : this.contenutiSelezionati) {
            HBox rigaRisorsa = new HBox(10);
            rigaRisorsa.setAlignment(Pos.CENTER_LEFT);
            rigaRisorsa.setPadding(new Insets(6, 12, 6, 12));
            rigaRisorsa.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6;");

            Label iconaAllegato = new Label("📎");
            iconaAllegato.setStyle("-fx-text-fill: #12305C; -fx-font-size: 14px;");

            // Mostra il titolo del contenuto
            Label lblNome = new Label(contenuto.getTitolo());
            lblNome.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #2C4A73; -fx-font-weight: bold;");

            Label lblTipo = new Label("(" + contenuto.getTipo().toUpperCase() + ")");
            lblTipo.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11px; -fx-text-fill: #8FA9C7;");

            rigaRisorsa.getChildren().addAll(iconaAllegato, lblNome, lblTipo);
            this.allegatiContainer.getChildren().add(rigaRisorsa);
        }

        
    }

    public void annullaCondivisione(){
        sc.sendHome(this.email,(Stage)this.rootContainer.getScene().getWindow());
    }

    public void inviaDestinatari(){
        sc.setDestinatari(this.txtDestinatari.getText());
        sc.mandaSelezioneContenuti((Stage)this.rootContainer.getScene().getWindow());
    }

    
    
    public Button getBtnAnnulla() { return this.btnAnnulla; }
    public List<ContenutoEntity> getContenutiSelezionati() { return this.contenutiSelezionati; }
    

}