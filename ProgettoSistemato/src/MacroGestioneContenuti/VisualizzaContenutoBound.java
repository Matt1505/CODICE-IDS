package src.MacroGestioneContenuti;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import src.GeneralClasses.Entities.ContenutoEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Window;
import javafx.scene.layout.HBox;

public class VisualizzaContenutoBound {

    private HomePageControl controller;
    private ContenutoEntity contenuto;

    public VisualizzaContenutoBound(HomePageControl controller, ContenutoEntity contenuto) {
        this.controller = controller;
        this.contenuto = contenuto;
    }

    public VBox visualizza() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #F0F4F8;");

        Label titolo = new Label(contenuto.getTitolo());
        titolo.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #091B33;");

        Label descrizione = new Label(contenuto.getDescrizione());
        descrizione.setWrapText(true);
        descrizione.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #556E8A;");

        Button btnIndietro = new Button("Torna alla Home");
        btnIndietro.setStyle("-fx-background-color: #091B33; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-cursor: hand;");
        btnIndietro.setOnAction(e -> {
            Window window = btnIndietro.getScene().getWindow();
            controller.clickHome(window);
        });

        VBox contentBox = new VBox(10);

        String mimeType = contenuto.getTipo().toLowerCase();

        if (mimeType.startsWith("image/")) {
            contentBox.getChildren().add(creaImageView());
        } else if (mimeType.startsWith("video/") || mimeType.startsWith("audio/")) {
            ImageView imageView = creaImageView();

            VBox imageContainer = new VBox(imageView);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.setPadding(new Insets(15));

contentBox.getChildren().add(imageContainer);
        } else {
            Label nonSupportato = new Label("Anteprima interna non disponibile per questo formato.");
            nonSupportato.setStyle("-fx-font-size: 14px; -fx-text-fill: #8FA9C7;");
            contentBox.getChildren().add(nonSupportato);
        }

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        root.getChildren().addAll(titolo, descrizione, scrollPane, btnIndietro);

        return root;
    }

    private ImageView creaImageView() {
        Image image = new Image(new ByteArrayInputStream(contenuto.getFile()));

        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(600);
        imageView.setFitHeight(450);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        return imageView;
    }

    private VBox creaMediaView() {
    VBox mediaBox = new VBox(10);

    try {
        File tempFile = creaFileTemporaneo();

        Media media = new Media(tempFile.toURI().toString());

        Label lblErrore = new Label();
        lblErrore.setStyle("-fx-text-fill: #DC3545; -fx-font-weight: bold;");

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setFitWidth(800);
        mediaView.setPreserveRatio(true);

        Button btnPlay = new Button("Play");
        btnPlay.setOnAction(e -> mediaPlayer.play());

        Button btnPausa = new Button("Pausa");
        btnPausa.setOnAction(e -> mediaPlayer.pause());

        HBox controlli = new HBox(10);
        controlli.getChildren().addAll(btnPlay, btnPausa);

        media.setOnError(() -> {
            System.out.println("Errore Media: " + media.getError());
            lblErrore.setText("Formato video non supportato.");
        });

        mediaPlayer.setOnError(() -> {
            System.out.println("Errore MediaPlayer: " + mediaPlayer.getError());
            lblErrore.setText("Errore durante la riproduzione del video.");
        });

        mediaPlayer.setOnReady(() -> {
            System.out.println("Video pronto: " + tempFile.getAbsolutePath());
        });

        mediaBox.getChildren().addAll(mediaView, controlli, lblErrore);

    } catch (Exception e) {
        e.printStackTrace();

        Label errore = new Label("Impossibile riprodurre questo video all'interno dell'applicazione.");
        errore.setWrapText(true);
        errore.setStyle("-fx-text-fill: #DC3545; -fx-font-weight: bold;");

        Label suggerimento = new Label("Il file potrebbe usare un codec non supportato da JavaFX Media oppure l'ambiente WSL/Linux potrebbe non avere i componenti multimediali necessari.");
        suggerimento.setWrapText(true);
        suggerimento.setStyle("-fx-text-fill: #556E8A;");

        mediaBox.getChildren().addAll(errore, suggerimento);
    }

    return mediaBox;
}

    private File creaFileTemporaneo() throws IOException {
        String estensione = ".bin";
        String mimeType = contenuto.getTipo().toLowerCase();

        if (mimeType.startsWith("video/")) {
            estensione = "." + mimeType.split("/")[1];
        } else if (mimeType.startsWith("audio/")) {
            estensione = "." + mimeType.split("/")[1];
        }

        File tempFile = File.createTempFile("contenuto_", estensione);
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(contenuto.getFile());
        }

        return tempFile;
    }
}