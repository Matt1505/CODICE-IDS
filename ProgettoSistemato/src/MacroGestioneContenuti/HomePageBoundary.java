package src.MacroGestioneContenuti;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;



import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.control.*;
import javafx.scene.paint.Color; 
import javafx.scene.Scene;
import src.GeneralClasses.Entities.ContenutoEntity;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;

import src.GeneralClasses.Entities.StudenteEntity;
import src.MacroGestioneCondivisioni.SendFileControl;
import src.MacroGestioneProfilo.GestioneProfiloBound;

public class HomePageBoundary {
    private String email;
    private VBox rootContainer;
    private FlowPane resourcesContainer; 
    private HomePageControl hc;
    private Button btnCaricaFile;
    private Button btnCondividiContenuti;
    private Button btnRiordinaContenuti;
    private Button btnModificaProfilo;
    private Button btnInterrompi;
    private Button btnSalva;
    private Button btnInvia;
    private TextField txtCercaUtenti;
    private VBox risultatiRicercaUtentiContainer;
    private SendFileControl sendFileControl;
    private PublicContentBound publicContentBound;
    private Button btnVisualizzaCondivisioniPassate;
      
    public HomePageBoundary(String email) {
        this.email = email;
        this.hc = new HomePageControl(email, this);
        this.sendFileControl= new SendFileControl(this,email);
        this.rootContainer = new VBox(0); 
        this.rootContainer.setStyle("-fx-background-color: #F4F7FB;"); // Sfondo app leggermente più luminoso
        
        // --- TESTATA (NAVBAR MODERNA) ---
        VBox header = new VBox(20);
        header.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 25 35; -fx-effect: dropshadow(three-pass-box, rgba(9,27,51,0.06), 15, 0, 0, 5);");
        
        // Layout orizzontale superiore che spinge il blocco profilo a destra
        HBox topHeaderRow = new HBox();
        topHeaderRow.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("Piattaforma AFAM"); 
        titleLabel.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 1, 1);");
        
        // Regione di spaziatura flessibile per spingere il profilo a destra
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Contenitore verticale per il profilo (Immagine sopra, Nome e Cognome sotto)
        VBox profileVBox = new VBox(5);
        profileVBox.setAlignment(Pos.CENTER);

        // Contenitore orizzontale per immagine profilo + bottone di modifica
        HBox profileContainer = new HBox(10);
        profileContainer.setAlignment(Pos.CENTER_RIGHT);

