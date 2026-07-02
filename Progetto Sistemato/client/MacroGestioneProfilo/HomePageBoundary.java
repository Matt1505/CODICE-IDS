package client.MacroGestioneProfilo;

import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.control.*;
import javafx.scene.paint.Color; 
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.swing.plaf.synth.Region;

import client.GeneralClasses.Entities.ContenutoEntity;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import client.GeneralClasses.Entities.StudenteEntity;

public class HomePageBoundary {
    private String email;
    private VBox rootContainer;
    private FlowPane resourcesContainer; 
    private HomePageControl hc;
    private Button btnCaricaFile;
    private Button btnCondividiContenuti;
    private Button btnRiordinaContenuti;
    private Button btnGestioneProfilo;
    private Button btnModificaProfilo;
    private TextField txtCercaUtenti;      

    public HomePageBoundary(String email) {
        this.email = email;
        this.hc = new HomePageControl(email, this);
        this.rootContainer = new VBox(0); 
        this.rootContainer.setStyle("-fx-background-color: #F0F4F8;"); // Sfondo app
        
        // --- TESTATA (NAVBAR MODERNA) ---
        VBox header = new VBox(20);
        header.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 30; -fx-effect: dropshadow(three-pass-box, rgba(9,27,51,0.08), 15, 0, 0, 5);");
        
        // Layout orizzontale superiore che spinge il blocco profilo a destra
        HBox topHeaderRow = new HBox();
        topHeaderRow.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("Piattaforma AFAM"); // non modificare
        titleLabel.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #091B33;");
        
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
            profileImageView.setImage(new Image(getClass().getResourceAsStream("/Assets/defaultProfilePicture.jpeg")));
        } catch (Exception e) {
            e.printStackTrace(); // non modificare
        }
        profileImageView.setFitWidth(45);
        profileImageView.setFitHeight(45);
        profileImageView.setPreserveRatio(true);
        
        Circle clip = new Circle(22.5, 22.5, 22.5);
        profileImageView.setClip(clip);

        // Bottone estetico di modifica profilo (icona matita) adiacente all'immagine
        Button btnModificaInTestata = new Button("✏");
        btnModificaInTestata.setStyle("-fx-background-color: #F0F4F8; -fx-text-fill: #091B33; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 15; -fx-padding: 5 8; -fx-font-weight: bold;");
        btnModificaInTestata.setOnMouseEntered(e -> btnModificaInTestata.setStyle("-fx-background-color: #091B33; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 15; -fx-padding: 5 8; -fx-font-weight: bold;"));
        btnModificaInTestata.setOnMouseExited(e -> btnModificaInTestata.setStyle("-fx-background-color: #F0F4F8; -fx-text-fill: #091B33; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 15; -fx-padding: 5 8; -fx-font-weight: bold;"));
        btnModificaInTestata.setOnAction(event -> clickModificaFotoProfilo());

        Button btnSalva= new Button("SALVA");
        btnSalva.setStyle("-fx-background-color: #F0F4F8; -fx-text-fill: #091B33; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 15; -fx-padding: 5 8; -fx-font-weight: bold;");
        btnSalva.setOnMouseEntered(e -> btnSalva.setStyle("-fx-background-color: #091B33; -fx-text-fill: white; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 15; -fx-padding: 5 8; -fx-font-weight: bold;"));
        btnSalva.setOnMouseExited(e -> btnSalva.setStyle("-fx-background-color: #F0F4F8; -fx-text-fill: #091B33; -fx-font-size: 12px; -fx-cursor: hand; -fx-background-radius: 15; -fx-padding: 5 8; -fx-font-weight: bold;"));
        btnSalva.setOnAction(event -> caricaNuovoOrdinamento());

        profileContainer.getChildren().addAll(btnModificaInTestata, profileImageView);
        
        // Etichetta segnaposto per Nome e Cognome posizionata SOTTO l'immagine del profilo
        Label lblNomeCognome = new Label("Nome Cognome");
        lblNomeCognome.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2C4A73;");
        
        profileVBox.getChildren().addAll(profileContainer, lblNomeCognome);
        topHeaderRow.getChildren().addAll(titleLabel, spacer, profileVBox);

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        String navBtnStyle = "-fx-background-color: transparent; -fx-border-color: #091B33; -fx-border-radius: 5; -fx-padding: 10 15; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #091B33; -fx-cursor: hand; -fx-font-size: 13px;";
        
