package client.MacroGestioneProfilo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import Server.DBMSBoundary;
import client.Altro.PageControl;
import client.GeneralClasses.AlertBoundary;
import client.GeneralClasses.Entities.ContenutoEntity;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePageControl {
    private HomePageBoundary hb;
    private CaricamentoFileBound cFileBound;
    private byte[] fileBlob;
    private String email;
    private DBMSBoundary db;
    private AlertBoundary ab;
    private String tipo;
    private VBox rootContainer;

    public HomePageControl(String e, HomePageBoundary boundary) {
        this.email = e;
        this.hb = boundary;
        this.db = new DBMSBoundary();    // <<-- INIZIALIZZA IL DB
        this.ab = new AlertBoundary();  // <<-- INIZIALIZZA L'ALERT
    }
    public String getEmail() {
        return this.email;
    }

    public void visualizza() {
        this.cFileBound = new CaricamentoFileBound(this);
        VBox caricamento=this.cFileBound.visualizza();
        rootContainer = hb.getRootContainer();
        
        // Rimuoviamo la vecchia area delle risorse (indice 1) e inseriamo la form di caricamento
       rootContainer.getChildren().clear(); 
        rootContainer.getChildren().add(caricamento);
        
    }

    public void mostraFormDatiContenuto(File fileSelezionato){
         if (fileSelezionato != null) {
            try {
                this.fileBlob = Files.readAllBytes(fileSelezionato.toPath()); // Salva nei dati interni del Control
                String mimeType = Files.probeContentType(fileSelezionato.toPath());
                this.tipo = mimeType != null ? mimeType : "application/octet-stream"; 
                System.out.println("MimeType rilevato: " + this.tipo);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

        
        public void salvaContenuto(String titolo, String descrizione) {
        if (this.fileBlob == null) {
            this.ab.alert("Errore: Seleziona prima un file d'arte!");
            return;
        }
        try {
            int maxPosizione = this.getMaxPosizione()+1;
            this.db.inserisciContenuto(this.fileBlob, titolo, descrizione, this.tipo, this.email,maxPosizione);
            this.ab.alert("Contenuto salvato con successo!");
            this.fileBlob = null;
            this.tipo = null;

            // PRENDIAMO LO STAGE ATTUALE
            Stage currentStage = (Stage) this.rootContainer.getScene().getWindow();

            // RISOLUZIONE TRAMITE PAGECONTROL: 
            // Inizializziamo PageControl passandogli lo stage in modo che possa fare il refresh completo dal DB
            client.Altro.PageControl pc = new client.Altro.PageControl(currentStage);
            this.clickHome(pc);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaxPosizione() throws SQLException {
        return db.getMaxPosizione(this.email);
    }
    public void aggiornaFotoProfilo() {
        if (this.fileBlob == null) {
            this.ab.alert("Errore: Seleziona prima un'immagine profilo!");
            return;
        }
        try {
            this.db.aggiornaFotoProfilo(this.fileBlob, this.email);
            this.ab.alert("Foto profilo aggiornata con successo!");
            this.fileBlob = null;
            this.tipo = null;

            // PRENDIAMO LO STAGE ATTUALE
            Stage currentStage = (Stage) this.rootContainer.getScene().getWindow();

            // RISOLUZIONE TRAMITE PAGECONTROL: 
            // Inizializziamo PageControl passandogli lo stage in modo che possa fare il refresh completo dal DB
            client.Altro.PageControl pc = new client.Altro.PageControl(currentStage);
            this.clickHome(pc);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void clickHome(PageControl pc) {

        pc.createHomePageBoundary(this.email);


    }
    public void setStage(Stage stage) {
        this.rootContainer = (VBox) stage.getScene().getRoot();
    }


    public void apriDocumento(ContenutoEntity risorsa) {
    if (risorsa == null || risorsa.getTipo() == null) {
        this.ab.alert("Errore: Risorsa non valida o tipo non specificato.");
        return;
    }

    String mimeType = risorsa.getTipo().toLowerCase();

    try {
        File tempFile = null;

        // Gestione PDF
        if (mimeType.equals("application/pdf")) {
            tempFile = File.createTempFile("opera_", ".pdf");
        } 
        // NUOVO: Gestione Video
        else if (mimeType.startsWith("video/")) {
            // Estraiamo la sotto-estensione (es. mp4, mkv) o usiamo mp4 di fallback
            String estensione = mimeType.contains("/") ? "." + mimeType.split("/")[1] : ".mp4";
            tempFile = File.createTempFile("video_artistico_", estensione);
        } 
        // NUOVO: Gestione Audio
        else if (mimeType.startsWith("audio/")) {
            String estensione = mimeType.contains("/") ? "." + mimeType.split("/")[1] : ".mp3";
            tempFile = File.createTempFile("audio_artistico_", estensione);
        }
        // Gestione Testo
        else if (mimeType.startsWith("text/")) {
            tempFile = File.createTempFile("testo_", ".txt");
        } 
        // Gestione di altri formati generici
        else {
            String estensione = mimeType.contains("/") ? "." + mimeType.split("/")[1] : ".bin";
            tempFile = File.createTempFile("allegato_", estensione);
        }

        // Se è stato generato un file temporaneo, vi scriviamo i byte e lo lanciamo
        if (tempFile != null) {
            tempFile.deleteOnExit(); // Viene rimosso alla chiusura dell'app
            
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(risorsa.getFile());
            }
            
            // Apertura nativa tramite xdg-open (ottimale per Linux)
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec(new String[]{"xdg-open", tempFile.getAbsolutePath()});
            } else {
                Runtime.getRuntime().exec(new String[]{"xdg-open", tempFile.getAbsolutePath()});
            }
        }

    } catch (IOException ex) {
        ex.printStackTrace();
        this.ab.alert("Impossibile riprodurre o aprire il file multimediale su questo sistema.");
    }
  }

  public void createCaricaFotoProfiloBound() {
    CaricaFotoProfiloBound cfpb = new CaricaFotoProfiloBound(this);
    mostraCaricamentoImmagine(cfpb);
  }

  public void mostraCaricamentoImmagine(CaricaFotoProfiloBound cfpb){ 
    VBox caricamentoFoto = cfpb.visualizza();
    rootContainer.getChildren().clear();
    rootContainer.getChildren().add(caricamentoFoto);
  }

  public void nascondiCaricamentoImmagine(){

  }

  public void ricaricaPagina(PageControl pc) {
      pc.createHomePageBoundary(this.email);
  }

  public void salvaNuovaFotoProfilo(){


  }

  public void abilitaRiodinamentoContenuti(){

    int numberOfCards = this.hb.getNumberOfCards();
    for(int i=0; i<numberOfCards; i++){
        if(i==0 )
            this.hb.impostaStatoFrecceCardSpecifici(i, false, true);
        else if(i==numberOfCards-1)
            this.hb.impostaStatoFrecceCardSpecifici(i, true, false);
        else
            this.hb.impostaStatoFrecceCardSpecifici(i, true, true);
    }

  }

  public void salvaNuovoOrdinamento(){
    HashMap<Integer,Integer> mappaPosizioni = new HashMap<>();
    FlowPane resourcesContainer = this.hb.getResourcesContainer();
    int numberOfCards = resourcesContainer.getChildren().size();
    List<ContenutoEntity> contenutiAggiornati = new ArrayList<>();
    for(int i=0; i<numberOfCards; i++){
        VBox card = (VBox) resourcesContainer.getChildren().get(i);
        ContenutoEntity risorsa = (ContenutoEntity) card.getUserData();
        mappaPosizioni.put(risorsa.getId(), risorsa.getPosizione());
        contenutiAggiornati.add(risorsa);
    }

    try {
        this.db.aggiornaPosizioniContenuti(mappaPosizioni);
        this.ab.alert("Nuovo ordinamento salvato con successo!");
        this.disabilitaRiodinamentoContenuti();
    } catch (SQLException e) {
        e.printStackTrace();
        
    }
    

  }

  public void disabilitaRiodinamentoContenuti(){

  }


public void invertiOrdineRisorse(int d, VBox card) {
   // 1. Identifica l'indice di partenza fisso
    int currentIndex = this.hb.getResourcesContainer().getChildren().indexOf(card);
    if (currentIndex == -1) return; // Controllo di sicurezza se la card non viene trovata

    ContenutoEntity risorsa = (ContenutoEntity) card.getUserData();
    int targetIndex = -1;

    // 2. Determina l'indice di destinazione in base alla direzione
    if (d == 1) { // Freccia Sinistra / Su
        if (currentIndex <= 0) return; // Giò è il primo elemento, ignora
        targetIndex = currentIndex - 1;
    } else { // Freccia Destra / Giù
        if (currentIndex >= this.hb.getResourcesContainer().getChildren().size() - 1) return; // Già ultimo, ignora
        targetIndex = currentIndex + 1;
    }

    // 3. Recupera la card partner con cui effettuare lo scambio
    VBox partnerCard = this.hb.getCardByIndex(targetIndex);
    if (partnerCard == null) return;
    ContenutoEntity partnerRisorsa = (ContenutoEntity) partnerCard.getUserData();

    // 4. Scambio dati logico sulle Entity (Puro BCE)
    int posIniziale = risorsa.getPosizione();
    risorsa.setPosizione(partnerRisorsa.getPosizione());
    partnerRisorsa.setPosizione(posIniziale);

    // 5. Richiesta alla Boundary di effettuare lo swap atomico sulla UI
    this.hb.scambiaCardNelContenitore(currentIndex, targetIndex);

    // 6. Ricalcola lo stato visivo abilitato/disabilitato/visibile di tutte le frecce
    this.abilitaRiodinamentoContenuti();
}


  public void cercaUtente(){

  }

  public void createPublicContentBound(){

  }

  public void visualizzaUtente(){

  }

  public void requestPublicContent(){

  }

  public void caricaContenuto(){

  }

  public void cancellaNomeInserito(){
    
  }




}