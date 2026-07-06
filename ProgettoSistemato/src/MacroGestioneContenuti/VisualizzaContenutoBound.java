package src.MacroGestioneContenuti;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Desktop;

import src.GeneralClasses.Entities.ContenutoEntity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.layout.HBox;

public class VisualizzaContenutoBound {

    private HomePageControl controller;
    private ContenutoEntity contenuto;
    // 1. MODIFICA: Dichiarato qui per renderlo accessibile a tutta la classe
    private MediaPlayer mediaPlayer; 

    public VisualizzaContenutoBound(HomePageControl controller, ContenutoEntity contenuto) {
        this.controller = controller;
        this.contenuto = contenuto;
    }

    public void visualizza(Object window) {
        VBox root = new VBox(20);
        
        if(window instanceof Stage) {
            Stage s = (Stage) window;
            
            root.setPadding(new Insets(0, 0, 20, 0)); 
            root.setStyle("-fx-background-color: #F4F7FA;"); 

            // --- TOOLBAR SUPERIORE CENTRATA ---
            VBox toolbar = new VBox(8);
            toolbar.setAlignment(Pos.CENTER); 
            toolbar.setPadding(new Insets(20, 40, 20, 40));
            toolbar.setStyle("-fx-background-color: #091B33;"); 

            Label titolo = new Label(contenuto.getTitolo());
            titolo.setStyle("-fx-font-family: 'Segoe UI', Helvetica, Arial; -fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label descrizione = new Label(contenuto.getDescrizione());
            descrizione.setWrapText(true);
            descrizione.setMaxWidth(740);
            descrizione.setAlignment(Pos.CENTER); 
            descrizione.setStyle("-fx-font-family: 'Segoe UI', Arial; -fx-font-size: 14px; -fx-text-fill: #A0B2C6; -fx-line-spacing: 4px;");

            toolbar.getChildren().addAll(titolo, descrizione);

            // Bottone Indietro coordinato
            Button btnIndietro = new Button("HOME");
            btnIndietro.setStyle("-fx-background-color: #091B33; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 10 20; -fx-cursor: hand;");
            
            btnIndietro.setOnMouseEntered(e -> btnIndietro.setStyle("-fx-background-color: #112D52; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 10 20; -fx-cursor: hand;"));
            btnIndietro.setOnMouseExited(e -> btnIndietro.setStyle("-fx-background-color: #091B33; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 10 20; -fx-cursor: hand;"));
            
            btnIndietro.setOnAction(e -> {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();    
                    mediaPlayer.dispose(); 
                }
                Window currWindow = btnIndietro.getScene().getWindow();
                controller.tornaHome(currWindow);
            });

            // Box Contenitore dell'anteprima
            VBox contentBox = new VBox(15);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(25));
            contentBox.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");

            // CORREZIONE 1: Convertiamo in minuscolo ed eliminiamo gli spazi vuoti residui del DB (.trim())
            String mimeType = contenuto.getTipo().toLowerCase().trim();

            if (mimeType.startsWith("image/")) {
                contentBox.getChildren().add(creaImageView());
            } else if (mimeType.startsWith("video/") || mimeType.startsWith("audio/")) {
                VBox mediaContainer = creaMediaView();
                mediaContainer.setAlignment(Pos.CENTER);
                contentBox.getChildren().add(mediaContainer);
            } else if (mimeType.contains("pdf")) { // CORREZIONE 2: Usiamo .contains() al posto di .equals()
                Label infoPdf = new Label("Documento PDF pronto per la lettura esterna");
                infoPdf.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #091B33;");
                
                Button apriPdfBtn = new Button("APRI DOCUMENTO PDF");
                apriPdfBtn.setStyle("-fx-background-color: #E04545; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 12 24; -fx-cursor: hand;");
                apriPdfBtn.setOnAction(e -> apriFileEsternamente());
                
                VBox pdfContainer = new VBox(12, infoPdf, apriPdfBtn);
                pdfContainer.setAlignment(Pos.CENTER);
                contentBox.getChildren().add(pdfContainer);
            } else {
                Label nonSupportato = new Label("Anteprima interna non disponibile per questo formato di file.");
                nonSupportato.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #8FA9C7; -fx-font-style: italic;");
                contentBox.getChildren().add(nonSupportato);
            }

            ScrollPane scrollPane = new ScrollPane(contentBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPadding(new Insets(0, 30, 0, 30)); 
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

            HBox bottomBar = new HBox(btnIndietro);
            bottomBar.setAlignment(Pos.CENTER_LEFT);
            bottomBar.setPadding(new Insets(10, 30, 0, 30));

            root.getChildren().addAll(toolbar, scrollPane, bottomBar);
            
            s.setScene(new Scene(root, 850, 700)); 
        }
    }

