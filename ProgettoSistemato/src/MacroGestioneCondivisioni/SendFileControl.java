package src.MacroGestioneCondivisioni;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import src.GeneralClasses.AlertBoundary;
import src.GeneralClasses.Entities.CondivisioneEntity;
import src.GeneralClasses.Entities.ContenutoEntity;
import src.GeneralClasses.Entities.StudenteEntity;
import src.MacroGestioneContenuti.HomePageBoundary;
import src.repository.DBMSBoundary;
import src.repository.cloudServiceBound;
import src.externalServices.ExternalSharingBoundary;
import src.externalServices.mailServerBound;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.util.UUID;

import com.sun.net.httpserver.HttpExchange;

public class SendFileControl {

    
    private HomePageBoundary hb;
    private StudenteEntity se;
    private DBMSBoundary db;
    private List<ContenutoEntity> content;
    private List<CondivisioneEntity> condivisioniPassate;
    private List<String> destinatariList;
    private mailServerBound mb;
    private cloudServiceBound cb = new cloudServiceBound();
    private ExternalSharingBoundary eb = new ExternalSharingBoundary(this);
    private AlertBoundary ab= new AlertBoundary();
    private condivisioniPassateBound cpastBound;
    private String email;
    private String nome;
    private String cognome;
    // Unifichiamo la porta su 8081 per evitare conflitti con la 8080
    private static final String PORTA_SERVER = "8081";

    public SendFileControl(HomePageBoundary hb,String email) {
        this.hb = hb;
        this.nome=nome;
        this.cognome=cognome;
        this.email=email;
        this.db = new DBMSBoundary();
        this.mb = new mailServerBound();
        this.cpastBound= new condivisioniPassateBound(this);
        try {
            eb.avviaServer();
        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione del server HTTP:");
            e.printStackTrace();
        }
    }

   

    public List<String> estraiDestinatari(String emailString) {
        List<String> listaDestinatari = new ArrayList<>();
        if (emailString == null || emailString.trim().isEmpty()) {
            return listaDestinatari;
        }
        String[] emailArray = emailString.split("[,;\\s]+");
        for (String email : emailArray) {
            String emailPulita = email.trim();
            if (!emailPulita.isEmpty()) {
                listaDestinatari.add(emailPulita);
            }
        }
        return listaDestinatari;
    }

    public void abilitaSelezione() {
        this.hb.enableSendButton();
        this.hb.abilitaInterfacciaRiordinamento();
        int numberOfCards = this.hb.getNumberOfCards();
        for (int i = 0; i < numberOfCards; i++) {
            this.hb.abilitaContentCheckBox(i);
        }
    }

    public void disabilitaSelezione() {
        this.hb.disableSendButton();
        this.hb.abilitaInterfacciaRiordinamento();
        int numberOfCards = this.hb.getNumberOfCards();
        for (int i = 0; i < numberOfCards; i++) {
            this.hb.disabilitaContentCheckBox(i);
        }
    }

    public void setDestinatari(String d) {
        this.destinatariList = this.estraiDestinatari(d);
        System.out.println(this.destinatariList);
    }

    public void createResumePageBound(List<ContenutoEntity> elementi, Object windowContext, String email) {
        this.content = elementi;
        try {
            se = db.getUserInfo(email);
            shareContentBound sc = new shareContentBound(elementi, se.getEmail(), this);
            sc.visualizza(windowContext);
        } catch (SQLException e) {
            e.printStackTrace();
            ab.alert("Errore di sistema, premi OK per riprovare");
            this.createResumePageBound(elementi, windowContext, email);
        }
    }

    public void sendHome(String email, Object window) {
        hb.visualizzaContesto(window);
        this.disabilitaSelezione();
    }

    public boolean checkCondivisioneUnivoca(String s, String dest) throws SQLException{
        return db.esisteGiaCondivisione(dest, s);

    }

    public void mandaSelezioneContenuti(Object window) {
        // Avvia il thread standard Java in background per l'I/O pesante
        new Thread(() -> {
            try {
                // 1. Caricamento file su Dropbox (operazione lenta)
                String extlink = cb.caricaSelezioneContenuti(this.content);
                if (extlink == null) {
                    System.err.println("Errore: Caricamento su Dropbox fallito.");
                    return;
                }
                try{

                
                // 2. Invio email sequenziali ai destinatari (operazione lenta)
                for (String dest : destinatariList) {
                    
                        if (checkCondivisioneUnivoca(dest, extlink)) {
                            continue;
                        }
                        String token = this.generaToken(dest);
                        String linkLocale = this.creaLinkDalToken(token);    

                        this.mandaCorrispondenza(dest, linkLocale, extlink);
                        this.mandaLinkInternoViaMail(dest, linkLocale);
                    
                }
            } catch (SQLException e) {
                        e.printStackTrace();
                        ab.alert("Errore di sistema, premi OK per riprovare");
                        this.mandaSelezioneContenuti(window);
            }
            } finally {
                // 3. Blocco FINALLY: Qualunque sia l'esito (successo o errore), riporta l'utente alla Home
                // Questo distruggerà la scena di shareContentBound e di conseguenza anche la sua ruota di caricamento
                if (se != null) {
                    this.sendHome(se.getEmail(), window);
                } else {
                    this.sendHome("", window);
                }
            }
        }).start();
    }

