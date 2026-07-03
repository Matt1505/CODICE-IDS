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

public class RegisterBound extends Application {

    private GridPane grid;
    private RegisterControl controller;
    private LoginControl lc;
    private Stage stage;


    public RegisterBound(){
        this.controller= new RegisterControl();
        this.lc= new LoginControl();
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage=primaryStage;
        this.visualizza(primaryStage);
    }
        
    

    public Scene getScene(Stage stage) {
        this.controller = new RegisterControl();
        this.lc = new LoginControl();
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15); grid.setVgap(15);
        grid.setPadding(new Insets(40, 40, 40, 40));

        mostraFormRegistrazione();

        return new Scene(grid, 500, 600);
    }

    public void visualizza(Object primaryStage){
        if(primaryStage instanceof Stage){
            this.stage= (Stage) primaryStage;
            Platform.runLater(()->{
                stage.setTitle("Piattaforma AFAM - Registrazione Artista"); // Titolo aggiornato
                
                this.controller = new RegisterControl();
                
                grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(15); grid.setVgap(15);
                grid.setPadding(new Insets(40, 40, 40, 40));

                mostraFormRegistrazione();

                // Aumentata leggermente l'altezza (da 550 a 600) solo per far entrare il nuovo campo senza sovrapposizioni
                Scene scene = new Scene(grid, 500, 600); 
                stage.setScene(scene);
                stage.show();




            });
        }



    }

    private void mostraFormRegistrazione() {
        grid.setStyle("-fx-background-color: #F0F4F8;");

        Text scenetitle = new Text("Piattaforma AFAM\nRegistrazione Nuovo Artista");
        scenetitle.setFont(Font.font("Georgia", FontWeight.BOLD, 22)); 
        scenetitle.setStyle("-fx-fill: #091B33;");
        grid.add(scenetitle, 0, 0, 2, 1);

        String labelStyle = "-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px;";
        String fieldStyle = "-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-pref-width: 250;";

        Label lblNome = new Label("Nome:"); lblNome.setStyle(labelStyle);
        TextField nameField = new TextField(); nameField.setStyle(fieldStyle);
        grid.add(lblNome, 0, 1); grid.add(nameField, 1, 1);

        Label lblCognome = new Label("Cognome:"); lblCognome.setStyle(labelStyle);
        TextField surnameField = new TextField(); surnameField.setStyle(fieldStyle);
        grid.add(lblCognome, 0, 2); grid.add(surnameField, 1, 2);

        Label lblEmail = new Label("Email:"); lblEmail.setStyle(labelStyle);
        TextField emailField = new TextField(); emailField.setStyle(fieldStyle);
        grid.add(lblEmail, 0, 3); grid.add(emailField, 1, 3);

        Label lblCf = new Label("Codice Fiscale:"); lblCf.setStyle(labelStyle);
        TextField cfField = new TextField(); cfField.setStyle(fieldStyle);
        grid.add(lblCf, 0, 4); grid.add(cfField, 1, 4);

        // --- AGGIUNTO SOLO QUESTO CAMPO: MATRICOLA ---
        Label lblMatricola = new Label("Matricola:"); lblMatricola.setStyle(labelStyle);
        TextField matricolaField = new TextField(); matricolaField.setStyle(fieldStyle);
        grid.add(lblMatricola, 0, 5); grid.add(matricolaField, 1, 5);

        // Slittamento degli indici di riga successivi per fare spazio al nuovo elemento
        Label lblPw = new Label("Password:"); lblPw.setStyle(labelStyle);
        PasswordField pwField = new PasswordField(); pwField.setStyle(fieldStyle);
        grid.add(lblPw, 0, 6); grid.add(pwField, 1, 6);

        Label lblRepeatPw = new Label("Ripeti Password:"); lblRepeatPw.setStyle(labelStyle);
        PasswordField repeatPwField = new PasswordField(); repeatPwField.setStyle(fieldStyle);
        grid.add(lblRepeatPw, 0, 7); grid.add(repeatPwField, 1, 7);

        Button btn = new Button("REGISTRATI");
        btn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));

        Button btnLogin = new Button("Già possiedi un account? Accedi");
        btnLogin.setStyle("-fx-background-color: transparent; -fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 0;");
        
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle("-fx-background-color: transparent; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 0; -fx-underline: true;"));
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle("-fx-background-color: transparent; -fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 0;"));
        btnLogin.setOnAction(e -> this.lc.createLoginBoundary(stage));
        HBox hbBtn = new HBox(15); 
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        
        // AGGIUNGI ENTRAMBI I BOTTONI ALL'HBOX
        hbBtn.getChildren().addAll(btnLogin, btn); 
        
        // Aggiungi l'HBox alla griglia alla riga successiva corretta (8)
        grid.add(hbBtn, 1, 8);

        // Funzionalità intatta con l'aggiunta del valore di matricolaField
        btn.setOnAction(e -> {
            sendCredentials(
                nameField.getText(), 
                surnameField.getText(), 
                emailField.getText(), 
                cfField.getText(), 
                matricolaField.getText(), // Valore aggiunto
                pwField.getText(), 
                repeatPwField.getText()
            );
        });
    }

    // Firma modificata includendo la stringa matricola passata al control
    private void sendCredentials(String nome, String cognome, String email, 
                                 String cf, String matricola, String pw, String repeatPw) {
        controller.checkEmptyForm(nome, cognome, email, cf, matricola, pw, repeatPw,this.stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}