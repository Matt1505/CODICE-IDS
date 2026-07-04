package client.MacroGestioneProfilo;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.SQLException;


public class CaricaFotoProfiloBound {
    // Riferimento alla Control per passare i dati
    private HomePageControl controller;
    // Dati grafici da prelevare
    private Label lblStatusFile;
    private String email;

    // Il costruttore riceve la Control
    public CaricaFotoProfiloBound(HomePageControl controller) {
        this.controller = controller;
        this.email = controller.getEmail(); 
    }

    // Metodo per generare il layout, perfettamente coerente con CaricamentoFileBound
    public VBox visualizza() {
        String labelStyle = "-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px;";

        Button btnSeleziona = new Button("Sfoglia Immagine Profilo...");
        btnSeleziona.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 15; -fx-cursor: hand;");
        
        btnSeleziona.setOnMouseEntered(e -> btnSeleziona.setStyle("-fx-background-color: #091B33; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 15; -fx-cursor: hand;"));
        btnSeleziona.setOnMouseExited(e -> btnSeleziona.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 15; -fx-cursor: hand;"));

        lblStatusFile = new Label("Nessuna immagine profilo selezionata");
        lblStatusFile.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-style: italic;");

        Button btnAggiungi = new Button("Imposta Foto Profilo");
        btnAggiungi.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        
        btnAggiungi.setOnMouseEntered(e -> btnAggiungi.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnAggiungi.setOnMouseExited(e -> btnAggiungi.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));

        // Otteniamo lo stage dalla scena corrente al momento del click per il FileChooser
        btnSeleziona.setOnAction(e -> acquisisciFileSelezionato((Stage) btnSeleziona.getScene().getWindow()));
        btnAggiungi.setOnAction(e -> confermaCaricamentoFoto());

        Button btnAnnulla = new Button("Annulla e Torna alla Home");
        btnAnnulla.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;");
        btnAnnulla.setOnMouseEntered(e -> btnAnnulla.setStyle("-fx-background-color: #091B33; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;"));
        btnAnnulla.setOnMouseExited(e -> btnAnnulla.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;"));
        btnAnnulla.setOnAction(e -> this.goHome());

        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: #F0F4F8;");
        root.setPadding(new Insets(30));
        
        Label lblIntestazione = new Label("Modifica Foto Profilo");
        lblIntestazione.setStyle("-fx-font-family: 'Georgia'; -fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #091B33; -fx-padding: 0 0 5 0;");

        Label lblInfo = new Label("Seleziona un'immagine (JPEG o PNG) per aggiornare il tuo avatar sulla Galleria Artistica.");
        lblInfo.setStyle("-fx-text-fill: #556E8A; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px;");
        lblInfo.setWrapText(true);

        root.getChildren().addAll(
                lblIntestazione,
                lblInfo,
                btnSeleziona, 
                lblStatusFile, 
                new Separator(), 
                btnAggiungi,
                btnAnnulla
        );

        return root;
    }

    // Metodo speculare a quello in CaricamentoFileBound per passare il file alla Control
    private void acquisisciFileSelezionato(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona la nuova foto profilo");
                // Applica i filtri per caricare esclusivamente immagini standard
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Immagini (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg")
        );
        
        File fileSelezionato = fileChooser.showOpenDialog(stage);
        
        if(fileSelezionato != null) {
            lblStatusFile.setText("File pronto: " + fileSelezionato.getName());
            // Passa l'intero file al controller per la memorizzazione temporanea
            controller.trasformaInBlob(fileSelezionato);
        }
    }

    // Passa l'intenzione di salvataggio dei metadati fissi del profilo alla Control
    private void confermaCaricamentoFoto() {
        // Invoca il salvataggio coordinato passando i metadati della foto profilo
        
            controller.aggiornaFotoProfilo(this.lblStatusFile.getScene().getWindow());
    }

    // Navigazione: recupera lo stage e sfrutta il PageControl per tornare in Homepage
    private void goHome() {
        // Recuperiamo lo stage tramite il nodo visibile di stato del file
        Stage stageCorrente = (Stage) lblStatusFile.getScene().getWindow();
        
        // Passiamo lo Stage al costruttore corretto di PageControl
        controller.createHomePageBoundary(email, stageCorrente);
        
        
        
    }
}