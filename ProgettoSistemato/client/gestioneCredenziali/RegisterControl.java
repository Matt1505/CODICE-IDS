package client.gestioneCredenziali;




import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import Server.DBMSBoundary;
import Server.mailServerBound;
import client.GeneralClasses.AlertBoundary;
import client.Altro.PageControl;
import client.GeneralClasses.*;
import client.GeneralClasses.Entities.OTPEntity;
import client.GeneralClasses.Entities.StudenteEntity;

public class RegisterControl {

  
    private client.GeneralClasses.Entities.StudenteEntity user;
    private String repeatPw;
    private DBMSBoundary dbBound = new DBMSBoundary();
    private mailServerBound mbound = new mailServerBound();
    private int wrongAttempsCounter = 0;
    private AlertBoundary ab = new AlertBoundary();

    public RegisterControl() {
        
    }
    
    public void createRegisterBoundary(Object windowContext) {
        RegisterBound rb = new RegisterBound();
        rb.visualizza(windowContext);
    }
  
    public void checkEmptyForm(String nome, String cognome, String email, 
                               String cf, String matricola, String pw, String repeatPw,Object windowContext) {
        if (nome.equals("")) {
            this.ab.alert("Tutti i campi devono essere riempiti");
            return;
        } else if (cognome.equals("")) {
            this.ab.alert("Tutti i campi devono essere riempiti");
            return;
        } else if (email.equals("")) {
            this.ab.alert("Tutti i campi devono essere riempiti");
            return;
        } else if (cf.equals("")) {
            this.ab.alert("Tutti i campi devono essere riempiti");
            return;
        } else if (matricola.equals("")) { 
            this.ab.alert("Tutti i campi devono essere riempiti");
            return;
        } else if (pw.equals("")) {
            this.ab.alert("Tutti i campi devono essere riempiti");
            return;
        } else if (repeatPw.equals("")) {
            this.ab.alert("Tutti i campi devono essere riempiti");
            return; 
        }

        int checkRes = checkPasswords(pw, repeatPw);

        if (checkRes == 1) {
            this.user = new StudenteEntity(matricola, nome, cognome, email, cf, pw);
            this.sendRequestUniqueData(email, cf,windowContext);
        } else {
            if (checkRes == 0) {
                this.ab.alert("campi password e ripeti password diversi!");
            } else {
                this.ab.alert("La password deve contenere almeno una letteral maiuscola e un carattere speciale");
            }
            return;
        }
    }

    public int checkPasswords(String pw, String repeatPw) {
        if (!pw.equals(repeatPw)) {
            return 0; 
        }

        boolean haMaiuscola = pw.matches(".*[A-Z].*");
        boolean haCarattereSpeciale = pw.matches(".*[^a-zA-Z0-9].*");

        if (haMaiuscola && haCarattereSpeciale) {
            return 1;
        } else {
            return -1;
        }
    }

    public void sendRequestUniqueData(String email, String cf,Object windowContext) {
        try {
            boolean isdata = dbBound.requestUniqueData(email, cf);
                                   
            if (isdata) {
                AlertBoundary errorBound = new AlertBoundary();
                errorBound.alert("Validazione fallita: Email o Codice Fiscale già registrati!");
            } else {
                this.createOTP(windowContext);
            }
        } catch (Exception e) {
            AlertBoundary errorBound = new AlertBoundary();
            errorBound.alert("Errore di sistema durante la verifica dei dati.");
            e.printStackTrace();
        }
    }

    private void createOTP(Object windowContext) {
        System.out.println("Generazione codice OTP in corso...");
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);    
        OTPBound otpb= new OTPBound(this, user.getEmail());
        otpb.visualizza(windowContext);
        this.saveOTP(number);
    }

    private void saveOTP(int OTP) {
        try {
            dbBound.addOtp(this.user.getEmail(), String.valueOf(OTP));
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mbound.sendOtpEmail(this.user.getEmail(), String.valueOf(OTP));
    }

    private void removeOTP(String email){
        try{
            dbBound.deleteOTP(this.user.getEmail());
            this.ab.alert("otp verificato con successo!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void requestGeneratedOTP(String email, String otp,Object windowContext) {
        try {
             this.verifyOTPs(dbBound.checkOTPExistance(this.user.getEmail(), otp),windowContext);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void increaseWrongAttempts() {
        this.wrongAttempsCounter++;
    }

    public void setWrongAttempsToZero() {
        this.wrongAttempsCounter = 0;
    }

    public void verifyOTPs(OTPEntity otpDalDb,Object windowContext) {
            if (otpDalDb != null) {
                String savedOTP = otpDalDb.getCode();
                java.sql.Timestamp createdAt = otpDalDb.getTs();            
                long currentTime = System.currentTimeMillis();
                long otpTime = createdAt.getTime();
                long diffMinutes = (currentTime - otpTime) / (60 * 1000);
                            
                if (diffMinutes <= 15) {
                     this.hashPassword(windowContext);
                } else {
                    System.out.println("Validazione fallita: Il codice OTP è scaduto.");
                    this.createOTP(windowContext);
                }
                this.removeOTP(this.user.getEmail());
            } else {
                this.ab.alert("OTP errato, reinseriscilo");
                
                this.increaseWrongAttempts();
                if (this.wrongAttempsCounter % 3 == 0) {
                    this.setWrongAttempsToZero();
                    this.ab.alert("ERRORE, raggiunti 3 tentativi errati, nuovo codice generato");
                    this.removeOTP(this.user.getEmail()); 
                    this.createOTP(windowContext);
                }
            }
    }

    private void hashPassword(Object windowContext) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(this.user.getPw().getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);

            while (hashtext.length() < 128) {
                hashtext = "0" + hashtext;
            }

            this.user.setPw(hashtext);
            this.sendCredentials(windowContext);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCredentials(Object windowContext) {
        try {
            this.dbBound.transmitCredentials(this.user.getMatricola(), this.user.getNome(), this.user.getCognome(), this.user.getEmail(), this.user.getCf(), this.user.getPw());
            LoginBound lb = new LoginBound();
            lb.visualizza(windowContext);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}