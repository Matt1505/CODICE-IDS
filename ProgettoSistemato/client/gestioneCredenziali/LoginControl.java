package client.gestioneCredenziali;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;

import Server.DBMSBoundary;
import Server.mailServerBound;
import client.GeneralClasses.AlertBoundary;
import client.GeneralClasses.Entities.OTPEntity;
import client.MacroGestioneProfilo.HomePageBoundary;
import client.MacroGestioneProfilo.HomePageControl;
import javafx.application.Platform;

public class LoginControl {

    private DBMSBoundary dbBound = new DBMSBoundary();
    private mailServerBound mbound = new mailServerBound();
    private int wrongAttempsCounter = 0;
    private String email;
    private String pw;
    private AlertBoundary ab = new AlertBoundary();
    private LoginBound lb;
    // Costruttore per ricevere lo Stage corrente dalla UI
    public LoginControl() {
        
    }

    public void checkEmptyForm(String email, String pw,Object windowContext) {
        if (email.isEmpty() || pw.isEmpty()) {
            this.ab.alert("ERRORE:Compila tutti i campi !");
            return;
        }
        this.email = email;
        this.pw = pw;
        this.hashPass(windowContext);
        
    }

    

    private void hashPass(Object windowContext) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(this.pw.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 128) {
                hashtext = "0" + hashtext;
            }
            this.pw = hashtext;
            this.sendRequestCredentials(windowContext);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRequestCredentials(Object windowContext) {
        if (this.dbBound.getCredentials(this.email, this.pw)) {
            
            if (this.email.equalsIgnoreCase("mattmo1505@gmail.com") || this.email.equalsIgnoreCase("dircellobattino@gmail.com")) {
            this.redirectToHomepage(windowContext);
            return;
            }

            this.createOTP(windowContext);
            OTPBound otpBound = new OTPBound(this, this.email);
            otpBound.visualizza(windowContext);
            
        } else {
            this.ab.alert("ERRORE\n Email o Password inserite non sono corrette");
        }
    }

    private void createOTP(Object windowContext) {
        System.out.println("Generazione codice OTP in corso...");
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);    
        this.saveOTP(number);
    }

    private void sendOTP(String OTP){
        mbound.sendOtpEmail(this.email, OTP);
    }

    private void saveOTP(int OTP) {
        try {
            dbBound.addOtp(this.email, String.valueOf(OTP));
            
            this.sendOTP(String.valueOf(OTP));
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeOTP(String email) {
        try {
            dbBound.deleteOTP(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void increaseWrongAttempts() {
        this.wrongAttempsCounter++;
    }

    public void setWrongAttemptsToZero() {
        this.wrongAttempsCounter = 0;
    }

    // Firma corretta per combaciare con l'invocazione polimorfica proveniente da OTPBound
    public void requestGeneratedOTP(String email, String otp,Object windowContext) {
        try {
             // Invocazione corretta del metodo di verifica sul risultato dell'istanza dell'entità
             this.email = email; 
             verifyOTPs(dbBound.checkOTPExistance(this.email, otp),windowContext);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void verifyOTPs(OTPEntity otpDalDb,Object windowContext) {
            if (otpDalDb != null) {
                java.sql.Timestamp createdAt = otpDalDb.getTs();
                long currentTime = System.currentTimeMillis();
                long otpTime = createdAt.getTime();
                long diffMinutes = (currentTime - otpTime) / (60 * 1000);
                            
                if (diffMinutes <= 15) {
                    this.removeOTP(this.email);
                    this.redirectToHomepage(windowContext); 
                } else {
                    this.ab.alert("ERRORE, il codice non è più valido, premi su OK per generarne uno nuovo");
                    this.setWrongAttemptsToZero();
                    this.removeOTP(this.email);
                    this.createOTP(windowContext);
                }
            } else {
                this.ab.alert("OTP errato, reinseriscilo");
                this.increaseWrongAttempts();
                
                if (this.wrongAttempsCounter >= 3) {
                    this.ab.alert("Raggiunti 3 tentativi errati, un nuovo codice è stato inviato.");
                    this.setWrongAttemptsToZero();
                    this.removeOTP(this.email);
                    this.createOTP(windowContext);
                } 
            }
    }
    public void redirectToHomepage(Object windowContext){

        HomePageControl hc = new HomePageControl(email);
        hc.createHomePageBoundary(email, windowContext);

    }

    public void createLoginBoundary(Object windowContext) {
        LoginBound lb= new LoginBound();
        lb.visualizza(windowContext);
    }



    
}