        // Immagine di profilo arrotondata
        ImageView profileImageView = new ImageView();
        try {
            profileImageView.setImage(new Image("file:src/Assets/defaultProfilePicture.jpeg"));
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        profileImageView.setFitWidth(45);
        profileImageView.setFitHeight(45);
        // Modifica Estetica: preserve ratio disattivato affinché l'immagine riempia il quadrato prima del taglio circolare
        profileImageView.setPreserveRatio(false); 
        
        Circle clip = new Circle(22.5, 22.5, 22.5);
        profileImageView.setClip(clip);

        // Bottone estetico di modifica profilo (icona matita) adiacente all'immagine
        Button btnModificaInTestata = new Button("✏");
        btnModificaInTestata.setStyle("-fx-background-color: #F4F7FB; -fx-text-fill: #12305C; -fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 50; -fx-padding: 6 9; -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");
        btnModificaInTestata.setOnMouseEntered(e -> btnModificaInTestata.setStyle("-fx-background-color: #12305C; -fx-text-fill: white; -fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 50; -fx-padding: 6 9; -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 2);"));
        btnModificaInTestata.setOnMouseExited(e -> btnModificaInTestata.setStyle("-fx-background-color: #F4F7FB; -fx-text-fill: #12305C; -fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 50; -fx-padding: 6 9; -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);"));
        btnModificaInTestata.setOnAction(event -> modificaFoto());

        btnSalva = new Button("SALVA");
        btnSalva.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold; -fx-effect: dropshadow(two-pass-box, rgba(46,125,50,0.4), 5, 0, 0, 2);");
        btnSalva.setDisable(true);
        btnSalva.setVisible(false);
        btnSalva.setOnMouseEntered(e -> btnSalva.setStyle("-fx-background-color: #1B5E20; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold; -fx-effect: dropshadow(two-pass-box, rgba(27,94,32,0.5), 6, 0, 0, 2);"));
        btnSalva.setOnMouseExited(e -> btnSalva.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold; -fx-effect: dropshadow(two-pass-box, rgba(46,125,50,0.4), 5, 0, 0, 2);"));
        btnSalva.setOnAction(event -> caricaNuovoOrdinamento());

        btnInterrompi = new Button("INTERROMPI CONDIVISIONE");
        btnInterrompi.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold; -fx-effect: dropshadow(two-pass-box, rgba(46,125,50,0.4), 5, 0, 0, 2);");
        btnInterrompi.setDisable(true);
        btnInterrompi.setVisible(false);
        btnInterrompi.setOnMouseEntered(e -> btnInterrompi.setStyle("-fx-background-color: #1B5E20; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold; -fx-effect: dropshadow(two-pass-box, rgba(27,94,32,0.5), 6, 0, 0, 2);"));
        btnInterrompi.setOnMouseExited(e -> btnInterrompi.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 20; -fx-padding: 6 15; -fx-font-weight: bold; -fx-effect: dropshadow(two-pass-box, rgba(46,125,50,0.4), 5, 0, 0, 2);"));
        btnInterrompi.setOnAction(event -> interrompiCondivisione());
        profileContainer.getChildren().addAll(btnModificaInTestata, profileImageView);
        
        Label lblNomeCognome = new Label("Nome Cognome");
        lblNomeCognome.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2C4A73;");
        
        profileVBox.getChildren().addAll(profileContainer, lblNomeCognome);
        topHeaderRow.getChildren().addAll(titleLabel, spacer, profileVBox);

        HBox topBar = new HBox(12);
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        // Modifica Estetica: Stile base più morbido per i bottoni secondari (Pill design)
        String navBtnStyle = "-fx-background-color: transparent; -fx-border-color: #C4D1DF; -fx-border-radius: 20; -fx-padding: 8 16; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-cursor: hand; -fx-font-size: 13px;";
        String navBtnHover = "-fx-background-color: #F4F7FB; -fx-border-color: #12305C; -fx-border-radius: 20; -fx-padding: 8 16; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-cursor: hand; -fx-font-size: 13px;";

        this.btnCaricaFile = new Button("NUOVO CARICAMENTO");
        this.btnCaricaFile.setStyle(navBtnStyle);
        this.btnCaricaFile.setOnMouseEntered(e -> this.btnCaricaFile.setStyle(navBtnHover));
        this.btnCaricaFile.setOnMouseExited(e -> this.btnCaricaFile.setStyle(navBtnStyle));
        this.btnCaricaFile.setOnAction(event -> attivaCaricamentoFile());

        // Modifica Estetica: Bottone Invia in stile "Success" con ombra
       
        
        // Modifica Estetica: Bottone Condividi Contenuti stile primario (Blu gradient, pill shape, ombra morbida)
        this.btnCondividiContenuti = new Button("CONDIVIDI CONTENUTI");
        this.btnCondividiContenuti.setStyle("-fx-background-color: linear-gradient(to right, #1A4073, #12305C); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 13px; -fx-effect: dropshadow(three-pass-box, rgba(18,48,92,0.35), 8, 0, 0, 3);");
        this.btnCondividiContenuti.setOnMouseEntered(e -> this.btnCondividiContenuti.setStyle("-fx-background-color: linear-gradient(to right, #235391, #1A4073); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 13px; -fx-effect: dropshadow(three-pass-box, rgba(18,48,92,0.5), 10, 0, 0, 4);"));
        this.btnCondividiContenuti.setOnMouseExited(e -> this.btnCondividiContenuti.setStyle("-fx-background-color: linear-gradient(to right, #1A4073, #12305C); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 13px; -fx-effect: dropshadow(three-pass-box, rgba(18,48,92,0.35), 8, 0, 0, 3);"));
        this.btnCondividiContenuti.setOnAction(event -> condividiContenutiOn());

        // Modifica Estetica: Bottone Riordina
        this.btnRiordinaContenuti = new Button("RIORDINA CONTENUTI");
        this.btnRiordinaContenuti.setStyle("-fx-background-color: transparent; -fx-border-color: #12305C; -fx-border-radius: 20; -fx-padding: 8 16; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-cursor: hand; -fx-font-size: 13px;");
        this.btnRiordinaContenuti.setOnMouseEntered(e -> this.btnRiordinaContenuti.setStyle("-fx-background-color: #F4F7FB; -fx-border-color: #12305C; -fx-border-radius: 20; -fx-padding: 8 16; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-cursor: hand;"));
        this.btnRiordinaContenuti.setOnMouseExited(e -> this.btnRiordinaContenuti.setStyle("-fx-background-color: transparent; -fx-border-color: #12305C; -fx-border-radius: 20; -fx-padding: 8 16; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-font-size: 13px;"));
        this.btnRiordinaContenuti.setOnAction(event -> attivaRiordinamentoContenuti());

        
        
        this.btnInvia = new Button("INVIA");
        this.btnInvia.setStyle("-fx-background-color: linear-gradient(to right, #28A745, #218838); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 13px; -fx-effect: dropshadow(three-pass-box, rgba(40,167,69,0.35), 8, 0, 0, 3);");
        this.btnInvia.setOnMouseEntered(e -> this.btnInvia.setStyle("-fx-background-color: linear-gradient(to right, #34CE57, #28A745); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 13px; -fx-effect: dropshadow(three-pass-box, rgba(40,167,69,0.5), 10, 0, 0, 4);"));
        this.btnInvia.setOnMouseExited(e -> this.btnInvia.setStyle("-fx-background-color: linear-gradient(to right, #28A745, #218838); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-font-size: 13px; -fx-effect: dropshadow(three-pass-box, rgba(40,167,69,0.35), 8, 0, 0, 3);"));
        this.btnInvia.setOnAction(event -> inviaSelezione());
        this.btnInvia.setVisible(false);
        this.btnInvia.setDisable(true);
        this.btnInvia.setManaged(true);
        // Modifica Estetica: Barra di ricerca in stile pillola
        this.txtCercaUtenti = new TextField();
        this.txtCercaUtenti.setPromptText(" Cerca altri utenti...");
        this.txtCercaUtenti.setPrefWidth(200);
        this.txtCercaUtenti.setStyle("-fx-background-color: #F4F7FB; -fx-border-color: #C4D1DF; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8 12; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px;");

        this.risultatiRicercaUtentiContainer = new VBox(8);
        this.risultatiRicercaUtentiContainer.setPadding(new Insets(10, 35, 10, 35));
        this.risultatiRicercaUtentiContainer.setStyle("-fx-background-color: transparent;");

        this.txtCercaUtenti.setOnAction(event -> ricercaUtente(this.txtCercaUtenti.getText()));

        this.txtCercaUtenti.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                this.pulisciRisultatiRicercaUtenti();
            }
        });
        