    private ImageView creaImageView() {
        Image image = new Image(new ByteArrayInputStream(contenuto.getFile()));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(650);
        imageView.setFitHeight(450);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    private VBox creaMediaView() {
        VBox mediaBox = new VBox(15);
        mediaBox.setAlignment(Pos.CENTER);
        
        try {
            File tempFile = creaFileTemporaneo();
            Media media = new Media(tempFile.toURI().toString());

            Label lblErrore = new Label();
            lblErrore.setStyle("-fx-font-family: 'Segoe UI'; -fx-text-fill: #D93838; -fx-font-weight: bold;");

            // 3. MODIFICA: Valorizziamo l'attributo di istanza globale anziché ridefinirlo localmente
            this.mediaPlayer = new MediaPlayer(media);

            if (contenuto.getTipo().toLowerCase().startsWith("audio/")) {
                try {
                    Image imgAudio = new Image(getClass().getResourceAsStream("/images/audio_icon.png"));
                    ImageView audioImageView = new ImageView(imgAudio);
                    audioImageView.setFitWidth(180);
                    audioImageView.setPreserveRatio(true);
                    mediaBox.getChildren().add(audioImageView); 
                } catch (Exception e) {
                    Label audioIconFallback = new Label("🎵");
                    audioIconFallback.setStyle("-fx-font-size: 80px; -fx-text-fill: #556E8A;");
                    mediaBox.getChildren().add(audioIconFallback);
                }
            } else {
                MediaView mediaView = new MediaView(this.mediaPlayer);
                mediaView.setFitWidth(700);
                mediaView.setPreserveRatio(true);
                mediaBox.getChildren().add(mediaView);
            }

            Button btnPlay = new Button("RIPRODUCI");
            btnPlay.setStyle("-fx-background-color: #556E8A; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-padding: 8 16; -fx-cursor: hand;");
            btnPlay.setOnAction(e -> mediaPlayer.play());

            Button btnPausa = new Button("PAUSA");
            btnPausa.setStyle("-fx-background-color: #78909C; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-padding: 8 16; -fx-cursor: hand;");
            btnPausa.setOnAction(e -> mediaPlayer.pause());

            HBox controlli = new HBox(12);
            controlli.setAlignment(Pos.CENTER);
            controlli.getChildren().addAll(btnPlay, btnPausa);

            media.setOnError(() -> {
                System.out.println("Errore Media: " + media.getError());
                lblErrore.setText("Il formato multimediale non è supportato dall'ambiente di sistema.");
            });

            mediaBox.getChildren().addAll(controlli, lblErrore);

        } catch (Exception e) {
            Label errore = new Label("Impossibile caricare il componente di riproduzione.");
            errore.setStyle("-fx-font-family: 'Segoe UI'; -fx-text-fill: #D93838; -fx-font-weight: bold;");
            mediaBox.getChildren().add(errore);
            e.printStackTrace();
        }
        return mediaBox;
    }

  private void apriFileEsternamente() {
        // Creiamo un thread separato per evitare di bloccare o far crashare JavaFX
        new Thread(() -> {
            try {
                File tempFile = creaFileTemporaneo();
                
                if (tempFile == null || !tempFile.exists()) {
                    System.err.println("Errore: Il file temporaneo non è stato creato correttamente.");
                    return;
                }

                // Tentativo 1: Utilizzo del Desktop standard di Java AWT
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    try {
                        Desktop.getDesktop().open(tempFile);
                        return; // Se ha successo, usciamo dal thread
                    } catch (Exception ex) {
                        System.err.println("Metodo Desktop fallito, provo metodo alternativo via comando di sistema...");
                    }
                }

                // Tentativo 2 (Fallback): Se AWT fallisce o non è supportato, usiamo i comandi nativi del sistema operativo
                String os = System.getProperty("os.name").toLowerCase();
                ProcessBuilder pb;
                
                if (os.contains("win")) {
                    // Comando Windows per aprire il file con l'applicazione predefinita
                    pb = new ProcessBuilder("cmd.exe", "/c", "start", "", tempFile.getAbsolutePath());
                } else if (os.contains("mac")) {
                    // Comando macOS
                    pb = new ProcessBuilder("open", tempFile.getAbsolutePath());
                } else {
                    // Comando Linux (xdg-open)
                    pb = new ProcessBuilder("xdg-open", tempFile.getAbsolutePath());
                }
                
                pb.start();

            } catch (Exception e) {
                // Cattura qualsiasi eccezione o runtime error strano impedendo al programma di crashare
                System.err.println("Impossibile aprire il file PDF. Dettagli errore:");
                e.printStackTrace();
            }
        }).start();
    }

private File creaFileTemporaneo() throws IOException {
        String estensione = ".bin";
        // CORREZIONE 3: Applichiamo la stessa protezione anche qui
        String mimeType = contenuto.getTipo().toLowerCase().trim();

        if (mimeType.startsWith("video/") || mimeType.startsWith("audio/")) {
            estensione = "." + mimeType.split("/")[1];
        } else if (mimeType.contains("pdf")) { // CORREZIONE 4: .contains() al posto di .equals()
            estensione = ".pdf";
        }

        File tempFile = File.createTempFile("contenuto_", estensione);
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(contenuto.getFile());
        }

        return tempFile;
    }
}