package client.MacroGestioneProfilo;

import java.io.ByteArrayInputStream;
import java.util.List;

import client.GeneralClasses.Entities.ContenutoEntity;
import client.GeneralClasses.Entities.StudenteEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PublicContentBound {

    private HomePageControl hc;
    private StudenteEntity studente;
    private VBox rootContainer;
    private FlowPane contenutiContainer;

    public PublicContentBound(HomePageControl hc, StudenteEntity studente) {
        this.hc = hc;
        this.studente = studente;
        this.rootContainer = new VBox(0);
        this.rootContainer.setStyle("-fx-background-color: #F0F4F8;");
    }

    public VBox visualizza() {
        VBox header = new VBox(15);
        header.setPadding(new Insets(30));
        header.setStyle("-fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, rgba(9,27,51,0.08), 15, 0, 0, 5);");

        HBox topRow = new HBox(15);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Button btnIndietro = new Button("← Torna alla home");
        btnIndietro.setStyle("-fx-background-color: transparent; -fx-border-color: #12305C; -fx-text-fill: #12305C; -fx-font-weight: bold; -fx-padding: 8 12; -fx-cursor: hand;");
        btnIndietro.setOnAction(e -> this.hc.tornaHome());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topRow.getChildren().addAll(btnIndietro, spacer);

        HBox profileBox = new HBox(20);
        profileBox.setAlignment(Pos.CENTER_LEFT);

        ImageView imgProfilo = new ImageView();
        imgProfilo.setFitWidth(80);
        imgProfilo.setFitHeight(80);
        imgProfilo.setPreserveRatio(true);

        if (studente.getFotoProfilo() != null && studente.getFotoProfilo().length > 0) {
            imgProfilo.setImage(new Image(new ByteArrayInputStream(studente.getFotoProfilo())));
        } else {
            try {
                imgProfilo.setImage(new Image(getClass().getResourceAsStream("/Assets/defaultProfilePicture.jpeg")));
            } catch (Exception e) {
                // immagine default assente
            }
        }

        Circle clip = new Circle(40, 40, 40);
        imgProfilo.setClip(clip);

        VBox datiStudente = new VBox(6);

        Label lblNome = new Label(studente.getNome() + " " + studente.getCognome());
        lblNome.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #091B33;");

        Label lblEmail = new Label(studente.getEmail());
        lblEmail.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #556E8A;");

        Label lblDescrizione = new Label();
        lblDescrizione.setWrapText(true);
        lblDescrizione.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #2C4A73;");

        if (studente.getDescrizione() != null && !studente.getDescrizione().trim().isEmpty()) {
            lblDescrizione.setText(studente.getDescrizione());
        } else {
            lblDescrizione.setText("Nessuna descrizione disponibile.");
        }

        datiStudente.getChildren().addAll(lblNome, lblEmail, lblDescrizione);
        profileBox.getChildren().addAll(imgProfilo, datiStudente);

        header.getChildren().addAll(topRow, profileBox);

        Label titoloContenuti = new Label("Contenuti pubblici");
        titoloContenuti.setPadding(new Insets(25, 30, 5, 30));
        titoloContenuti.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #091B33;");

        this.contenutiContainer = new FlowPane();
        this.contenutiContainer.setHgap(30);
        this.contenutiContainer.setVgap(30);
        this.contenutiContainer.setPadding(new Insets(20, 30, 30, 30));
        this.contenutiContainer.setAlignment(Pos.TOP_LEFT);

        VBox scrollContent = new VBox(0);
        scrollContent.getChildren().addAll(titoloContenuti, this.contenutiContainer);

        ScrollPane scrollPane = new ScrollPane(scrollContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F0F4F8; -fx-background-color: transparent; -fx-border-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.rootContainer.getChildren().addAll(header, scrollPane);

        return this.rootContainer;
    }

    public void mostraListaContenuti(List<ContenutoEntity> contenuti) {
        this.contenutiContainer.getChildren().clear();

        if (contenuti == null || contenuti.isEmpty()) {
            Label lbl = new Label("Questo studente non ha ancora contenuti pubblici.");
            lbl.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 15px; -fx-text-fill: #8FA9C7; -fx-padding: 40;");
            this.contenutiContainer.getChildren().add(lbl);
            return;
        }

        for (ContenutoEntity contenuto : contenuti) {
            caricaContenuto(contenuto);
        }
    }

    public void caricaContenuto(ContenutoEntity contenuto) {
        VBox card = new VBox(0);
        card.setPrefWidth(320);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(9,27,51,0.06), 10, 0, 0, 5); -fx-border-color: #E8EFF5; -fx-border-width: 1; -fx-border-radius: 8;");

        StackPane previewArea = new StackPane();
        previewArea.setPrefHeight(200);
        previewArea.setCursor(Cursor.HAND);
        previewArea.setStyle("-fx-background-color: #091B33; -fx-background-radius: 8 8 0 0;");

        String mimeType = contenuto.getTipo().toLowerCase();

        if (mimeType.startsWith("image/")) {
            ByteArrayInputStream bis = new ByteArrayInputStream(contenuto.getFile());
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

        previewArea.setOnMouseClicked(e -> this.hc.apriDocumento(contenuto));

        VBox infoArea = new VBox(10);
        infoArea.setPadding(new Insets(20));

        Label lblTitolo = new Label(contenuto.getTitolo());
        lblTitolo.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #091B33;");

        Label lblDescrizione = new Label(contenuto.getDescrizione());
        lblDescrizione.setWrapText(true);
        lblDescrizione.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #2C4A73;");

        infoArea.getChildren().addAll(lblTitolo, lblDescrizione);
        card.getChildren().addAll(previewArea, infoArea);

        this.contenutiContainer.getChildren().add(card);
    }
}