package src.MacroGestioneCredenziali;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UpdatePasswordBound {

    private String email;
    private GestionePasswordControl controller;

    public UpdatePasswordBound(String email) {
        this.email = email;
        this.controller = new GestionePasswordControl(email);
    }

    public void visualizza(Object windowContext) {
        if (windowContext instanceof Stage) {
            Stage stage = (Stage) windowContext;

            Platform.runLater(() -> {
                Scene scene = buildScene(stage);
                stage.setScene(scene);
                stage.setTitle("Piattaforma AFAM - Aggiorna Password");
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

        Text title = new Text("Aggiorna Password");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        title.setStyle("-fx-fill: #091B33;");
        grid.add(title, 0, 0, 2, 1);

        Label lblPassword = new Label("Nuova password");
        lblPassword.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        grid.add(lblPassword, 0, 1);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Inserisci nuova password");
        passwordField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10;");
        grid.add(passwordField, 1, 1);

        Label lblRipetiPassword = new Label("Ripeti password");
        lblRipetiPassword.setStyle("-fx-text-fill: #16325B; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
        grid.add(lblRipetiPassword, 0, 2);

        PasswordField ripetiPasswordField = new PasswordField();
        ripetiPasswordField.setPromptText("Ripeti nuova password");
        ripetiPasswordField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C4D1DF; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 10;");
        grid.add(ripetiPasswordField, 1, 2);

        Button btnAggiorna = new Button("Aggiorna password");
        btnAggiorna.setStyle("-fx-background-color: #091B33; -fx-text-fill: #FFFFFF; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 4; -fx-cursor: hand;");
        btnAggiorna.setOnAction(e -> {
            this.controller.sendCredentials(
                passwordField.getText(),
                ripetiPasswordField.getText(),
                stage
            );
        });
        grid.add(btnAggiorna, 1, 3);

        return new Scene(grid, 500, 400);
    }
}