package src.gestioneCredenziali;

import javafx.application.Application;
import javafx.application.Platform;
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

public class LoginBound extends Application {

    private GridPane grid;
    private LoginControl controller;
    private RegisterControl rc;
    private Stage stage;

    public LoginBound() {
        this.controller = new LoginControl();
        this.rc = new RegisterControl();
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.visualizza(primaryStage);
    }

    public Scene getScene(Stage stage) {
        this.stage = stage;
        this.controller = new LoginControl();
        this.rc = new RegisterControl();

        this.grid = new GridPane();
        this.grid.setAlignment(Pos.CENTER);
        this.grid.setHgap(15);
        this.grid.setVgap(15);
        this.grid.setPadding(new Insets(40, 40, 40, 40));

        mostraFormLogin();

        return new Scene(this.grid, 500, 500);
    }

    private void mostraFormLogin() {
        this.grid.setStyle("-fx-background-color: #F0F4F8;");

        Text scenetitle = new Text("Piattaforma AFAM\nAccesso Area Artisti");
        scenetitle.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        scenetitle.setStyle("-fx-fill: #091B33;");
        this.grid.add(scenetitle, 0, 0, 2, 1);

        Label lblEmail = new Label("Email");
        lblEmail.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px;");
        this.grid.add(lblEmail, 0, 3);

        TextField emailField = new TextField();
        emailField.setPromptText("inserisci la tua email istituzionale");
        emailField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-pref-width: 250;");
        this.grid.add(emailField, 1, 3);

        Label lblPw = new Label("Password");
        lblPw.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px;");
        this.grid.add(lblPw, 0, 5);

        PasswordField pwField = new PasswordField();
        pwField.setPromptText("inserisci la tua password");
        pwField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-pref-width: 250;");
        this.grid.add(pwField, 1, 5);

        Button btn = new Button("LOGIN");
        btn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #12305C; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;"));

        Button btnRegistrati = new Button("Nuovo Artista? Registrati");
        btnRegistrati.setStyle("-fx-background-color: transparent; -fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 0;");

        btnRegistrati.setOnMouseEntered(e -> btnRegistrati.setStyle("-fx-background-color: transparent; -fx-text-fill: #091B33; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 0; -fx-underline: true;"));
        btnRegistrati.setOnMouseExited(e -> btnRegistrati.setStyle("-fx-background-color: transparent; -fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 0;"));

        btnRegistrati.setOnAction(e -> this.nuovoAccount());

        HBox hbBtn = new HBox(15);
        hbBtn.setAlignment(Pos.CENTER_RIGHT);
        hbBtn.getChildren().addAll(btnRegistrati, btn);

        this.grid.add(hbBtn, 0, 7, 2, 1);

        Button btnRecuperaPassword = new Button("Password dimenticata?");
        btnRecuperaPassword.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #12305C;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;"
        );

        btnRecuperaPassword.setOnMouseEntered(e -> btnRecuperaPassword.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #1A4073;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-weight: bold;" +
            "-fx-underline: true;" +
            "-fx-cursor: hand;"
        ));

        btnRecuperaPassword.setOnMouseExited(e -> btnRecuperaPassword.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #12305C;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;"
        ));

        btnRecuperaPassword.setOnAction(e -> {
            this.controller.showRecuperoPasswordPage(btnRecuperaPassword.getScene().getWindow());
        });

        this.grid.add(btnRecuperaPassword, 1, 8);

        btn.setOnAction(e -> {
            sendCredentials(
                emailField.getText(),
                pwField.getText()
            );
        });
    }

    private void nuovoAccount() {
        this.rc.createRegisterBoundary(this.stage);
    }

    private void sendCredentials(String email, String pw) {
        this.controller.checkEmptyForm(email, pw, this.stage);
    }

    public void visualizza(Object windowContext) {
        if (windowContext instanceof Stage) {
            this.stage = (Stage) windowContext;

            Platform.runLater(() -> {
                this.stage.setTitle("Piattaforma AFAM - Accedi");

                this.controller = new LoginControl();
                this.rc = new RegisterControl();

                this.grid = new GridPane();
                this.grid.setAlignment(Pos.CENTER);
                this.grid.setHgap(15);
                this.grid.setVgap(15);
                this.grid.setPadding(new Insets(45, 45, 45, 45));

                mostraFormLogin();

                Scene scene = new Scene(this.grid, 500, 500);
                this.stage.setScene(scene);
                this.stage.show();
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}