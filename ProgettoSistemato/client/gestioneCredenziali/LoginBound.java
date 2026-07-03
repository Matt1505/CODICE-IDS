package client.gestioneCredenziali;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Platform;


public class LoginBound extends Application {
    // FUNZIONI E VARIABILI ORIGINALI CONSERVATE INTEGRALMENTE
    private GridPane grid;
    private LoginControl controller;
    private RegisterControl rc; 
    private Stage stage; 

    public LoginBound() {
            this.controller = new LoginControl();
            this.rc= new RegisterControl();
    }



    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage; 
        this.visualizza(primaryStage);
    }
        
    

    public Scene getScene(Stage stage) {
        this.controller = new LoginControl();
        this.rc = new RegisterControl();
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15); grid.setVgap(15);
        grid.setPadding(new Insets(40, 40, 40, 40));
        mostraFormLogin();
        return new Scene(grid, 500, 550);
    }

    private void mostraFormLogin() {
        // Stile artistico/accademico: sfondo grigio-blu
        grid.setStyle("-fx-background-color: #F0F4F8;");

        // Titolo Piattaforma AFAM con stile elegante
        Text scenetitle = new Text("Piattaforma AFAM\nAccesso Area Artisti");
        scenetitle.setFont(Font.font("Georgia", FontWeight.BOLD, 24)); 
        scenetitle.setStyle("-fx-fill: #091B33;"); // Blu molto scuro
        grid.add(scenetitle, 0, 0, 2, 1);
        
        Label lblEmail = new Label("Email");
        lblEmail.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px;");
        grid.add(lblEmail, 0, 3); 
        
        TextField emailField = new TextField();
        emailField.setPromptText("inserisci la tua email istituzionale");
        emailField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-pref-width: 250;");
        grid.add(emailField, 1, 3);

        Label lblPw = new Label("Password");
        lblPw.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px;");
        grid.add(lblPw, 0, 5); 
        
        PasswordField pwField = new PasswordField();
        pwField.setPromptText("inserisci la tua password");
        pwField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-pref-width: 250;");
        grid.add(pwField, 1, 5);

        // Bottone principale "Accedi"
        Button btn = new Button("LOGIN");
        btn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        
        // Effetto hover per il bottone Accedi
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));

        HBox hbBtn = new HBox(15); 
        hbBtn.setAlignment(Pos.CENTER_RIGHT); 
        hbBtn.getChildren().add(btn);
        
        // Bottone "Registrati"
        Button btnRegistrati = new Button("Nuovo Artista? Registrati");
        btnRegistrati.setStyle("-fx-background-color: transparent; -fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 0;");
        
        btnRegistrati.setOnMouseEntered(e -> btnRegistrati.setStyle("-fx-background-color: transparent; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 0; -fx-underline: true;"));
        btnRegistrati.setOnMouseExited(e -> btnRegistrati.setStyle("-fx-background-color: transparent; -fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 0;"));

        btnRegistrati.setOnAction(e -> this.nuovoAccount());
        
        hbBtn.getChildren().add(0, btnRegistrati); 
        grid.add(hbBtn, 0, 7, 2, 1); 

        // Interazione Utente -> Boundary originale intatta
        btn.setOnAction(e -> {
            sendCredentials(
                emailField.getText(), 
                pwField.getText()
            );
        });
    }

    private void nuovoAccount() {
        this.rc.createRegisterBoundary(stage);
    }
    private void sendCredentials(String email, String pw) {
        controller.checkEmptyForm(email, pw,this.stage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    public void visualizza(Object windowContext) {
        if(windowContext instanceof Stage){
            this.stage= (Stage) windowContext;
            Platform.runLater(()->{
            this.stage.setTitle("Piattaforma AFAM - Accedi"); // Titolo aggiornato
            this.controller = new LoginControl();
            this.rc = new RegisterControl(); 
            
            grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(15); 
            grid.setVgap(15);
            grid.setPadding(new Insets(45, 45, 45, 45)); 

            mostraFormLogin();

            // Leggermente allargata per ospitare il nuovo titolo elegante
            Scene scene = new Scene(grid, 500, 450);
            this.stage.setScene(scene);
            this.stage.show();


            });
        
        
        }        
        
    }
}