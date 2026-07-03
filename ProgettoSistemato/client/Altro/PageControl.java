package client.Altro;
import java.sql.SQLException;
import java.util.List;

import client.GeneralClasses.Entities.ContenutoEntity;
import client.GeneralClasses.Entities.StudenteEntity;
import client.MacroGestioneProfilo.*;
import Server.DBMSBoundary;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PageControl {

    private HomePageBoundary homePageBound;
    private DBMSBoundary dbmsBound;
    private Stage currentStage; // <<-- Aggiunto riferimento allo Stage

    // Costruttore modificato per ricevere lo Stage (o puoi sovraccaricarlo)
    public PageControl(Stage currentStage) {
        this.dbmsBound = new DBMSBoundary(); 
        this.currentStage = currentStage; // <<-- Inizializzazione dello Stage
    }
    
    // Costruttore di fallback vuoto per non rompere altre istanze esistenti
    public PageControl() {
        this.dbmsBound = new DBMSBoundary();
    }

    public List<ContenutoEntity> getUserResources(String email) throws SQLException {
        return dbmsBound.getResources(email);
    }

    public void visualizza(){
        
          if (this.currentStage != null) {
            Platform.runLater(() -> {
                // Prende il contenitore VBox principale definito nella HomePageBoundary
                // e crea una nuova scena con le dimensioni adatte alla dashboard
                Scene homeScene = new Scene(homePageBound.getRootContainer(), 900, 700);
                
                this.currentStage.setTitle("Piattaforma AFAM - Galleria & Dashboard");
                this.currentStage.setScene(homeScene);
                this.currentStage.show();
            });
        } else {
            // Fallback se il costruttore ha usato lo stage nullo
            homePageBound.visualizza();
        }
    }

    public void createHomePageBoundary(String email) {
        try{
            List<ContenutoEntity> userResources = this.getUserResources(email);
            this.homePageBound = new HomePageBoundary(email);
            this.getUserInfo(email);
            this.visualizza();
            if (userResources != null && !userResources.isEmpty()) { 
            for (ContenutoEntity risorsa : userResources) {
                homePageBound.mostraRisorsa();
                homePageBound.caricaDatiRisorsa(risorsa);
                homePageBound.associaBottoneModifica();
                homePageBound.associaBottoneRimozione();
            }
        } else {
            homePageBound.backgroundDisplay("Nessuna risorsa è stata caricata");
        }
        }catch(SQLException e){
            e.printStackTrace();   
        }
    }

    public void getUserInfo(String email) {
        try {
            StudenteEntity studente = dbmsBound.getUserInfo(email);
            if (studente != null) {
                homePageBound.setUserInfo(studente);
            } else {
                System.out.println("Nessun utente trovato con l'email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}