        this.btnCaricaFile = new Button("Nuovo Caricamento");
        this.btnCaricaFile.setStyle(navBtnStyle);
        this.btnCaricaFile.setOnMouseEntered(e -> this.btnCaricaFile.setStyle("-fx-background-color: #091B33; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10 15; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-cursor: hand;"));
        this.btnCaricaFile.setOnMouseExited(e -> this.btnCaricaFile.setStyle(navBtnStyle));
        this.btnCaricaFile.setOnAction(event -> attivaCaricamentoFile());
        
        // Bottone Condividi Contenuti
        this.btnCondividiContenuti = new Button("CONDIVIDI CONTENUTI");
        this.btnCondividiContenuti.setStyle("-fx-background-color: #12305C; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 15; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 13px;");
        this.btnCondividiContenuti.setOnMouseEntered(e -> this.btnCondividiContenuti.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 15; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 13px;"));
        this.btnCondividiContenuti.setOnMouseExited(e -> this.btnCondividiContenuti.setStyle("-fx-background-color: #12305C; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 15; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 13px;"));
        this.btnCondividiContenuti.setOnAction(event -> clickCondividiContenuti());

        // Bottone RIORDINA CONTENUTI
        this.btnRiordinaContenuti = new Button("RIORDINA CONTENUTI");
        this.btnRiordinaContenuti.setStyle("-fx-background-color: transparent; -fx-border-color: #12305C; -fx-border-radius: 5; -fx-padding: 10 15; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-cursor: hand; -fx-font-size: 13px;");
        this.btnRiordinaContenuti.setOnMouseEntered(e -> this.btnRiordinaContenuti.setStyle("-fx-background-color: #12305C; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10 15; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-cursor: hand;"));
        this.btnRiordinaContenuti.setOnMouseExited(e -> this.btnRiordinaContenuti.setStyle("-fx-background-color: transparent; -fx-border-color: #12305C; -fx-border-radius: 5; -fx-padding: 10 15; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #12305C; -fx-font-size: 13px;"));
        this.btnRiordinaContenuti.setOnAction(event -> attivaRiordinamentoContenuti());

        this.btnGestioneProfilo = new Button("Gestione Profilo");
        this.btnGestioneProfilo.setStyle(navBtnStyle);
        this.btnGestioneProfilo.setOnAction(event -> clickGestioneProfilo());
        
        this.btnModificaProfilo = new Button("Impostazioni");
        this.btnModificaProfilo.setStyle(navBtnStyle);
        this.btnModificaProfilo.setOnAction(event -> clickModificaFotoProfilo());
        
        // Barra di ricerca
        this.txtCercaUtenti = new TextField();
        this.txtCercaUtenti.setPromptText(" Cerca altri utenti...");
        this.txtCercaUtenti.setPrefWidth(220);
        this.txtCercaUtenti.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 9; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px;");
        this.txtCercaUtenti.setOnAction(event -> cercaUtenti(txtCercaUtenti.getText()));

        topBar.getChildren().addAll(this.btnCaricaFile, this.btnCondividiContenuti, this.btnRiordinaContenuti, this.btnGestioneProfilo, this.btnModificaProfilo, this.txtCercaUtenti);
        header.getChildren().addAll(topHeaderRow, topBar);

        // --- NUOVA SEZIONE: DESCRIZIONE PROFILO UTENTE (SOPRA AI CONTENUTI) ---
        VBox bioSection = new VBox(10);
        bioSection.setPadding(new Insets(20, 30, 10, 30));
        bioSection.setStyle("-fx-background-color: transparent;");
        
        HBox bioTitleBar = new HBox(15);
        bioTitleBar.setAlignment(Pos.CENTER_LEFT);
        
        Label lblBioTitle = new Label("Descrizione del Profilo / Biografia");
        lblBioTitle.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #091B33;");
        
        Button btnModificaBio = new Button("Modifica Descrizione");
        btnModificaBio.setStyle("-fx-background-color: transparent; -fx-border-color: #12305C; -fx-border-radius: 4; -fx-text-fill: #12305C; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 4 10;");
        btnModificaBio.setOnMouseEntered(e -> btnModificaBio.setStyle("-fx-background-color: #12305C; -fx-border-color: #12305C; -fx-border-radius: 4; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 4 10;"));
        btnModificaBio.setOnMouseExited(e -> btnModificaBio.setStyle("-fx-background-color: transparent; -fx-border-color: #12305C; -fx-border-radius: 4; -fx-text-fill: #12305C; -fx-font-size: 11px; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 4 10;"));
        btnModificaBio.setOnAction(event -> clickModificaFotoProfilo()); // Agganciato coerentemente all'azione di modifica esistente

        bioTitleBar.getChildren().addAll(lblBioTitle, btnModificaBio);
        
        Label lblBioContent = new Label("Nessuna descrizione inserita. Modifica il tuo profilo per aggiungere una breve biografia o per presentare le tue opere.");
        lblBioContent.setWrapText(true);
        lblBioContent.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #556E8A;");
        
        bioSection.getChildren().addAll(bioTitleBar, lblBioContent);

        // --- CONTENITORE RISORSE ---
        this.resourcesContainer = new FlowPane();
        this.resourcesContainer.setHgap(30);
        this.resourcesContainer.setVgap(30);
        this.resourcesContainer.setPadding(new Insets(20, 30, 30, 30));
        this.resourcesContainer.setAlignment(Pos.TOP_LEFT);
        this.resourcesContainer.setStyle("-fx-background-color: transparent;");
        
        // VBox interno allo ScrollPane che impila la sezione Descrizione sopra la Galleria di Contenuti
        VBox scrollContentWrapper = new VBox(0);
        scrollContentWrapper.setStyle("-fx-background-color: transparent;");
        scrollContentWrapper.getChildren().addAll(bioSection, this.resourcesContainer);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(scrollContentWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F0F4F8; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        this.rootContainer.getChildren().addAll(header, scrollPane);
    }

    public void visualizza() { this.rootContainer.setVisible(true); }
    public VBox getRootContainer() { return this.rootContainer; }
    public void attivaCaricamentoFile() {
        Stage stageAttuale = (Stage) rootContainer.getScene().getWindow();
        this.hc.setStage(stageAttuale);
        if (this.hc != null) { this.hc.visualizza(); }
    }

    public int getNumberOfCards(){
        return this.resourcesContainer.getChildren().size();
    }
    public void attivaRiordinamentoContenuti() {

        this.hc.abilitaRiodinamentoContenuti();

    }

    public void clickCondividiContenuti() {}
    public void cercaUtenti(String query) {
        if(query != null && !query.trim().isEmpty()) { }
    }
    public void clickGestioneProfilo() {}
    public void clickModificaFotoProfilo() {

    // Passa lo stage attuale al controller per permettergli di cambiare schermata o aprire un popup
    Stage stageAttuale = (Stage) rootContainer.getScene().getWindow();
    this.hc.setStage(stageAttuale); 
    
    // Delega l'azione al controller
    this.hc.createCaricaFotoProfiloBound();


    }

    public void mostraRisorsa() { 
        VBox card = new VBox(0);
        card.setPrefWidth(320); 
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(9,27,51,0.06), 10, 0, 0, 5); -fx-border-color: #E8EFF5; -fx-border-width: 1; -fx-border-radius: 8;");
        this.resourcesContainer.getChildren().add(card);
    }

