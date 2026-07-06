package src.MacroGestioneContenuti;

import java.beans.VetoableChangeListenerProxy;
import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import src.GeneralClasses.Entities.ContenutoEntity;

public class ModificaBound {

    private HomePageControl controller;
    private ContenutoEntity contenuto;

    private TextField txtTitolo;
    private TextArea txtDescrizione;
    private Label lblFileSelezionato;

    private File nuovoFile;

    public ModificaBound(HomePageControl controller, ContenutoEntity contenuto) {
        this.controller = controller;
        this.contenuto = contenuto;
    }
    public void mostraModificaBound(Object root) {
        if(root instanceof VBox){
            VBox rootContainer= (VBox) root;
            rootContainer.getChildren().clear();
            rootContainer.getChildren().add(this.visualizza());
        }
       
    } 

    public VBox visualizza() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F0F4F8;");

        Label lblPagina = new Label("Modifica Contenuto");
        lblPagina.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #091B33;");

        Label lblTitolo = new Label("Titolo");
        lblTitolo.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");

        this.txtTitolo = new TextField();
        this.txtTitolo.setText(contenuto.getTitolo());
        this.txtTitolo.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 8;");

        Label lblDescrizione = new Label("Descrizione");
        lblDescrizione.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");

        this.txtDescrizione = new TextArea();
        this.txtDescrizione.setText(contenuto.getDescrizione());
        this.txtDescrizione.setWrapText(true);
        this.txtDescrizione.setPrefRowCount(4);
        this.txtDescrizione.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 8;");

        Button btnScegliFile = new Button("SOSTITUISCI FILE");
        btnScegliFile.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 16; -fx-cursor: hand;");
        btnScegliFile.setOnAction(e -> scegliNuovoFile((Stage) btnScegliFile.getScene().getWindow()));

        this.lblFileSelezionato = new Label("Nessun nuovo file selezionato. Se non scegli un file, verranno modificati solo titolo e descrizione.");
        this.lblFileSelezionato.setWrapText(true);
        this.lblFileSelezionato.setStyle("-fx-text-fill: #556E8A; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px;");

        Button btnSalva = new Button("SALVA MODIFICHE");
        btnSalva.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        btnSalva.setOnAction(e -> this.confermaModificaContenuto());

        Button btnAnnulla = new Button("ANNULLA");
        btnAnnulla.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;");
        btnAnnulla.setOnAction(e -> tornaHome());

        root.getChildren().addAll(
            lblPagina,
            lblTitolo,
            this.txtTitolo,
            lblDescrizione,
            this.txtDescrizione,
            btnScegliFile,
            this.lblFileSelezionato,
            btnSalva,
            btnAnnulla
        );

        return root;
    }

    private void scegliNuovoFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona nuovo contenuto");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Tutti i file supportati", "*.png", "*.jpg", "*.jpeg", "*.pdf", "*.mp3", "*.wav", "*.mp4"),
            new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"),
            new FileChooser.ExtensionFilter("Audio", "*.mp3", "*.wav"),
            new FileChooser.ExtensionFilter("Video", "*.mp4"),
            new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );

        File fileScelto = fileChooser.showOpenDialog(stage);

        if (fileScelto != null) {
            this.nuovoFile = fileScelto;
            this.lblFileSelezionato.setText("Nuovo file selezionato: " + fileScelto.getName());
        }
    }

    private void confermaModificaContenuto() {
        Window window = this.txtTitolo.getScene().getWindow();

        this.controller.salvaModificheContenuto(
            this.contenuto,
            this.nuovoFile,
            this.txtTitolo.getText(),
            this.txtDescrizione.getText(),
            window
        );
    }

    private void tornaHome() {
        Window window = this.txtTitolo.getScene().getWindow();
        this.controller.tornaHome(window);
    }
}