        this.btnModificaProfilo = new Button("MODIFICA PROFILO");
        this.btnModificaProfilo.setStyle(navBtnStyle);
        this.btnModificaProfilo.setOnMouseEntered(e -> this.btnModificaProfilo.setStyle(navBtnHover));
        this.btnModificaProfilo.setOnMouseExited(e -> this.btnModificaProfilo.setStyle(navBtnStyle));
        this.btnModificaProfilo.setOnAction(event -> modificaFoto());
        
        topBar.getChildren().addAll(this.btnCaricaFile, this.btnCondividiContenuti, this.btnInvia,this.btnInterrompi, this.btnRiordinaContenuti, this.btnModificaProfilo, this.btnSalva, this.txtCercaUtenti);
        header.getChildren().addAll(topHeaderRow, topBar);

        // --- CONTENITORE RISORSE ---
        this.resourcesContainer = new FlowPane();
        this.resourcesContainer.setHgap(35);
        this.resourcesContainer.setVgap(35);
        this.resourcesContainer.setPadding(new Insets(20, 35, 40, 35));
        this.resourcesContainer.setAlignment(Pos.TOP_LEFT);
        this.resourcesContainer.setStyle("-fx-background-color: transparent;");
        
        // VBox interno allo ScrollPane che impila la sezione Descrizione sopra la Galleria di Contenuti
        VBox scrollContentWrapper = new VBox(0);
        scrollContentWrapper.setStyle("-fx-background-color: transparent;");
        scrollContentWrapper.getChildren().addAll(this.risultatiRicercaUtentiContainer, this.resourcesContainer);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(scrollContentWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.btnVisualizzaCondivisioniPassate = new Button("VISUALIZZA CONDIVISIONI PASSATE");
        this.btnVisualizzaCondivisioniPassate.setStyle(navBtnStyle);
        this.btnVisualizzaCondivisioniPassate.setOnMouseEntered(e -> this.btnVisualizzaCondivisioniPassate.setStyle(navBtnHover));
        this.btnVisualizzaCondivisioniPassate.setOnMouseExited(e -> this.btnVisualizzaCondivisioniPassate.setStyle(navBtnStyle));
        this.btnVisualizzaCondivisioniPassate.setOnAction(event -> caricaPaginaCondivisioniPassate());
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        this.rootContainer.getChildren().addAll(header, scrollPane);
        topBar.getChildren().add(this.btnVisualizzaCondivisioniPassate);
    }   

    public void visualizza() { 
        this.rootContainer.setVisible(true);
     }

    public void attivaCaricamentoFile() {
        if (this.hc != null) { this.hc.visualizza(); }
    }

    public void mostraPannelloCaricamentoFile(CaricamentoFileBound cFileBound){
        Stage currenteStage = (Stage) this.rootContainer.getScene().getWindow();
        VBox layout = cFileBound.visualizza();
        Scene reindirizzamentoScene = new Scene(layout, 400, 300);
        currenteStage.setScene(reindirizzamentoScene);
    }

  public void visualizzaContesto(Object windowContext){
        if(windowContext instanceof Stage){
            Stage stage = (Stage) windowContext;
            javafx.application.Platform.runLater(() -> {
                // 1. Se il rootContainer è ancora agganciato a una vecchia scena, lo liberiamo
                if (this.rootContainer.getScene() != null) {
                    this.rootContainer.getScene().setRoot(new javafx.scene.layout.Pane());
                }

                // 2. Controlliamo se lo stage ha già una scena attiva da poter riutilizzare
                Scene currentScene = stage.getScene();
                if (currentScene != null) {
                    currentScene.setRoot(this.rootContainer);
                } else {
                    // Fallback: creiamo la scena solo se lo stage ne è privo
                    Scene homeScene = new Scene(this.rootContainer, 900, 700);
                    stage.setScene(homeScene);
                }
                
                stage.setTitle("Piattaforma AFAM - HOME");
                stage.show();
            });
        }
    }