    public void setUserInfo(StudenteEntity studente) {
    if (studente == null) return;

    try {
        // 1. Recupera l'HBox principale del topHeaderRow (il primo figlio della testata)
        VBox header = (VBox) this.rootContainer.getChildren().get(0);
        HBox topHeaderRow = (HBox) header.getChildren().get(0);

        // 2. Trova la profileVBox (l'ultimo elemento a destra, all'indice 2)
        VBox profileVBox = (VBox) topHeaderRow.getChildren().get(2);

        // 3. All'interno di profileVBox:
        //    - All'indice 0 c'è la profileContainer (HBox con pulsante matita e immagine)
        //    - All'indice 1 c'è la label Nome Cognome
        HBox profileContainer = (HBox) profileVBox.getChildren().get(0);
        Label lblNomeCognome = (Label) profileVBox.getChildren().get(1);

        // 4. All'interno di profileContainer, l'ImageView si trova all'indice 1
        ImageView profileImageView = (ImageView) profileContainer.getChildren().get(1);

        // Aggiorna Nome e Cognome
        lblNomeCognome.setText(studente.getNome() + " " + studente.getCognome());

        // Aggiorna Foto Profilo se presente
        if (studente.getFotoProfilo() != null && studente.getFotoProfilo().length > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(studente.getFotoProfilo());
            profileImageView.setImage(new Image(bis));
        }

        // 5. Recupera la sezione Biografia navigando nello ScrollPane
        ScrollPane scrollPane = (ScrollPane) this.rootContainer.getChildren().get(1);
        VBox scrollContentWrapper = (VBox) scrollPane.getContent();
        VBox bioSection = (VBox) scrollContentWrapper.getChildren().get(0);
        
        // La label del contenuto si trova all'indice 1 di bioSection
        Label lblBioContent = (Label) bioSection.getChildren().get(1);

        // Aggiorna Biografia
        if (studente.getDescrizione() != null && !studente.getDescrizione().trim().isEmpty()) {
            lblBioContent.setText(studente.getDescrizione());
        } else {
            lblBioContent.setText("Nessuna descrizione inserita. Modifica il tuo profilo per aggiungere una breve biografia o per presentare le tue opere.");
        }

    } catch (Exception e) {
        System.err.println("Errore durante l'aggiornamento dei dati utente nell'interfaccia:");
        e.printStackTrace();
    }
}

