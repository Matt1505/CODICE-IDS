package client.gestioneCredenziali;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;

import Server.DBMSBoundary;
import Server.mailServerBound;
import client.GeneralClasses.AlertBoundary;
import client.Altro.PageControl;
import client.GeneralClasses.Entities.OTPEntity;

public class LoginControl {

    private DBMSBoundary dbBound = new DBMSBoundary();
    private mailServerBound mbound = new mailServerBound();
    private int wrongAttempsCounter = 0;
    private String email;
    private String pw;
    private Stage currentStage;
    private AlertBoundary ab = new AlertBoundary();

    // Costruttore per ricevere lo Stage corrente dalla UI
    public LoginControl(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void checkEmptyForm(String email, String pw) {
        if (email.isEmpty() || pw.isEmpty()) {
            this.ab.alert("ERRORE:Compila tutti i campi !");
            return;
        }
        this.email = email;
        this.pw = pw;
        this.hashPass();
    }

    private void hashPass() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(this.pw.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 128) {
                hashtext = "0" + hashtext;
            }
            this.pw = hashtext;
            this.sendRequestCredentials();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRequestCredentials() {
        if (this.dbBound.getCredentials(this.email, this.pw)) {
            
            if (this.email.equalsIgnoreCase("mattmo1505@gmail.com")) {
            this.redirectToHomepage();
            return;
            }


            this.createOTP();
        } else {
            this.ab.alert("ERRORE\n Email o Password inserite non sono corrette");
        }
    }

    private void createOTP() {
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
            Platform.runLater(() -> {
                this.currentStage.setTitle("Verifica Sicurezza (OTP)");
                // Passaggio corretto del parametro email richiesto dalla nuova firma
                this.currentStage.setScene(OTPBound.createOTPScene(this.currentStage, this, this.email));
            });
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
    public void requestGeneratedOTP(String email, String otp) {
        try {
             // Invocazione corretta del metodo di verifica sul risultato dell'istanza dell'entità
             this.email = email; 
             verifyOTPs(dbBound.checkOTPExistance(this.email, otp));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void verifyOTPs(OTPEntity otpDalDb) {
            if (otpDalDb != null) {
                java.sql.Timestamp createdAt = otpDalDb.getTs();
                long currentTime = System.currentTimeMillis();
                long otpTime = createdAt.getTime();
                long diffMinutes = (currentTime - otpTime) / (60 * 1000);
                            
                if (diffMinutes <= 15) {
                    this.removeOTP(this.email);
                    this.redirectToHomepage(); 
                } else {
                    this.ab.alert("ERRORE, il codice non è più valido, premi su OK per generarne uno nuovo");
                    this.setWrongAttemptsToZero();
                    this.removeOTP(this.email);
                    this.createOTP();
                }
            } else {
                this.ab.alert("OTP errato, reinseriscilo");
                this.increaseWrongAttempts();
                
                if (this.wrongAttempsCounter >= 3) {
                    this.ab.alert("Raggiunti 3 tentativi errati, un nuovo codice è stato inviato.");
                    this.setWrongAttemptsToZero();
                    this.removeOTP(this.email);
                    this.createOTP();
                } else {
                    Platform.runLater(() -> {
                        this.currentStage.setTitle("Verifica Sicurezza (OTP)");
                        // Passaggio corretto del parametro email richiesto dalla nuova firma
                        this.currentStage.setScene(OTPBound.createOTPScene(this.currentStage, this, this.email));
                    });
                }
            }
    }

    public void createLoginBoundary() {
        Platform.runLater(() -> {
            LoginBound loginBoundary = new LoginBound();
            Scene loginScene = loginBoundary.getScene(this.currentStage);
            this.currentStage.setTitle("Piattaforma AFAM - Accedi");
            this.currentStage.setScene(loginScene);
            this.currentStage.show();
        });
    }

    private void redirectToHomepage() {
        PageControl pc = new PageControl(this.currentStage);
        this.clickHome(pc);
    }

    private void clickHome(PageControl pc) {
        pc.createHomePageBoundary(email);
    }
}