    public void mostraPannelloGestioneProfilo(GestioneProfiloBound gestioneProfiloBound){
        Stage currenteStage = (Stage) this.rootContainer.getScene().getWindow();
        VBox layout = gestioneProfiloBound.visualizza();
        Scene reindirizzamentoScene = new Scene(layout, 400, 300);
        currenteStage.setScene(reindirizzamentoScene);
    }

    public void mostraPannelloCaricamentoFoto(GestioneProfiloBound gestioneProfiloBound){
        this.mostraPannelloGestioneProfilo(gestioneProfiloBound);
    }

    public int getNumberOfCards(){
        return this.resourcesContainer.getChildren().size();
    }

    public void attivaRiordinamentoContenuti() {
        this.hc.abilitaRiodinamentoContenuti();
    }

    public void ricercaUtente(String query) {
        this.hc.cercaUtente(query);
    }

    public void cercaUtenti(String query) {
        this.ricercaUtente(query);
    }

    public void sendToGestioneProfilo() {
        this.hc.createGestioneProfiloBound();
    }

    public void modificaFoto() {
        this.hc.createGestioneProfiloBound();
    }

    public void mostraRisorsa() { 
        VBox card = new VBox(0);
        card.setPrefWidth(320); 
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(9,27,51,0.08), 12, 0, 0, 6); -fx-border-color: #E8EFF5; -fx-border-width: 1; -fx-border-radius: 12;");
        this.resourcesContainer.getChildren().add(card);
    }

    public void setUserInfo(StudenteEntity studente) {
    if (studente == null) return;

    try {
        VBox header = (VBox) this.rootContainer.getChildren().get(0);
        HBox topHeaderRow = (HBox) header.getChildren().get(0);
        VBox profileVBox = (VBox) topHeaderRow.getChildren().get(2);
        HBox profileContainer = (HBox) profileVBox.getChildren().get(0);
        Label lblNomeCognome = (Label) profileVBox.getChildren().get(1);
        ImageView profileImageView = (ImageView) profileContainer.getChildren().get(1);

        lblNomeCognome.setText(studente.getNome() + " " + studente.getCognome());

        if (studente.getFotoProfilo() != null && studente.getFotoProfilo().length > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(studente.getFotoProfilo());
            profileImageView.setImage(new Image(bis));
        }

    } catch (Exception e) {
        System.err.println("Errore durante l'aggiornamento dei dati utente nell'interfaccia:");
        e.printStackTrace();
    }
}

    public void caricaDatiRisorsa(ContenutoEntity risorsa) {
        if (this.resourcesContainer.getChildren().isEmpty()) return;
        VBox card = (VBox) this.resourcesContainer.getChildren().get(this.resourcesContainer.getChildren().size() - 1);
        card.setUserData(risorsa);

        StackPane previewArea = new StackPane();
        previewArea.setPrefHeight(200);
        previewArea.setCursor(Cursor.HAND);
        previewArea.setStyle("-fx-background-color: #0A1C3A; -fx-background-radius: 12 12 0 0;"); 

        String mimeType = risorsa.getTipo().toLowerCase();
        if (mimeType.startsWith("image/")) {
            ByteArrayInputStream bis = new ByteArrayInputStream(risorsa.getFile());
            ImageView imgView = new ImageView(new Image(bis));
            imgView.setFitWidth(318);
            imgView.setFitHeight(200);
            imgView.setPreserveRatio(false); 
            
            // Modifica estetica: clip arrotondato sulla preview per seguire la card
            Rectangle imgClip = new Rectangle(318, 200);
            imgClip.setArcWidth(24);
            imgClip.setArcHeight(24);
            imgView.setClip(imgClip);
            
            previewArea.getChildren().add(imgView);
        } else if (mimeType.startsWith("video/") || mimeType.startsWith("audio/")) {
            Rectangle bg = new Rectangle(318, 200, Color.web("#0A1C3A"));
            bg.setArcWidth(24);
            bg.setArcHeight(24);
            Label icon = new Label("▶");
            icon.setStyle("-fx-font-size: 50px; -fx-text-fill: #8FA9C7;");
            previewArea.getChildren().addAll(bg, icon);
        } else if (mimeType.equals("application/pdf")) {
            Rectangle bg = new Rectangle(318, 200, Color.web("#F4F7FB"));
            bg.setArcWidth(24);
            bg.setArcHeight(24);
            Label icon = new Label("PDF DOCUMENT");
            icon.setStyle("-fx-font-family: 'Georgia'; -fx-font-weight: bold; -fx-text-fill: #12305C;");
            previewArea.getChildren().addAll(bg, icon);
        } else {
            Label icon = new Label("FILE");
            icon.setStyle("-fx-text-fill: white;");
            previewArea.getChildren().add(icon);
        }

        previewArea.setOnMouseClicked(e -> this.apriDocumento(risorsa));
        previewArea.setOnMouseEntered(e -> previewArea.setOpacity(0.85));
        previewArea.setOnMouseExited(e -> previewArea.setOpacity(1.0));

        CheckBox chkSeleziona = new CheckBox();
        chkSeleziona.setStyle("-fx-cursor: hand; -fx-scale-x: 1.3; -fx-scale-y: 1.3; -fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 6; -fx-padding: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 6, 0, 0, 2);");
        StackPane.setAlignment(chkSeleziona, Pos.TOP_RIGHT);
        StackPane.setMargin(chkSeleziona, new Insets(15));
        
        chkSeleziona.setOnMouseClicked(e -> e.consume());
        previewArea.getChildren().add(chkSeleziona);
        chkSeleziona.setDisable(true);
        chkSeleziona.setVisible(false);
        chkSeleziona.setManaged(false);
        
        VBox infoArea = new VBox(10);
        infoArea.setPadding(new Insets(20));
        
        Label lblTitolo = new Label(risorsa.getTitolo());
        lblTitolo.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #12305C;");
        
        Label lblDescrizione = new Label(risorsa.getDescrizione());
        lblDescrizione.setWrapText(true);
        lblDescrizione.setMinHeight(Region.USE_COMPUTED_SIZE);
        lblDescrizione.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #556E8A; -fx-line-spacing: 3px;");

        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPadding(new Insets(10, 0, 0, 0));
        
        Button btnFrecciaSinistra = new Button("←");
        btnFrecciaSinistra.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-text-fill: #12305C; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 5 15;");
        btnFrecciaSinistra.setDisable(true);
        btnFrecciaSinistra.setVisible(false); 
        btnFrecciaSinistra.setManaged(false); 
        btnFrecciaSinistra.setOnAction(e -> this.invertiOrdineRisorse(1,(ContenutoEntity) card.getUserData()));
        Button btnFrecciaDestra = new Button("→");
        btnFrecciaDestra.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-text-fill: #12305C; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 5 15;");
        btnFrecciaDestra.setDisable(true);
        btnFrecciaDestra.setVisible(false); 
        btnFrecciaDestra.setManaged(false); 
        btnFrecciaDestra.setOnAction(e -> this.invertiOrdineRisorse(2, (ContenutoEntity) card.getUserData()));
        
        actionBox.getChildren().addAll(btnFrecciaSinistra, btnFrecciaDestra);
        
        infoArea.getChildren().addAll(lblTitolo, lblDescrizione, actionBox);
        card.getChildren().addAll(previewArea, infoArea);
    }

    public void apriDocumento(ContenutoEntity risorsa){
        this.hc.apriDocumento(risorsa,(Stage)rootContainer.getScene().getWindow());
    }
    public void invertiOrdineRisorse(int pos,ContenutoEntity e){
            this.hc.invertiOrdineRisorse(pos, e);
    }
    public void associaBottoneModifica() {
        if (this.resourcesContainer.getChildren().isEmpty()) return;
        VBox card = (VBox) this.resourcesContainer.getChildren().get(this.resourcesContainer.getChildren().size() - 1);
        VBox infoArea = (VBox) card.getChildren().get(1);
        HBox actionBox = (HBox) infoArea.getChildren().get(2);
        
        Button btnModifica = new Button("MODIFICA");
        btnModifica.setStyle("-fx-background-color: transparent; -fx-text-fill: #12305C; -fx-font-size: 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        btnModifica.setOnMouseEntered(e -> btnModifica.setStyle("-fx-background-color: #F4F7FB; -fx-text-fill: #12305C; -fx-font-size: 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 6;"));
        btnModifica.setOnMouseExited(e -> btnModifica.setStyle("-fx-background-color: transparent; -fx-text-fill: #12305C; -fx-font-size: 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;"));
        actionBox.getChildren().add(btnModifica);
        btnModifica.setOnAction(e -> sendToModifica((ContenutoEntity) card.getUserData()));
    }

    public void associaBottoneRimozione() {
        if (this.resourcesContainer.getChildren().isEmpty()) return;
        VBox card = (VBox) this.resourcesContainer.getChildren().get(this.resourcesContainer.getChildren().size() - 1);
        VBox infoArea = (VBox) card.getChildren().get(1);
        HBox actionBox = (HBox) infoArea.getChildren().get(2);
        
        Button btnRimozione = new Button("RIMUOVI");
        btnRimozione.setStyle("-fx-background-color: transparent; -fx-text-fill: #DC3545; -fx-font-size: 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        btnRimozione.setOnMouseEntered(e -> btnRimozione.setStyle("-fx-background-color: #F8D7DA; -fx-text-fill: #DC3545; -fx-font-size: 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 6;"));
        btnRimozione.setOnMouseExited(e -> btnRimozione.setStyle("-fx-background-color: transparent; -fx-text-fill: #DC3545; -fx-font-size: 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;"));
        btnRimozione.setOnAction(e -> avviaEliminazioneContenuto((ContenutoEntity) card.getUserData())); 
        actionBox.getChildren().add(btnRimozione);
    }

    public void backgroundDisplay(String messaggio) {
        this.resourcesContainer.getChildren().clear();
        Label lbl = new Label(messaggio);
        lbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-text-fill: #8FA9C7; -fx-padding: 100;");
        this.resourcesContainer.getChildren().add(lbl);
    }

    public void setStatoFrecceCardCorrente(boolean abilitaSinistra, boolean abilitaDestra) {
        if (this.resourcesContainer.getChildren().isEmpty()) return;
        impostaStatoFrecceCardSpecifici(this.resourcesContainer.getChildren().size() - 1, abilitaSinistra, abilitaDestra);
    }

    public void impostaStatoFrecceCardSpecifici(int indiceCard, boolean abilitaSinistra, boolean abilitaDestra) {
        if (indiceCard < 0 || indiceCard >= this.resourcesContainer.getChildren().size()) return;
        VBox card = (VBox) this.resourcesContainer.getChildren().get(indiceCard);
        VBox infoArea = (VBox) card.getChildren().get(1);
        HBox actionBox = (HBox) infoArea.getChildren().get(2);
        
        Button btnFrecciaSinistra = (Button) actionBox.getChildren().get(0);
        Button btnFrecciaDestra = (Button) actionBox.getChildren().get(1);
        
        btnFrecciaSinistra.setVisible(true);
        btnFrecciaSinistra.setManaged(true);
        btnFrecciaDestra.setVisible(true);
        btnFrecciaDestra.setManaged(true);
        
        btnFrecciaSinistra.setDisable(!abilitaSinistra);
        btnFrecciaDestra.setDisable(!abilitaDestra);
    }

    public ContenutoEntity getCardByIndex(int index) {
        if (index < 0 || index >= this.resourcesContainer.getChildren().size()) return null;
        VBox cardBox = (VBox) this.resourcesContainer.getChildren().get(index);
        return (ContenutoEntity) cardBox.getUserData();
    }

    public FlowPane getResourcesContainer() {
        return this.resourcesContainer;
    }

    public void scambiaCardNelContenitore(int indiceA, int indiceB) {
        if (indiceA < 0 || indiceB < 0 || 
            indiceA >= this.resourcesContainer.getChildren().size() || 
            indiceB >= this.resourcesContainer.getChildren().size()) {
            return;
        }
        
        javafx.scene.Node nodeA = this.resourcesContainer.getChildren().get(indiceA);
        javafx.scene.Node nodeB = this.resourcesContainer.getChildren().get(indiceB);
        
        int maxIndex = Math.max(indiceA, indiceB);
        int minIndex = Math.min(indiceA, indiceB);
        
        this.resourcesContainer.getChildren().remove(maxIndex);
        this.resourcesContainer.getChildren().remove(minIndex);
        
        if (indiceA < indiceB) {
            this.resourcesContainer.getChildren().add(minIndex, nodeB);
            this.resourcesContainer.getChildren().add(maxIndex, nodeA);
        } else {
            this.resourcesContainer.getChildren().add(minIndex, nodeA);
            this.resourcesContainer.getChildren().add(maxIndex, nodeB);
        }
    }

    public void caricaNuovoOrdinamento() {
        ArrayList<ContenutoEntity> nuovoOrdinamento = new ArrayList<>();
        for (javafx.scene.Node node : this.resourcesContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox card = (VBox) node;
                ContenutoEntity contenuto = (ContenutoEntity) card.getUserData();
                if (contenuto != null) {
                    nuovoOrdinamento.add(contenuto);
                }
            }
        }
        this.hc.salvaNuovoOrdinamento(nuovoOrdinamento);
    }   

    public int getResourceIndex(ContenutoEntity risorsa) {
        for (int i = 0; i < this.resourcesContainer.getChildren().size(); i++) {
            VBox card = (VBox) this.resourcesContainer.getChildren().get(i);
            ContenutoEntity contenuto = (ContenutoEntity) card.getUserData();
            if (contenuto != null && contenuto.equals(risorsa)) {
                return i;
            }
        }
        return -1; 
    }

    public int getContenitoreCardSize(){
        return this.getResourcesContainer().getChildren().size();
    }

    public void EnableSaveButton(){
        this.btnSalva.setDisable(false);
        this.btnSalva.setVisible(true);
    }

    public void DisableSaveButton(){
        this.btnSalva.setDisable(true);
        this.btnSalva.setVisible(false);
    }   

    public void nascondiFrecceCardSpecifici(int i){
        if (i < 0 || i >= this.resourcesContainer.getChildren().size()) return;
        VBox card = (VBox) this.resourcesContainer.getChildren().get(i);
        VBox infoArea = (VBox) card.getChildren().get(1);
        HBox actionBox = (HBox) infoArea.getChildren().get(2);
        
        Button btnFrecciaSinistra = (Button) actionBox.getChildren().get(0);
        Button btnFrecciaDestra = (Button) actionBox.getChildren().get(1);
        
        btnFrecciaSinistra.setVisible(false);
        btnFrecciaSinistra.setManaged(false);
        btnFrecciaDestra.setVisible(false);
        btnFrecciaDestra.setManaged(false);
    }

    public void condividiContenutiOn() {
        sendFileControl.abilitaSelezione();
    }

    public void inviaSelezione(){
        this.sendFileControl.createResumePageBound(this.getSelectedContent(), this.rootContainer.getScene().getWindow(),this.email);
    }

    public void abilitaContentCheckBox(int i){
        if (i < 0 || i >= this.resourcesContainer.getChildren().size()) return;
        
        VBox card = (VBox) this.resourcesContainer.getChildren().get(i);
        StackPane previewArea = (StackPane) card.getChildren().get(0);
        
        for (javafx.scene.Node node : previewArea.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox chkSeleziona = (CheckBox) node;
                chkSeleziona.setDisable(false);
                chkSeleziona.setVisible(true);
                chkSeleziona.setManaged(true);
                break; 
            }
        }
    }

 

    public List<ContenutoEntity> getSelectedContent() {
    List<ContenutoEntity> selezionati = new ArrayList<>();
    
    // Cicla attraverso tutti i nodi (le card) all'interno del contenitore delle risorse
    for (javafx.scene.Node node : this.resourcesContainer.getChildren()) {
        if (node instanceof VBox) {
            VBox card = (VBox) node;
            
            // La previewArea (StackPane) è il primo elemento all'interno della card (indice 0)
            if (!card.getChildren().isEmpty() && card.getChildren().get(0) instanceof StackPane) {
                StackPane previewArea = (StackPane) card.getChildren().get(0);
                
                // Cicla tra i figli della previewArea per individuare la CheckBox
                for (javafx.scene.Node child : previewArea.getChildren()) {
                    if (child instanceof CheckBox) {
                        CheckBox chkSeleziona = (CheckBox) child;
                        
                        // Se la checkbox è selezionata, recupera l'entità associata alla card
                        if (chkSeleziona.isSelected()) {
                            ContenutoEntity contenuto = (ContenutoEntity) card.getUserData();
                            if (contenuto != null) {
                                selezionati.add(contenuto);
                            }
                        }
                        break; // Trovata la checkbox di questa card, passa alla card successiva
                    }
                }
            }
        }
    }
    
    return selezionati;
    }
    public void pulisciRisultatiRicercaUtenti() {
        if (this.risultatiRicercaUtentiContainer != null) {
            this.risultatiRicercaUtentiContainer.getChildren().clear();
        }
    }

    public void mostraListaStudenti(List<StudenteEntity> listaStudenti) {
        this.risultatiRicercaUtentiContainer.getChildren().clear();

        Label titolo = new Label("Risultati ricerca utenti");
        titolo.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #12305C;");
        this.risultatiRicercaUtentiContainer.getChildren().add(titolo);

        if (listaStudenti == null || listaStudenti.isEmpty()) {
            Label nessunRisultato = new Label("Nessun utente trovato.");
            nessunRisultato.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #8FA9C7;");
            this.risultatiRicercaUtentiContainer.getChildren().add(nessunRisultato);
            return;
        }

        for (StudenteEntity utente : listaStudenti) {
            HBox cardUtente = new HBox(15);
            cardUtente.setAlignment(Pos.CENTER_LEFT);
            cardUtente.setPadding(new Insets(12));
            cardUtente.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: #E8EFF5; -fx-border-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(9,27,51,0.05), 8, 0, 0, 3);");

            ImageView imgProfilo = new ImageView();
            imgProfilo.setFitWidth(40);
            imgProfilo.setFitHeight(40);
            imgProfilo.setPreserveRatio(false);

            if (utente.getFotoProfilo() != null && utente.getFotoProfilo().length > 0) {
                imgProfilo.setImage(new Image(new ByteArrayInputStream(utente.getFotoProfilo())));
            } else {
                try {
                    imgProfilo.setImage(new Image(getClass().getResourceAsStream("/Assets/defaultProfilePicture.jpeg")));
                } catch (Exception e) {
                    // Ignora se manca immagine default
                }
            }

            Circle clip = new Circle(20, 20, 20);
            imgProfilo.setClip(clip);

            VBox infoUtente = new VBox(3);

            Label nomeCognome = new Label(utente.getNome() + " " + utente.getCognome());
            nomeCognome.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #12305C;");

            Label emailLabel = new Label(utente.getEmail());
            emailLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #556E8A;");

            infoUtente.getChildren().addAll(nomeCognome, emailLabel);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button btnVisualizza = new Button("VISUALIZZA PROFILO");
            btnVisualizza.setStyle("-fx-background-color: linear-gradient(to right, #1A4073, #12305C); -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 7 14; -fx-background-radius: 20; -fx-cursor: hand;");
            btnVisualizza.setOnAction(e -> this.hc.visualizzaStudente(utente));

            cardUtente.getChildren().addAll(imgProfilo, infoUtente, spacer, btnVisualizza);
            this.risultatiRicercaUtentiContainer.getChildren().add(cardUtente);
        }
    }

    public void mostraPublicContentBound(PublicContentBound publicContentBound) {
        this.publicContentBound = publicContentBound;

        this.rootContainer.getChildren().clear();
        this.rootContainer.getChildren().add(this.publicContentBound.visualizza());
    }

    public void mostraListaContenutiPubblici(List<ContenutoEntity> contenuti) {
        if (this.publicContentBound != null) {
            this.publicContentBound.mostraListaContenuti(contenuti);
        }
    }

    public void caricaContenutoPubblico(ContenutoEntity contenuto) {
        if (this.publicContentBound != null && contenuto != null) {
            this.publicContentBound.caricaContenuto(contenuto);
        }
    }
    public void avviaEliminazioneContenuto(ContenutoEntity contenuto) {
        this.hc.avviaEliminazioneContenuto(contenuto, this.rootContainer.getScene().getWindow());
    }
    

    public void sendToModifica(ContenutoEntity contenuto) {
    this.hc.sendToModifica(contenuto,this.rootContainer);
}


    public void disabilitaContentCheckBox(int i){
        if (i < 0 || i >= this.resourcesContainer.getChildren().size()) return;
        
        VBox card = (VBox) this.resourcesContainer.getChildren().get(i);
        StackPane previewArea = (StackPane) card.getChildren().get(0);
        
        for (javafx.scene.Node node : previewArea.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox chkSeleziona = (CheckBox) node;
                chkSeleziona.setDisable(true);
                chkSeleziona.setVisible(false);
                chkSeleziona.setManaged(true);
                break; 
            }
        }
    }


    public void enableSendButton(){
        this.btnInvia.setVisible(true);
        this.btnInvia.setDisable(false);
        
        this.btnInterrompi.setVisible(true);
        this.btnInterrompi.setDisable(false);
       
        this.btnCondividiContenuti.setDisable(true);
    }

    public void disableSendButton(){
        this.btnInvia.setVisible(false);
        this.btnInvia.setDisable(true);
        // Fa scomparire il pulsante di interruzione
        this.btnInterrompi.setVisible(false);
        this.btnInterrompi.setDisable(true);
        // Ripristina il pulsante di attivazione condivisione
        this.btnCondividiContenuti.setDisable(false);
    }


    public void caricaPaginaCondivisioniPassate() {
        if (this.rootContainer.getScene() != null) {
            this.sendFileControl.recuperaCondivisoni(this.email, this.rootContainer.getScene().getWindow());
        }
    }

    public void interrompiCondivisione(){
        // Ripristina la visibilità dei bottoni della navbar
        disableSendButton();
        this.resetInterfacciaRiordinamento();
        // Cicla su tutte le card per nascondere e disabilitare le checkbox di selezione
        int numberOfCards = getNumberOfCards();
        for(int i = 0; i < numberOfCards; i++){
            disabilitaContentCheckBox(i);
        }
    }

    public void disableCondividiContenuti(){
        this.btnCondividiContenuti.setDisable(true);
        btnInterrompi.setVisible(false);
    }

    public void enableCondividiContenuti(){
        this.btnCondividiContenuti.setDisable(false);
        btnInterrompi.setVisible(false);
    }


    public void abilitaInterfacciaRiordinamento() {
        // Mostra e abilita il pulsante SALVA (coordinato con l'interruzione)
        this.btnSalva.setVisible(false);
        this.btnSalva.setDisable(true);
        // Blocca gli altri flussi per evitare conflitti (es. non puoi condividere mentre riordini)
        this.btnCondividiContenuti.setDisable(true);
        this.btnRiordinaContenuti.setDisable(true);
    }

    // Nasconde e disabilita l'interfaccia di riordinamento (Speculare a disableSendButton)
    public void disabilitaInterfacciaRiordinamento() {
        // Nasconde il pulsante SALVA e l'interruzione
        this.btnSalva.setVisible(true);
        this.btnSalva.setDisable(false);
        
        
        // Ripristina la navbar principale riabilitando i bottoni standard
        this.btnCondividiContenuti.setDisable(false);
        this.btnRiordinaContenuti.setDisable(false);
    }

    public void resetInterfacciaRiordinamento(){
        this.btnSalva.setVisible(false);
        this.btnSalva.setDisable(true);
         this.btnCondividiContenuti.setDisable(false);
        this.btnRiordinaContenuti.setDisable(false);

    }

    // Metodo attivato dal click su "Interrompi" durante il riordinamento
}