    public void caricaDatiRisorsa(ContenutoEntity risorsa) {
        if (this.resourcesContainer.getChildren().isEmpty()) return;
        // prende ultima risorsa
        VBox card = (VBox) this.resourcesContainer.getChildren().get(this.resourcesContainer.getChildren().size() - 1);
        // associa i dati della risorsa
        card.setUserData(risorsa);

        StackPane previewArea = new StackPane();
        previewArea.setPrefHeight(200);
        previewArea.setCursor(Cursor.HAND);
        previewArea.setStyle("-fx-background-color: #091B33; -fx-background-radius: 8 8 0 0;"); 

        String mimeType = risorsa.getTipo().toLowerCase();
        if (mimeType.startsWith("image/")) {
            ByteArrayInputStream bis = new ByteArrayInputStream(risorsa.getFile());
            ImageView imgView = new ImageView(new Image(bis));
            imgView.setFitWidth(318);
            imgView.setFitHeight(200);
            imgView.setPreserveRatio(false); 
            previewArea.getChildren().add(imgView);
        } else if (mimeType.startsWith("video/") || mimeType.startsWith("audio/")) {
            Rectangle bg = new Rectangle(318, 200, Color.web("#0A1C3A"));
            Label icon = new Label("▶");
            icon.setStyle("-fx-font-size: 50px; -fx-text-fill: #8FA9C7;");
            previewArea.getChildren().addAll(bg, icon);
        } else if (mimeType.equals("application/pdf")) {
            Rectangle bg = new Rectangle(318, 200, Color.web("#FFFFFF"));
            Label icon = new Label("PDF DOCUMENT");
            icon.setStyle("-fx-font-family: 'Georgia'; -fx-font-weight: bold; -fx-text-fill: #12305C;");
            previewArea.getChildren().addAll(bg, icon);
        } else {
            Label icon = new Label("FILE");
            icon.setStyle("-fx-text-fill: white;");
            previewArea.getChildren().add(icon);
        }

        previewArea.setOnMouseClicked(e -> this.hc.apriDocumento(risorsa));
        previewArea.setOnMouseEntered(e -> previewArea.setOpacity(0.85));
        previewArea.setOnMouseExited(e -> previewArea.setOpacity(1.0));

        VBox infoArea = new VBox(10);
        infoArea.setPadding(new Insets(20));
        
        Label lblTitolo = new Label(risorsa.getTitolo());
        lblTitolo.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #091B33;");
        
        Label lblDescrizione = new Label(risorsa.getDescrizione());
        lblDescrizione.setWrapText(true);
        lblDescrizione.setMinHeight(Region.USE_COMPUTED_SIZE);
        lblDescrizione.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #2C4A73;");

        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPadding(new Insets(10, 0, 0, 0));
        //inserisco freccia a sinistra e destra  disabilitati
         Button btnFrecciaSinistra = new Button("←");
        btnFrecciaSinistra.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #12305C; -fx-text-fill: #12305C; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 4 12;");
        btnFrecciaSinistra.setDisable(true);
        btnFrecciaSinistra.setVisible(false); // <<-- AGGIUNGI QUESTO: Nasconde la freccia all'avvio
        btnFrecciaSinistra.setManaged(false); // <<-- AGGIUNGI QUESTO: Rimuove l'ingombro visivo nel layout
        btnFrecciaSinistra.setOnAction(e -> this.hc.invertiOrdineRisorse(1, card));

        Button btnFrecciaDestra = new Button("→");
        btnFrecciaDestra.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #12305C; -fx-text-fill: #12305C; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 4 12;");
        btnFrecciaDestra.setDisable(true);
        btnFrecciaDestra.setVisible(false); // <<-- AGGIUNGI QUESTO: Nasconde la freccia all'avvio
        btnFrecciaDestra.setManaged(false); // <<-- AGGIUNGI QUESTO: Rimuove l'ingombro visivo nel layout
        btnFrecciaDestra.setOnAction(e -> this.hc.invertiOrdineRisorse(2, card));
        // Inseriamo le frecce come primi elementi dell'actionBox
        actionBox.getChildren().addAll(btnFrecciaSinistra, btnFrecciaDestra);

        infoArea.getChildren().addAll(lblTitolo, lblDescrizione, actionBox);
        card.getChildren().addAll(previewArea, infoArea);
    }