    public String generaToken(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email non può essere vuota.");
        }
        String uuidCasuale = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        String stringaBase = email + uuidCasuale + timestamp;
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(stringaBase.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return (uuidCasuale + timestamp).replace("-", "");
        }
    }

    public String creaLinkDalToken(String token) {
        String ip = "192.168.1.111";
        return "http://" + ip + ":" + PORTA_SERVER + "/share?token=" + token;
    }

    public void mandaCorrispondenza(String email, String link, String extLink) {
        try {
            this.db.salvaCorrispondenza(email, link, extLink, se.getEmail());
        } catch (SQLException e) {

            e.printStackTrace();
            ab.alert("Errore di sistema, premi OK per riprovare");
                this.mandaCorrispondenza(email, link, extLink);

        }
    }




    public void mandaLinkInternoViaMail(String dest, String link) {
        mb.mailCondivisione(this.email,dest, link);
    }

    public void verificaEsistenzaCondivisione(Object exchange,String token) {
        System.out.println("[CONTROL] Richiesto tracciamento per il token: " + token);
        try {
            // Cerchiamo nel DB usando il token parziale (estratto con LIKE %token=)
            String linkEsterno = db.verificaEsistenzaToken(token);
            System.out.println("[CONTROL] Link esterno trovato nel DB: " + linkEsterno);
            try{
             if (linkEsterno == null) {
                ab.alert("Link non valido o non più disponibile");
                eb.MandaErrore(exchange);
                
            }else{

                this.salvaDataOra(token);
                eb.Reindirizza(exchange, linkEsterno);

            }
            }catch(IOException e){
              e.printStackTrace();

            }
           

        } catch (SQLException e) {
            e.printStackTrace();
            ab.alert("Errore di sistema, premi OK per riprovare");
                        
            this.verificaEsistenzaCondivisione(exchange, token);
            return;
        }
    }


    public void salvaDataOra(String token){
        // Prendi il timestamp in millisecondi
        long ts = System.currentTimeMillis();
        // Convertilo in un oggetto java.sql.Timestamp valido per MySQL
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(ts);
        
        // Passa l'oggetto SQL corretto al metodo successivo
        this.modificaRiscontroVisualizzazione(token, sqlTimestamp);
    }

   public void modificaRiscontroVisualizzazione(String token, java.sql.Timestamp ts){
        // Aggiornalo nel db
        try {
             db.registraVisualizzazione(token, ts);
        } catch(SQLException e) {
            e.printStackTrace();
            ab.alert("Errore di sistema, premi ok per riprovare");
            this.modificaRiscontroVisualizzazione(token, ts);
        }
    }
    
    public void recuperaCondivisoni(String email,Object window){
      
        try{
             this.condivisioniPassate=this.db.richiediCondivisioni(email);
        }catch(SQLException e){
            e.printStackTrace();
            ab.alert("Errore di sistema, premi OK per riprovare");
            this.recuperaCondivisoni(email, window);
            
        }
       if(this.condivisioniPassate==null){
        ab.alert("Non hai effettuato ancora nessuna condivisione!");

       }else{
        
        this.caricaCondivisioni(this.condivisioniPassate, window);

       }
    }


    public void caricaCondivisioni(List<CondivisioneEntity> cpast, Object window){
        cpastBound.visualizza(window, cpast);
    }

    public void eliminazione(CondivisioneEntity cond,Object window){

    if(ab.conferma("Sei sicuro di volere eliminare la condivisione?")){
        
        this.richiediCancellazione(cond.getLinkEsterno());
        this.eliminaDatiCondivisione(cond);
        ab.alert("Condivisione eliminata con successo.");
        this.refreshPage(window,this.condivisioniPassate);
        
    }


    }
    
    public void richiediCancellazione(String link){
        
        cb.cancellaCartellaDaLink(link);



    }

    public void eliminaDatiCondivisione(CondivisioneEntity c){
        try{
            db.eliminaCondivisione(c);
        }catch(SQLException e){

            e.printStackTrace();
            ab.alert("Errore di sistema, premi OK per riprovare");
            this.eliminaDatiCondivisione(c);
        }
        
    }

    public void refreshPage(Object window,List<CondivisioneEntity> cpast){
        cpastBound.visualizza(window, cpast);
    }

        public void openDropboxPage(CondivisioneEntity cond){
        try {
                if (cond.getLinkEsterno() != null) {
                    String os = System.getProperty("os.name").toLowerCase();
                    
                    if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                        // --- SOLUZIONE PER UBUNTU / LINUX ---
                        // xdg-open invoca direttamente il browser predefinito del sistema
                        new ProcessBuilder("xdg-open", cond.getLinkEsterno()).start();
                    } else if (java.awt.Desktop.isDesktopSupported()) {
                        // --- FALLBACK PER WINDOWS / MAC ---
                        java.awt.Desktop.getDesktop().browse(new java.net.URI(cond.getLinkEsterno()));
                    } else {
                        System.err.println("Apertura browser non supportata su questo sistema.");
                    }
                }
            } catch (Exception ex) {
                System.err.println("Errore durante l'apertura del link:");
                ex.printStackTrace();
            }

    }



}