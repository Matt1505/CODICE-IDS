package src.MacroGestioneContenuti;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import src.repository.DBMSBoundary;
import src.GeneralClasses.AlertBoundary;
import src.GeneralClasses.Entities.ContenutoEntity;
import src.GeneralClasses.Entities.StudenteEntity;

import java.util.HashMap;

import src.MacroGestioneProfilo.GestioneProfiloBound;
import src.MacroGestioneCredenziali.LoginBound;
import src.MacroGestioneCredenziali.UpdatePasswordBound;

public class HomePageControl {
    private HomePageBoundary hb;
    private CaricamentoFileBound cFileBound;
    private byte[] fileBlob;
    private byte[] fotoBlob;
    private String tipoFoto;
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

       public void trasformaInBlobFotoProfilo(File fileSelezionato){
         if (fileSelezionato != null) {
            try {
                this.fotoBlob = Files.readAllBytes(fileSelezionato.toPath()); // Salva nei dati interni del Control
                String mimeType = Files.probeContentType(fileSelezionato.toPath());
                this.tipoFoto = mimeType != null ? mimeType : "application/octet-stream"; 
                System.out.println("MimeType rilevato: " + this.tipo);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
        
    public void salvaContenuto(String titolo, String descrizione, boolean pubblico, Object windowC) {
        if (this.fileBlob == null) {
        this.ab.alert("Errore: Seleziona prima un file d'arte!");
        return;
    }   

    try {
        int maxPosizione = this.getMaxPosizione() + 1;

        this.db.inserisciContenuto(
            this.fileBlob,
            titolo,
            descrizione,
            this.tipo,
            this.email,
            maxPosizione,
            pubblico
        );

        this.ab.alert("Contenuto salvato con successo!");
        this.fileBlob = null;
        this.tipo = null;

        this.tornaHome(windowC);

    } catch (SQLException e) {
        e.printStackTrace();
        this.ab.alert("Errore durante il salvataggio del contenuto.");
    }
}

    public int getMaxPosizione() throws SQLException {
        return db.getMaxPosizione(this.email);
    }

    public void aggiornaFotoProfilo(Object windowContex) {
        if (this.fotoBlob == null) {
            this.ab.alert("Errore: Seleziona prima un'immagine profilo!");
            return;
        }
        try {
            this.db.aggiornaFotoProfilo(this.fotoBlob, this.email);
            this.ab.alert("Foto profilo aggiornata con successo!");
            this.fotoBlob = null;
            this.tipoFoto = null;
            this.tornaHome(windowContex);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void tornaHome(Object windowContex) {
        this.createHomePageBoundary(this.email,windowContex);
    }

    


    public void apriDocumento(ContenutoEntity risorsa,Object windowContext) {
    if (risorsa == null || risorsa.getTipo() == null) {
        this.ab.alert("Errore: Risorsa non valida o tipo non specificato.");
        return;
    }

    VisualizzaContenutoBound visualizzaContenutoBound = new VisualizzaContenutoBound(this, risorsa);
    visualizzaContenutoBound.visualizza(windowContext);
    }   

  public void createGestioneProfiloBound() {
    GestioneProfiloBound gestioneProfiloBound = new GestioneProfiloBound(this);
    this.mostraGestioneProfilo(gestioneProfiloBound);
  }

  public void createCaricaGestioneProfiloBound() {
    this.createGestioneProfiloBound();
  }

  public void createCaricaFotoProfiloBound() {
    this.createGestioneProfiloBound();
  }

  public void mostraGestioneProfilo(GestioneProfiloBound gestioneProfiloBound){ 
    this.hb.mostraPannelloGestioneProfilo(gestioneProfiloBound);
  }

 
  

  public void ricaricaPagina(Object context) {
      this.createHomePageBoundary(this.email,context);
      
  }

  public void salvaNuovaFotoProfilo(Object windowContext){
    this.ricaricaPagina(windowContext );
  }

  public void abilitaRiodinamentoContenuti(){
    this.hb.disableCondividiContenuti();
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
    this.hb.disabilitaInterfacciaRiordinamento();
    for(int i=0; i<numberOfCards; i++){
        ContenutoEntity risorsa = contenutiAggiornati.get(i);
        mappaPosizioni.put(risorsa.getId(), risorsa.getPosizione());
    }

    try {
        this.db.aggiornaPosizioniContenuti(mappaPosizioni);
        this.ab.alert("Nuovo ordinamento salvato con successo!");
        this.disabilitaRiodinamentoContenuti();
        this.hb.enableCondividiContenuti();
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


  public void cercaUtente(String query) {
    if (query == null || query.trim().isEmpty()) {
        this.hb.pulisciRisultatiRicercaUtenti();
        this.ab.alert("Inserisci un nome e cognome per la ricerca.");
        return;
    }

    try {
        ArrayList<StudenteEntity> risultati = this.db.cerca(query.trim(), this.email);

        System.out.println("Risultati trovati: " + risultati.size());

        if (risultati.isEmpty()) {
            this.hb.pulisciRisultatiRicercaUtenti();
            this.ab.alert("Nessun utente trovato.");
            return;
        }

        this.hb.mostraListaStudenti(risultati);

    } catch (SQLException e) {
        e.printStackTrace();
        this.ab.alert("Errore durante la ricerca degli utenti.");
    }
}

public void createPublicContentBound(StudenteEntity studente) {
    PublicContentBound publicContentBound = new PublicContentBound(this, studente);

    this.hb.mostraPublicContentBound(publicContentBound);

    this.requestPublicContent(studente);
}

public void visualizzaStudente(StudenteEntity studente) {
    if (studente == null) {
        this.ab.alert("Studente non valido.");
        return;
    }

    this.createPublicContentBound(studente);
}

public void requestPublicContent(StudenteEntity studente) {
    List<ContenutoEntity> contentList = this.db.getPublicResources(studente.getEmail());

    if (contentList == null || contentList.isEmpty()) {
        this.hb.mostraListaContenutiPubblici(contentList);
        return;
    }

    this.hb.mostraListaContenutiPubblici(contentList);
}

public void caricaContenuto(ContenutoEntity contenuto) {
    this.hb.caricaContenutoPubblico(contenuto);
}

public void cancellaNomeInserito() {
    this.hb.pulisciRisultatiRicercaUtenti();
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
            ab.alert("Errore di sistema, premi OK per riprovare");
            createHomePageBoundary(email, windowContext);
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
    public void logout(Object windowContext) {
        LoginBound loginBound = new LoginBound();
loginBound.visualizza(windowContext);
        loginBound.visualizza(windowContext);
    }
    public String getDescrizioneProfilo() {
    try {
        StudenteEntity studente = this.db.getUserInfo(this.email);

        if (studente != null && studente.getDescrizione() != null) {
            return studente.getDescrizione();
        }

    } catch (SQLException e) {
        e.printStackTrace();
        this.ab.alert("Errore durante il recupero della descrizione profilo.");
    }

    return "";
}

public void aggiornaDescrizioneProfilo(String descrizione, Object windowContext) {
    try {
        this.db.aggiornaDescrizioneProfilo(this.email, descrizione);
        this.ab.alert("Bio aggiornata con successo!");
        this.tornaHome(windowContext);
    } catch (SQLException e) {
        e.printStackTrace();
        this.ab.alert("Errore durante l'aggiornamento della descrizione profilo.");
    }
}

    public byte[] getFotoProfilo() {
        return this.db.getProfilePicture(this.email);
    }
    public void avviaEliminazioneContenuto(ContenutoEntity contenuto, Object windowContext) {
    if (contenuto == null) {
        this.ab.alert("Errore: contenuto non valido.");
        return;
    }

    boolean confermato = this.ab.conferma("Sei sicuro di volere rimuovere questa risorsa?");

    if (confermato) {
        this.richiediEliminazioneContenuto(contenuto, windowContext);
    } else {
        this.annullaEliminazione();
    }
}

public void richiediEliminazioneContenuto(ContenutoEntity contenuto, Object windowContext) {
    try {
        this.db.eliminaContenuto(contenuto.getId(), this.email);

        this.ab.alert("Risorsa rimossa con successo");

        this.ricaricaPagina(windowContext);

    } catch (SQLException e) {
        e.printStackTrace();
        this.ab.alert("Errore durante l'eliminazione della risorsa.");
    }
}

public void annullaEliminazione() {
    System.out.println("Eliminazione annullata dall'utente.");
}

 
public void sendToModifica(ContenutoEntity contenuto,Object root) {
    if (contenuto == null) {
        this.ab.alert("Errore: contenuto non valido.");
        return;
    }

    ModificaBound modificaBound = new ModificaBound(this, contenuto);
    modificaBound.mostraModificaBound(root);
}

public void salvaModificheContenuto(ContenutoEntity contenuto, File file, String titolo, String descrizione, Object windowContext) {
    if (contenuto == null) {
        this.ab.alert("Errore: contenuto non valido.");
        return;
    }

    if (titolo == null || titolo.trim().isEmpty()) {
        this.ab.alert("Errore: il titolo è obbligatorio.");
        return;
    }

    this.salvaModifiche(contenuto, file, titolo.trim(), descrizione, windowContext);
}

public void salvaModifiche(ContenutoEntity contenuto, File file, String titolo, String descrizione, Object windowContext) {
    try {
        byte[] fileBlob = null;
        String tipo = null;

        if (file != null) {
            fileBlob = Files.readAllBytes(file.toPath());
            tipo = Files.probeContentType(file.toPath());

            if (tipo == null) {
                tipo = "application/octet-stream";
            }

            System.out.println("Nuovo MimeType rilevato: " + tipo);
        }

        this.db.modifica(
            contenuto.getId(),
            this.email,
            fileBlob,
            titolo,
            descrizione,
            tipo
        );

        this.ab.alert("Contenuto modificato con successo!");

        this.tornaHome(windowContext);

    } catch (IOException e) {
        e.printStackTrace();
        this.ab.alert("Errore durante la lettura del nuovo file.");
    } catch (Exception e) {
        e.printStackTrace();
        this.ab.alert("Errore durante la modifica del contenuto:\n" + e.getMessage());
    }
}

}