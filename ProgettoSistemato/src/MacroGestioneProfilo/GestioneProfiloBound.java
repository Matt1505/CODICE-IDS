package src.MacroGestioneProfilo;

import java.io.ByteArrayInputStream;
import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import src.MacroGestioneContenuti.HomePageControl;
import src.MacroGestioneCredenziali.GestionePasswordControl;

public class GestioneProfiloBound {
    private HomePageControl controller;
    private GestionePasswordControl passwordControl;
    private Label lblStatusFile;
    private Label lblDescrizioneCorrente;
    private TextArea txtDescrizione;
    private VBox descrizioneContainer;
    private ImageView imgProfilo;


    private String email;

    public GestioneProfiloBound(HomePageControl controller) {
        this.controller = controller;
        this.passwordControl=new GestionePasswordControl();
        this.email = controller.getEmail();
    }

    public VBox visualizza() {
        String labelStyle = "-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px;";

        VBox root = new VBox(18);
        root.setStyle("-fx-background-color: #F0F4F8;");
        root.setPadding(new Insets(30));

        Label lblIntestazione = new Label("Gestione Profilo");
        lblIntestazione.setStyle("-fx-font-family: 'Georgia'; -fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #091B33; -fx-padding: 0 0 5 0;");

        Label lblInfo = new Label("Da questa pagina puoi aggiornare la tua foto profilo, modificare la descrizione oppure uscire dal tuo account.");
        lblInfo.setStyle("-fx-text-fill: #556E8A; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px;");
        lblInfo.setWrapText(true);

        /*
         * SEZIONE FOTO PROFILO
         */
        Label lblSezioneFoto = new Label("Foto profilo");
        lblSezioneFoto.setStyle(labelStyle);

        this.imgProfilo = new ImageView();
        this.imgProfilo.setFitWidth(90);
        this.imgProfilo.setFitHeight(90);
        this.imgProfilo.setPreserveRatio(false);
        this.imgProfilo.setSmooth(true);

        byte[] fotoProfilo = this.controller.getFotoProfilo();
        if (fotoProfilo != null && fotoProfilo.length > 0) {
            this.imgProfilo.setImage(new Image(new ByteArrayInputStream(fotoProfilo)));
        } else {
            try {
                this.imgProfilo.setImage(new Image(getClass().getResourceAsStream("src/Assets/defaultProfilePicture.jpeg")));
            } catch (Exception e) {
                // Immagine default assente
            }
        }

        Circle clip = new Circle(45, 45, 45);
        this.imgProfilo.setClip(clip);

        Button btnSeleziona = new Button("SFOGLIA IMMAGINI");
        btnSeleziona.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 15; -fx-cursor: hand;");
        btnSeleziona.setOnMouseEntered(e -> btnSeleziona.setStyle("-fx-background-color: #091B33; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 15; -fx-cursor: hand;"));
        btnSeleziona.setOnMouseExited(e -> btnSeleziona.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 15; -fx-cursor: hand;"));
        btnSeleziona.setOnAction(e -> acquisisciFileSelezionato((Stage) btnSeleziona.getScene().getWindow()));

        this.lblStatusFile = new Label("Nessuna immagine profilo selezionata");
        this.lblStatusFile.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-style: italic;");

        Button btnAggiungi = new Button("IMPOSTA FOTO PROFILO");
        btnAggiungi.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        btnAggiungi.setOnMouseEntered(e -> btnAggiungi.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnAggiungi.setOnMouseExited(e -> btnAggiungi.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnAggiungi.setOnAction(e -> confermaCaricamentoFoto());

        VBox comandiFoto = new VBox(10);
        comandiFoto.getChildren().addAll(btnSeleziona, this.lblStatusFile, btnAggiungi);

        HBox boxFoto = new HBox(25);
        boxFoto.setAlignment(Pos.CENTER_LEFT);
        boxFoto.getChildren().addAll(this.imgProfilo, comandiFoto);

        /*
         * SEZIONE DESCRIZIONE
         */
        Label lblSezioneBio = new Label("Descrizione profilo");
        lblSezioneBio.setStyle(labelStyle);

        this.descrizioneContainer = new VBox(10);

        String descrizione = this.controller.getDescrizioneProfilo();
        if (descrizione == null || descrizione.trim().isEmpty()) {
            descrizione = "Nessuna descrizione inserita. Clicca qui per aggiungerla.";
        }

        this.lblDescrizioneCorrente = new Label(descrizione);
        this.lblDescrizioneCorrente.setWrapText(true);
        this.lblDescrizioneCorrente.setCursor(Cursor.HAND);
        this.lblDescrizioneCorrente.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 12; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #2C4A73;");
        this.lblDescrizioneCorrente.setOnMouseClicked(e -> mostraEditorDescrizione());

        Label lblSuggerimentoBio = new Label("Clicca sulla descrizione per modificarla.");
        lblSuggerimentoBio.setStyle("-fx-text-fill: #556E8A; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-style: italic;");

        this.descrizioneContainer.getChildren().addAll(this.lblDescrizioneCorrente, lblSuggerimentoBio);

        /*
         * SEZIONE ACCOUNT
         */
        Label lblSezioneAccount = new Label("Account");
        lblSezioneAccount.setStyle(labelStyle);

        Button btnModificaPassword = new Button("MODIFICA PASSWORD");
btnModificaPassword.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand;");

btnModificaPassword.setOnMouseEntered(e -> btnModificaPassword.setStyle("-fx-background-color: #091B33; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand;"));

btnModificaPassword.setOnMouseExited(e -> btnModificaPassword.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-cursor: hand;"));

btnModificaPassword.setOnAction(e -> this.modificaPwd());

        Button btnLogout = new Button("LOGOUT");
        btnLogout.setStyle("-fx-background-color: #DC3545; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("-fx-background-color: #B02A37; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle("-fx-background-color: #DC3545; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnLogout.setOnAction(e -> this.logout());

        Button btnAnnulla = new Button("HOME");
        btnAnnulla.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;");
        btnAnnulla.setOnMouseEntered(e -> btnAnnulla.setStyle("-fx-background-color: #091B33; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;"));
        btnAnnulla.setOnMouseExited(e -> btnAnnulla.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;"));
        btnAnnulla.setOnAction(e -> this.goHome());

       root.getChildren().addAll(
        lblIntestazione,
        lblInfo,

        new Separator(),

        lblSezioneFoto,
        boxFoto,

        new Separator(),

        lblSezioneBio,
        this.descrizioneContainer,

        new Separator(),

        lblSezioneAccount,
        btnModificaPassword,
        btnLogout,
        btnAnnulla
);

        return root;
    }

    private void mostraEditorDescrizione() {
        String descrizioneAttuale = this.controller.getDescrizioneProfilo();

        this.txtDescrizione = new TextArea();
        this.txtDescrizione.setPromptText("Inserisci una breve descrizione del tuo profilo artistico...");
        this.txtDescrizione.setWrapText(true);
        this.txtDescrizione.setPrefRowCount(4);
        this.txtDescrizione.setText(descrizioneAttuale != null ? descrizioneAttuale : "");
        this.txtDescrizione.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px;");

        Button btnSalvaDescrizione = new Button("SALVA DESCRIZIONE");
        btnSalvaDescrizione.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        btnSalvaDescrizione.setOnMouseEntered(e -> btnSalvaDescrizione.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnSalvaDescrizione.setOnMouseExited(e -> btnSalvaDescrizione.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btnSalvaDescrizione.setOnAction(e -> confermaModificaDescrizione());

        Button btnAnnullaDescrizione = new Button("ANNULLA");
        btnAnnullaDescrizione.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #091B33; -fx-border-radius: 4; -fx-background-radius: 4; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-cursor: hand;");
        btnAnnullaDescrizione.setOnAction(e -> nascondiEditorDescrizione());

        HBox pulsantiDescrizione = new HBox(10);
        pulsantiDescrizione.getChildren().addAll(btnSalvaDescrizione, btnAnnullaDescrizione);

        this.descrizioneContainer.getChildren().clear();
        this.descrizioneContainer.getChildren().addAll(this.txtDescrizione, pulsantiDescrizione);
    }

    private void nascondiEditorDescrizione() {
        this.descrizioneContainer.getChildren().clear();

        String descrizione = this.controller.getDescrizioneProfilo();
        if (descrizione == null || descrizione.trim().isEmpty()) {
            descrizione = "Nessuna descrizione inserita. Clicca qui per aggiungerla.";
        }

        this.lblDescrizioneCorrente = new Label(descrizione);
        this.lblDescrizioneCorrente.setWrapText(true);
        this.lblDescrizioneCorrente.setCursor(Cursor.HAND);
        this.lblDescrizioneCorrente.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 12; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #2C4A73;");
        this.lblDescrizioneCorrente.setOnMouseClicked(e -> mostraEditorDescrizione());

        Label lblSuggerimentoBio = new Label("Clicca sulla descrizione per modificarla.");
        lblSuggerimentoBio.setStyle("-fx-text-fill: #556E8A; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-style: italic;");

        this.descrizioneContainer.getChildren().addAll(this.lblDescrizioneCorrente, lblSuggerimentoBio);
    }

    private void acquisisciFileSelezionato(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona la nuova foto profilo");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Immagini (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg")
        );

        File fileSelezionato = fileChooser.showOpenDialog(stage);

        if (fileSelezionato != null) {
            this.lblStatusFile.setText("File pronto: " + fileSelezionato.getName());

            try {
                this.imgProfilo.setImage(new Image(fileSelezionato.toURI().toString()));
            } catch (Exception e) {
                // Anteprima non disponibile
            }

            controller.trasformaInBlobFotoProfilo(fileSelezionato);
        }
    }

    private void confermaCaricamentoFoto() {
        controller.aggiornaFotoProfilo(this.lblStatusFile.getScene().getWindow());
    }

    private void confermaModificaDescrizione() {
        controller.aggiornaDescrizioneProfilo(
            this.txtDescrizione.getText(),
            this.txtDescrizione.getScene().getWindow()
        );
    }

    private void goHome() {
        Stage stageCorrente = (Stage) this.lblStatusFile.getScene().getWindow();
        controller.createHomePageBoundary(email, stageCorrente);
    }

    private void logout() {
        Stage stageCorrente = (Stage) this.lblStatusFile.getScene().getWindow();
        controller.logout(stageCorrente);
    }
    private void modificaPwd() {
        Stage stageCorrente = (Stage) this.lblStatusFile.getScene().getWindow();
        this.passwordControl.updatePwd(email,stageCorrente);
    }
}