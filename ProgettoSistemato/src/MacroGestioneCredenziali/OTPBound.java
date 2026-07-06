package src.MacroGestioneCredenziali;

import javax.swing.border.StrokeBorder;

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
import javafx.application.Platform;

public class OTPBound {
    private RegisterControl registerController;
    private LoginControl loginController;
    private String email;
    private GestionePasswordControl gestionePasswordControl;

    public OTPBound(RegisterControl rc,String email) {
        this.registerController=rc;
        this.email=email;
        this.gestionePasswordControl = gestionePasswordControl;
    }

    public OTPBound(LoginControl lc,String email){

        this.loginController=lc;
        this.email=email;

    }

    public OTPBound(GestionePasswordControl gestionePasswordControl, String email) {
        this.gestionePasswordControl = gestionePasswordControl;
        this.email = email;
    }
    public void visualizza(Object windowContext){
        if(windowContext instanceof Stage){
            Stage stage=(Stage) windowContext;
            Platform.runLater(() -> {
                this.buildScene(stage, registerController, loginController, gestionePasswordControl, email);
            });    
        }


    }
    

    private static void buildScene(Stage stage, RegisterControl rc, LoginControl lc, GestionePasswordControl gpc, String email) {
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
        Button verifyBtn = new Button("VERIFICA CODICE");
        verifyBtn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        
        // Effetti hover per pulsante
        verifyBtn.setOnMouseEntered(e -> verifyBtn.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        verifyBtn.setOnMouseExited(e -> verifyBtn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        
        grid.add(verifyBtn, 1, 3);

        verifyBtn.setOnAction(e -> {
            // Invoca la funzione di ponte corretta passando il valore dell'OTP e l'email
            sendOtpToControl(otpField.getText(), email, rc, lc, gpc, stage);
        });

        // Dimensione adattata per ospitare comodamente la nuova intestazione
        Scene scene = new Scene(grid,420,320);
        stage.setTitle("Verifica Sicurezza (OTP)");
        stage.setScene(scene);
        stage.show();
    }

    // Risolti gli errori 1 e 2: Utilizzo della firma esatta 'requestGeneratedOTP(email, codice)' definita nei Control
    public static void sendOtpToControl(String inputOtp, String email, RegisterControl rc, LoginControl lc, GestionePasswordControl gpc, Stage stage) {
        if (rc != null) {
            rc.requestGeneratedOTP(email, inputOtp, stage);
        } else if (lc != null) {
            lc.requestGeneratedOTP(email, inputOtp, stage);
        } else if (gpc != null) {
            gpc.requestGeneratedOTP(email, inputOtp, stage);
        }
    }
}