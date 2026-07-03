package client.GeneralClasses;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBoundary {

    public void alert(String messaggio) {
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Piattaforma AFAM - Avviso");
        window.setMinWidth(340);

        // Testo di notifica (Mantenuto rosso acceso ma pulito per semantic error)
        Text text = new Text(messaggio);
        text.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        text.setFill(Color.web("#C92A2A")); 
        text.setWrappingWidth(280);

        Button closeButton = new Button("OK");
        // Stile bottone primario blu scuro
        closeButton.setStyle("-fx-background-color: #12305C; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 25; -fx-background-radius: 4; -fx-cursor: hand;");
        
        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #0A1C3A; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: #12305C; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-padding: 8 25; -fx-background-radius: 4; -fx-cursor: hand;"));
        
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(20);
        layout.setStyle("-fx-background-color: #F0F4F8;"); // Sfondo grigio-blu freddo e chiaro
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(text, closeButton);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}