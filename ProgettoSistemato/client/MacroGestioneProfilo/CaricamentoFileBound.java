package client.MacroGestioneProfilo;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;


public class CaricamentoFileBound {

    // Riferimento alla Control per passare i dati
    private HomePageControl controller;

    // Dati grafici da prelevare
    private TextField txtTitolo;
    private TextArea txtDescrizione;
    private Label lblStatusFile;
    private String email;
    
    private CheckBox chkPubblico;

    // Il costruttore riceve la Control
    public CaricamentoFileBound(HomePageControl controller) {
        this.controller = controller;
        this.email=controller.getEmail(); 
    }

    // Metodo corrispondente a "visualizza" e "mostraFormContenuto" nel diagramma
    public VBox visualizza() {
        String labelStyle = "-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px;";
        String fieldStyle = "-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;";

        txtTitolo = new TextField();
        txtTitolo.setStyle(fieldStyle);
        txtTitolo.setPromptText("Inserisci il titolo dell'opera");

        txtDescrizione = new TextArea();
        txtDescrizione.setStyle(fieldStyle);
        txtDescrizione.setPromptText("Inserisci una breve descrizione artistica o note sul contenuto...");
        txtDescrizione.setPrefRowCount(3);
        txtDescrizione.setWrapText(true);

        this.chkPubblico = new CheckBox("Rendi pubblico questo contenuto");
        this.chkPubblico.setSelected(true);
        this.chkPubblico.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-font-weight: bold;");

        Button btnSeleziona = new Button("Sfoglia File d'Arte...");
        btnSeleziona.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 15; -fx-cursor: hand;");
        
        btnSeleziona.setOnMouseEntered(e -> btnSeleziona.setStyle("-fx-background-color: #091B33; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 15; -fx-cursor: hand;"));
        btnSeleziona.setOnMouseExited(e -> btnSeleziona.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 15; -fx-cursor: hand;"));

        lblStatusFile = new Label("Nessun file d'arte selezionato");
        lblStatusFile.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-style: italic;");

        Button btnAggiungi = new Button("Aggiungi Contenuto");
        btnAggiungi.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        
        btnAggiungi.setOnMouseEntered(e -> btnAggiungi.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnAggiungi.setOnMouseExited(e -> btnAggiungi.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));

        // Otteniamo lo stage dalla scena corrente al momento del click per il FileChooser
        btnSeleziona.setOnAction(e -> acquisisciFileSelezionato((Stage) btnSeleziona.getScene().getWindow()));
        btnAggiungi.setOnAction(e -> confermaCaricamentoContenuto());
        Button btnAnnulla = new Button("Annulla e Torna alla Home");

        btnAnnulla.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;");
        btnAnnulla.setOnMouseEntered(e -> btnAnnulla.setStyle("-fx-background-color: #091B33; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;"));
        btnAnnulla.setOnMouseExited(e -> btnAnnulla.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;"));
        btnAnnulla.setOnAction(e -> this.goHome());
        VBox root = new VBox(12);
        root.setStyle("-fx-background-color: #F0F4F8;");
        root.setPadding(new Insets(30));
        
        Label lblIntestazione = new Label("Carica Nuovo Contenuto Artistico");
        lblIntestazione.setStyle("-fx-font-family: 'Georgia'; -fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #091B33; -fx-padding: 0 0 10 0;");

        Label lblTitolo = new Label("Titolo:"); lblTitolo.setStyle(labelStyle);
        Label lblDesc = new Label("Descrizione dell'Opera:"); lblDesc.setStyle(labelStyle);

        root.getChildren().addAll(
                lblIntestazione,
                lblTitolo, 
                txtTitolo, 
                lblDesc, 
                txtDescrizione, 
                btnSeleziona, 
                lblStatusFile,
                this.chkPubblico, 
                new Separator(), 
                btnAggiungi,
                btnAnnulla
        );

        return root;
    }

    // Metodo per convertire il file e passarlo alla Control
    private void acquisisciFileSelezionato(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il file da caricare");
        File fileSelezionato = fileChooser.showOpenDialog(stage);
        
        if(fileSelezionato != null) {
            lblStatusFile.setText("File pronto: " + fileSelezionato.getName());
            // Passa l'intero file al controller, che si occuperà della memorizzazione temporanea
            controller.trasformaInBlob(fileSelezionato);
            
        }
    }

    


    // Risolto l'errore 3: Il metodo si limita a prelevare le stringhe e inviarle alla Control conformemente al tipo richiesto (String, String)
    private void confermaCaricamentoContenuto() {
        controller.salvaContenuto(
            txtTitolo.getText(),
            txtDescrizione.getText(),
            this.chkPubblico.isSelected(),
            this.txtTitolo.getScene().getWindow()
        );
    }

private void goHome() {
   //recuperiamo lo stage
    Stage stageCorrente = (Stage) txtTitolo.getScene().getWindow();
    
    // Passiamo lo Stage al costruttore corretto di PageControl
    
    //  Creiamo la Home (che si occuperà anche di fare il visualizza)
    controller.createHomePageBoundary(email, stageCorrente);
}
}