    public void associaBottoneModifica() {
        if (this.resourcesContainer.getChildren().isEmpty()) return;
        VBox card = (VBox) this.resourcesContainer.getChildren().get(this.resourcesContainer.getChildren().size() - 1);
        VBox infoArea = (VBox) card.getChildren().get(1);
        HBox actionBox = (HBox) infoArea.getChildren().get(2);
        
        Button btnModifica = new Button("Modifica");
        btnModifica.setStyle("-fx-background-color: transparent; -fx-border-color: #12305C; -fx-text-fill: #12305C; -fx-font-size: 11px; -fx-cursor: hand; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        actionBox.getChildren().add(btnModifica);
    }

    public void associaBottoneRimozione() {
    
        if (this.resourcesContainer.getChildren().isEmpty()) return;
        VBox card = (VBox) this.resourcesContainer.getChildren().get(this.resourcesContainer.getChildren().size() - 1);
        VBox infoArea = (VBox) card.getChildren().get(1);
        HBox actionBox = (HBox) infoArea.getChildren().get(2);
        
        Button btnRimozione = new Button("Rimuovi");
        btnRimozione.setStyle("-fx-background-color: transparent; -fx-border-color: #B22222; -fx-text-fill: #B22222; -fx-font-size: 11px; -fx-cursor: hand; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        actionBox.getChildren().add(btnRimozione);
    
    }

    public void backgroundDisplay(String messaggio) {
        this.resourcesContainer.getChildren().clear();
        Label lbl = new Label(messaggio);
        lbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-text-fill: #8FA9C7; -fx-padding: 100;");
        this.resourcesContainer.getChildren().add(lbl);
    }
    //aggiorna ultimo elemento della lista di card con lo stato delle frecce
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
        
        // Rende visibili le frecce nel momento in cui viene attivato il riordinamento
        btnFrecciaSinistra.setVisible(true);
        btnFrecciaSinistra.setManaged(true);
        btnFrecciaDestra.setVisible(true);
        btnFrecciaDestra.setManaged(true);
        
        // Configura l'effettiva cliccabilità basata sulla posizione della card
        btnFrecciaSinistra.setDisable(!abilitaSinistra);
        btnFrecciaDestra.setDisable(!abilitaDestra);
    }

    public VBox getCardByIndex(int index) {
        if (index < 0 || index >= this.resourcesContainer.getChildren().size()) return null;
        return (VBox) this.resourcesContainer.getChildren().get(index);
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
    
    // 2. Rimuovo prima l'indice più alto per non sballare gli indici di quello più basso
    int maxIndex = Math.max(indiceA, indiceB);
    int minIndex = Math.min(indiceA, indiceB);
    
    this.resourcesContainer.getChildren().remove(maxIndex);
    this.resourcesContainer.getChildren().remove(minIndex);
    
    // 3. Reinserisco i nodi scambiati invertendo le posizioni originali
    if (indiceA < indiceB) {
        this.resourcesContainer.getChildren().add(minIndex, nodeB);
        this.resourcesContainer.getChildren().add(maxIndex, nodeA);
    } else {
        this.resourcesContainer.getChildren().add(minIndex, nodeA);
        this.resourcesContainer.getChildren().add(maxIndex, nodeB);
    }
}

public void caricaNuovoOrdinamento() {
    this.hc.salvaNuovoOrdinamento();
    
}   
    

}