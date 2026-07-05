package src.MacroGestioneCredenziali;

import javafx.application.Platform;
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

public class RecuperaPasswordBound {

    private GestionePasswordControl controller;

    public RecuperaPasswordBound() {
        this.controller = new GestionePasswordControl();
    }

    public void visualizza(Object windowContext) {
        if (windowContext instanceof Stage) {
            Stage stage = (Stage) windowContext;

            Platform.runLater(() -> {
                Scene scene = buildScene(stage);
                stage.setScene(scene);
                stage.setTitle("Piattaforma AFAM - Recupera Password");
                stage.show();
            });
        }
    }

    private Scene buildScene(Stage stage) {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #F0F4F8;");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(40, 40, 40, 40));

        Text title = new Text("Recupera Password");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        title.setStyle("-fx-fill: #091B33;");
        grid.add(title, 0, 0, 2, 1);

        Label lblInfo = new Label("Inserisci la tua email per ricevere un codice OTP.");
        lblInfo.setWrapText(true);
        lblInfo.setStyle("-fx-text-fill: #556E8A; -fx-font-family: 'Segoe UI'; -fx-font-size: 13px;");
        grid.add(lblInfo, 0, 1, 2, 1);

        Label lblEmail = new Label("Email");
        lblEmail.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        grid.add(lblEmail, 0, 2);

        TextField emailField = new TextField();
        emailField.setPromptText("inserisci la tua email");
        emailField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10;");
        grid.add(emailField, 1, 2);

        Button btnConferma = new Button("Conferma");
        btnConferma.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        btnConferma.setOnAction(e -> {
            this.controller.checkEmailNotNull(emailField.getText(), stage);
        });
        grid.add(btnConferma, 1, 3);

        Button btnIndietro = new Button("Torna al Login");
        btnIndietro.setStyle("-fx-background-color: transparent; -fx-text-fill: #12305C; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-cursor: hand;");
        btnIndietro.setOnAction(e -> {
            LoginBound loginBound = new LoginBound();
            loginBound.visualizza(stage);
        });
        grid.add(btnIndietro, 1, 4);

        return new Scene(grid, 500, 400);
    }
}