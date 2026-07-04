package client.MacroGestioneProfilo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Server.DBMSBoundary;
import client.Altro.PageControl;
import client.GeneralClasses.AlertBoundary;
import client.GeneralClasses.Entities.ContenutoEntity;
import client.GeneralClasses.Entities.StudenteEntity;

import java.util.HashMap;

public class HomePageControl {
    private HomePageBoundary hb;
    private CaricamentoFileBound cFileBound;
    private byte[] fileBlob;
    private String email;
    private DBMSBoundary db;
    private AlertBoundary ab;
    private String tipo;
    private List<ContenutoEntity> contenuti;

    public HomePageControl(String e, HomePageBoundary boundary) {
        this.email = e;
        this.hb = boundary;
        this.db = new DBMSBoundary();    // <<-- INIZIALIZZA IL DB
        this.ab = new AlertBoundary();  // <<-- INIZIALIZZA L'ALERT
    }


    public HomePageControl(String e){
        
        this.email=e;
        this.db= new DBMSBoundary();
        this.ab= new AlertBoundary();

    }

    public String getEmail() {
        
        return this.email;

    }

    public void setContenuti(List<ContenutoEntity> contenuti) {
        this.contenuti = contenuti;
    }

    public void visualizza() {
        CaricamentoFileBound cfb = new CaricamentoFileBound(this);
        this.hb.mostraPannelloCaricamentoFile(cfb);
    }

    public void trasformaInBlob(File fileSelezionato){
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

        
        public void salvaContenuto(String titolo, String descrizione,Object windowC) {
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

           
            client.Altro.PageControl pc = new client.Altro.PageControl();
            this.clickHome(windowC);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaxPosizione() throws SQLException {
        return db.getMaxPosizione(this.email);
    }

    public void aggiornaFotoProfilo(Object windowContex) {
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

            // RISOLUZIONE TRAMITE PAGECONTROL: 
            // Inizializziamo PageControl passandogli lo stage in modo che possa fare il refresh completo dal DB
            this.clickHome(windowContex);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void clickHome(Object windowContex) {
        this.createHomePageBoundary(this.email,windowContex);
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
    this.hb.mostraPannelloCaricamentoFoto(cfpb);
  }

 
  

  public void ricaricaPagina(Object context) {
      this.createHomePageBoundary(this.email,context);
      
  }

  public void salvaNuovaFotoProfilo(Object windowContext){
    this.ricaricaPagina(windowContext );

  }

  public void abilitaRiodinamentoContenuti(){

    this.hb.EnableSaveButton();
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
  

  public void salvaNuovoOrdinamento(List<ContenutoEntity> contenutiAggiornati){
    HashMap<Integer,Integer> mappaPosizioni = new HashMap<>();
    int numberOfCards = contenutiAggiornati.size();
    
    for(int i=0; i<numberOfCards; i++){
        ContenutoEntity risorsa = contenutiAggiornati.get(i);
        mappaPosizioni.put(risorsa.getId(), risorsa.getPosizione());
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
    int numberOfCards = this.hb.getNumberOfCards();
    this.hb.DisableSaveButton();
    for(int i=0; i<numberOfCards; i++){
        this.hb.impostaStatoFrecceCardSpecifici(i, false, false);
        this.hb.nascondiFrecceCardSpecifici(i);
    }

  }


public void invertiOrdineRisorse(int d, ContenutoEntity card) {
   // 1. Identifica l'indice di partenza fisso
    int currentIndex = this.hb.getResourceIndex(card);
    if (currentIndex == -1) return; // Controllo di sicurezza se la card non viene trovata
    int targetIndex = -1;

    //Determina l'indice di destinazione in base alla direzione
    if (d == 1) { // Freccia Sinistra / Su
        if (currentIndex <= 0) return; // Giò è il primo elemento, ignora
        targetIndex = currentIndex - 1;
    } else { // Freccia Destra / Giù
        if (currentIndex >= this.hb.getContenitoreCardSize() - 1) return; // Già ultimo, ignora
        targetIndex = currentIndex + 1;
    }

    // 3. Recupera la card partner con cui effettuare lo scambio
    ContenutoEntity partnerRisorsa = this.hb.getCardByIndex(targetIndex);
    if (partnerRisorsa == null) return;
    
    
    //Scambio dati logico sulle Entity (Puro BCE)
    int posIniziale = card.getPosizione();
    card.setPosizione(partnerRisorsa.getPosizione());
    partnerRisorsa.setPosizione(posIniziale);

    //Richiesta alla Boundary di effettuare lo swap atomico sulla UI
    this.hb.scambiaCardNelContenitore(currentIndex, targetIndex);
    
    // Ricalcola lo stato visivo abilitato/disabilitato/visibile di tutte le frecce
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

  public void visualizza(Object windowContext){
        this.hb.visualizzaContesto(windowContext);
    }

     public List<ContenutoEntity> getUserResources(String email) throws SQLException {
        return db.getResources(email);
   
   
     }

    public void createHomePageBoundary(String email,Object windowContext) {
        try{
            List<ContenutoEntity> userResources = this.getUserResources(email);
            this.hb = new HomePageBoundary(email);
            this.getUserInfo(email);
            this.visualizza(windowContext);
            if (userResources != null && !userResources.isEmpty()) { 
            for (ContenutoEntity risorsa : userResources) {
                hb.mostraRisorsa();
                hb.caricaDatiRisorsa(risorsa);
                hb.associaBottoneModifica();
                hb.associaBottoneRimozione();
            }
        } else {
            hb.backgroundDisplay("Nessuna risorsa è stata caricata");
        }
        }catch(SQLException e){
            e.printStackTrace();   
        }
    }


     public void getUserInfo(String email) {
        try {
            StudenteEntity studente = db.getUserInfo(email);
            if (studente != null) {
                hb.setUserInfo(studente);
            } else {
                System.out.println("Nessun utente trovato con l'email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}