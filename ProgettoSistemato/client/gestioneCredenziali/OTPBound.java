package client.gestioneCredenziali;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OTPBound {

    public OTPBound() {
        
    }
    
    // Overloading per supportare il flusso di Registrazione (accetta email)
    public static Scene createOTPScene(Stage stage, RegisterControl rc, String email) {
        return buildScene(stage, rc, null, email);
    }

    // Overloading per supportare il flusso di Login (accetta email)
    public static Scene createOTPScene(Stage stage, LoginControl lc, String email) {
        return buildScene(stage, null, lc, email);
    }

    // Metodo privato centralizzato per evitare codice duplicato
    private static Scene buildScene(Stage stage, RegisterControl rc, LoginControl lc, String email) {
        GridPane grid = new GridPane();
        // Sfondo coordinato in grigio-blu
        grid.setStyle("-fx-background-color: #F0F4F8;");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15); 
        grid.setVgap(15);
        grid.setPadding(new Insets(35, 35, 35, 35));

        // Titolo
        Text title = new Text("Piattaforma AFAM\nVerifica Sicurezza (OTP)");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        title.setStyle("-fx-fill: #091B33;");
        grid.add(title, 0, 0, 2, 1);

        Label infoLabel = new Label("Inserisci il codice OTP ricevuto:");
        infoLabel.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px;");
        grid.add(infoLabel, 0, 1, 2, 1);

        TextField otpField = new TextField();
        otpField.setPromptText("es. 123456");
        otpField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        grid.add(otpField, 0, 2, 2, 1);

        // Bottone
        Button verifyBtn = new Button("Verifica Codice");
        verifyBtn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        
        // Effetti hover per pulsante
        verifyBtn.setOnMouseEntered(e -> verifyBtn.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        verifyBtn.setOnMouseExited(e -> verifyBtn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        
        grid.add(verifyBtn, 1, 3);

        verifyBtn.setOnAction(e -> {
            // Invoca la funzione di ponte corretta passando il valore dell'OTP e l'email
            sendOtpToControl(otpField.getText(), email, rc, lc);
        });

        // Dimensione adattata per ospitare comodamente la nuova intestazione
        return new Scene(grid, 420, 280);
    }

    // Risolti gli errori 1 e 2: Utilizzo della firma esatta 'requestGeneratedOTP(email, codice)' definita nei Control
    public static void sendOtpToControl(String inputOtp, String email, RegisterControl rc, LoginControl lc){
        if (rc != null) {
            rc.requestGeneratedOTP(email, inputOtp);
        } else if (lc != null) {
            lc.requestGeneratedOTP(email, inputOtp);
        }
    }
}