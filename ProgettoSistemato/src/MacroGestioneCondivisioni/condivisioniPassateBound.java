package src.MacroGestioneCondivisioni;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.util.List;

import src.GeneralClasses.Entities.CondivisioneEntity;

public class condivisioniPassateBound {

    private SendFileControl controller;
    private Stage stage;

    public condivisioniPassateBound(SendFileControl controller) {
        this.controller = controller;
    }

    /**
     * Metodo principale invocato dalla Control per mostrare lo storico delle condivisioni.
     */
    public void visualizza(Object windowContext, List<CondivisioneEntity> listaCondivisioni) {
        VBox root = new VBox(20);
        
        if (windowContext instanceof Stage) {
            this.stage = (Stage) windowContext;
            
            root.setPadding(new Insets(0, 0, 20, 0));
            root.setStyle("-fx-background-color: #F4F7FA;");

            // --- TOOLBAR SUPERIORE CENTRATA ---
            VBox toolbar = new VBox(8);
            toolbar.setAlignment(Pos.CENTER);
            toolbar.setPadding(new Insets(20, 40, 20, 40));
            toolbar.setStyle("-fx-background-color: #091B33;");

            Label titolo = new Label("Storico Condivisioni");
            titolo.setStyle("-fx-font-family: 'Segoe UI', Helvetica, Arial; -fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label descrizione = new Label("Visualizza lo stato dei file inviati ed esamina i riscontri di apertura.");
            descrizione.setStyle("-fx-font-family: 'Segoe UI', Arial; -fx-font-size: 14px; -fx-text-fill: #A0B2C6;");

            toolbar.getChildren().addAll(titolo, descrizione);

            // --- CONTENITORE DELLE CARD (LISTA) ---
            VBox cardsContainer = new VBox(14);
            cardsContainer.setPadding(new Insets(10, 10, 10, 10)); // Aggiunto padding interno destro/sinistro
            cardsContainer.setAlignment(Pos.TOP_CENTER); // Forza l'allineamento dall'alto
            cardsContainer.setStyle("-fx-background-color: transparent;");

            if (listaCondivisioni == null || listaCondivisioni.isEmpty()) {
                Label vuoto = new Label("Nessuna condivisione passata presente nel sistema.");
                vuoto.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 15px; -fx-text-fill: #78909C; -fx-font-style: italic;");
                cardsContainer.getChildren().add(vuoto);
            } else {
                for (CondivisioneEntity cond : listaCondivisioni) {
                    cardsContainer.getChildren().add(creaCardCondivisione(cond));
                }
            }

            ScrollPane scrollPane = new ScrollPane(cardsContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true); // <-- FONDAMENTALE: Forza lo ScrollPane a riempire l'altezza
            scrollPane.setPadding(new Insets(10, 30, 10, 30));
            // Rimossa la trasparenza dello sfondo del pannello per evitare bug grafici di rendering su alcuni OS
            scrollPane.setStyle("-fx-background-color: #F4F7FA; -fx-viewport-background-color: #F4F7FA; -fx-border-color: transparent;");
            
            // Permette allo scrollPane di espandersi verticalmente occupando tutto lo spazio tra toolbar e bottomBar
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            
            // --- BOTTONE DI RITORNO ALLA HOME ---
            Button btnIndietro = new Button("← Torna alla Home");
            btnIndietro.setStyle("-fx-background-color: #091B33; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 10 20; -fx-cursor: hand;");
            
            btnIndietro.setOnMouseEntered(e -> btnIndietro.setStyle("-fx-background-color: #112D52; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 10 20; -fx-cursor: hand;"));
            btnIndietro.setOnMouseExited(e -> btnIndietro.setStyle("-fx-background-color: #091B33; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 10 20; -fx-cursor: hand;"));
            
            btnIndietro.setOnAction(e -> {
                Window currWindow = btnIndietro.getScene().getWindow();
                // Si assume che il controller gestisca il rientro alla Home recuperando l'email corrente
                controller.sendHome("", currWindow); 
            });

            HBox bottomBar = new HBox(btnIndietro);
            bottomBar.setAlignment(Pos.CENTER_LEFT);
            bottomBar.setPadding(new Insets(10, 30, 0, 30));

            root.getChildren().addAll(toolbar, scrollPane, bottomBar);
            stage.setScene(new Scene(root, 950, 650));
        }
    }

    /**
     * Costruisce graficamente la singola riga (card) associata a una condivisione.
     */
private HBox creaCardCondivisione(CondivisioneEntity cond) {
        HBox card = new HBox(20);
        // ... (resto del tuo codice per lo stile della card)

        // 1. Dettagli Oggetto e Email del Destinatario
        VBox infoDestinatario = new VBox(4);
        infoDestinatario.setMinWidth(250); // <-- AGGIUNGI QUESTO per dare spazio al testo dell'email
        
        Label lblEmail = new Label(cond.getEmailAssociata()); 
        lblEmail.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #091B33;");
        
        infoDestinatario.getChildren().addAll(lblEmail);
        HBox.setHgrow(infoDestinatario, Priority.ALWAYS);

        // 2. Zona Stato Dinamica (Verde se dataCondivisione non è null)
        VBox statoBox = new VBox(4);
        statoBox.setAlignment(Pos.CENTER);
        statoBox.setPadding(new Insets(6, 14, 6, 14));
        statoBox.setPrefWidth(210);
        
        if (cond.getDataCondivisione() != null) {
            // Successo: Visualizzato/Aperto dal destinatario
            statoBox.setStyle("-fx-background-color: #E8F8F5; -fx-background-radius: 6px; -fx-border-color: #2ECC71; -fx-border-radius: 6px; -fx-border-width: 1px;");
            Label lblStato = new Label("✓ Visualizzato");
            lblStato.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #27AE60;");
            
            Label lblData = new Label(cond.getDataCondivisione().toString());
            lblData.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11px; -fx-text-fill: #27AE60;");
            statoBox.getChildren().addAll(lblStato, lblData);
        } else {
            // In attesa di prima visualizzazione
            statoBox.setStyle("-fx-background-color: #FDF2E9; -fx-background-radius: 6px; -fx-border-color: #E67E22; -fx-border-radius: 6px; -fx-border-width: 1px;");
            Label lblStato = new Label("⟳ Non ancora aperto");
            lblStato.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #D35400;");
            statoBox.getChildren().addAll(lblStato);
        }

        // 3. Pulsante di Reindirizzamento a Dropbox
        Button btnVisualizza = new Button("Link Dropbox");
        btnVisualizza.setStyle("-fx-background-color: #556E8A; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-padding: 8 14; -fx-cursor: hand;");
        btnVisualizza.setOnMouseEntered(e -> btnVisualizza.setStyle("-fx-background-color: #43586F; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-padding: 8 14; -fx-cursor: hand;"));
        btnVisualizza.setOnMouseExited(e -> btnVisualizza.setStyle("-fx-background-color: #556E8A; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-padding: 8 14; -fx-cursor: hand;"));
        
        btnVisualizza.setOnAction(e -> {
            // Nota: Se la Control non dispone di un metodo di inoltro al browser, 
            // è possibile aprirlo direttamente tramite java.awt.Desktop se supportato,
            // oppure delegando al rispettivo servizio esterno.
            try {
                if (java.awt.Desktop.isDesktopSupported() && cond.getLinkEsterno() != null) {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(cond.getLinkEsterno()));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // 4. Pulsante di Rimozione
        Button btnRimuovi = new Button("Rimuovi");
        btnRimuovi.setStyle("-fx-background-color: #E04545; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-padding: 8 14; -fx-cursor: hand;");
        btnRimuovi.setOnMouseEntered(e -> btnRimuovi.setStyle("-fx-background-color: #C63434; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-padding: 8 14; -fx-cursor: hand;"));
        btnRimuovi.setOnMouseExited(e -> btnRimuovi.setStyle("-fx-background-color: #E04545; -fx-text-fill: white; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-padding: 8 14; -fx-cursor: hand;"));
        
        btnRimuovi.setOnAction(e -> {
            // Collegamento per la rimozione logica o fisica dell'elemento della condivisione
            System.out.println("Rimozione richiesta per la condivisione ID: " + cond.getId());
            // Se necessario, implementare il metodo di eliminazione all'interno di SendFileControl
        });

        card.getChildren().addAll(infoDestinatario, statoBox, btnVisualizza, btnRimuovi);
        return card;
    }
}