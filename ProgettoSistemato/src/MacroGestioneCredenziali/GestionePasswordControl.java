package src.gestioneCredenziali;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;

import src.GeneralClasses.AlertBoundary;
import src.GeneralClasses.Entities.OTPEntity;
import src.MacroGestioneProfilo.HomePageControl;
import src.externalServices.mailServerBound;
import src.repository.DBMSBoundary;

public class GestionePasswordControl {

    private DBMSBoundary dbBound;
    private mailServerBound mailBound;
    private AlertBoundary alertBound;

    private String email;
    private int wrongAttemptsCounter;

    public GestionePasswordControl() {
        this.dbBound = new DBMSBoundary();
        this.mailBound = new mailServerBound();
        this.alertBound = new AlertBoundary();
        this.wrongAttemptsCounter = 0;
    }

    public GestionePasswordControl(String email) {
        this();
        this.email = email;
    }

    // =========================================================
    // PARTE 1: RECUPERA PASSWORD
    // LoginBound -> LoginControl -> RecuperaPasswordBound
    // RecuperaPasswordBound -> GestionePasswordControl
    // =========================================================

    public void checkEmailNotNull(String email, Object windowContext) {
        if (email == null || email.trim().isEmpty()) {
            this.alertBound.alert("ERRORE, inserisci la tua email.");
            return;
        }

        this.email = email.trim();
        this.verifyIfEmailExists(windowContext);
    }

    private void verifyIfEmailExists(Object windowContext) {
        try {
            boolean emailPresente = this.dbBound.verifyIfEmailExists(this.email);

            if (!emailPresente) {
                this.alertBound.alert("ERRORE, l'email inserita non è presente nel Database.");
                return;
            }

            this.createOTP();

            OTPBound otpBound = new OTPBound(this, this.email);
            otpBound.visualizza(windowContext);

        } catch (SQLException e) {
            e.printStackTrace();
            this.alertBound.alert("Errore durante la verifica dell'email.");
        }
    }

    private void createOTP() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);

        this.saveOTP(number);
        this.sendOTP(String.valueOf(number));
    }

    private void saveOTP(int otp) {
        try {
            this.dbBound.addOtp(this.email, String.valueOf(otp));
        } catch (SQLException e) {
            e.printStackTrace();
            this.alertBound.alert("Errore durante la generazione del codice OTP.");
        }
    }

    private void sendOTP(String otp) {
        this.mailBound.sendOtpEmail(this.email, otp);
    }

    public void requestGeneratedOTP(String email, String otp, Object windowContext) {
        try {
            this.email = email;

            OTPEntity otpDalDb = this.dbBound.checkOTPExistance(this.email, otp);
            this.verifyOTPs(otpDalDb, windowContext);

        } catch (SQLException e) {
            e.printStackTrace();
            this.alertBound.alert("Errore durante la verifica dell'OTP.");
        }
    }

    private void verifyOTPs(OTPEntity otpDalDb, Object windowContext) {
        if (otpDalDb != null) {
            java.sql.Timestamp createdAt = otpDalDb.getTs();

            long currentTime = System.currentTimeMillis();
            long otpTime = createdAt.getTime();
            long diffMinutes = (currentTime - otpTime) / (60 * 1000);

            if (diffMinutes <= 15) {
                this.removeOTP(this.email);
                this.setWrongAttemptsToZero();

                UpdatePasswordBound updatePasswordBound = new UpdatePasswordBound(this.email);
                updatePasswordBound.visualizza(windowContext);

            } else {
                this.alertBound.alert("ERRORE, il codice non è più valido, premi su OK per generarne uno nuovo");

                this.setWrongAttemptsToZero();
                this.removeOTP(this.email);
                this.createOTP();
            }

        } else {
            this.increaseWrongAttempts();

            if (this.wrongAttemptsCounter >= 3) {
                this.alertBound.alert("ERRORE, raggiunti 3 tentativi errati, nuovo codice generato");

                this.setWrongAttemptsToZero();
                this.removeOTP(this.email);
                this.createOTP();

            } else {
                this.alertBound.alert("Codice errato, riprovare");
            }
        }
    }

    private void removeOTP(String email) {
        try {
            this.dbBound.deleteOTP(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void increaseWrongAttempts() {
        this.wrongAttemptsCounter++;
    }

    private void setWrongAttemptsToZero() {
        this.wrongAttemptsCounter = 0;
    }

    // =========================================================
    // PARTE 2: RESET / MODIFICA PASSWORD
    // UpdatePasswordBound -> GestionePasswordControl
    // =========================================================

    public void sendCredentials(String password, String ripetiPassword, Object windowContext) {
        if (password == null || password.isEmpty() || ripetiPassword == null || ripetiPassword.isEmpty()) {
            this.alertBound.alert("ERRORE, tutti i campi devono essere compilati");
            return;
        }

        int checkResult = this.checkPassword(password, ripetiPassword);

        if (checkResult == 0) {
            this.alertBound.alert("Le due password non coincidono");
            return;
        }

        if (checkResult == -1) {
            this.alertBound.alert("La password deve contenere almeno una lettera maiuscola e un carattere speciale");
            return;
        }

        String passwordHash = this.hashPassword(password);
        this.sendPassword(passwordHash, windowContext);
    }

    private int checkPassword(String password, String ripetiPassword) {
        if (!password.equals(ripetiPassword)) {
            return 0;
        }

        boolean haMaiuscola = password.matches(".*[A-Z].*");
        boolean haCarattereSpeciale = password.matches(".*[^a-zA-Z0-9].*");

        if (!haMaiuscola || !haCarattereSpeciale) {
            return -1;
        }

        return 1;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(password.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);

            while (hashtext.length() < 128) {
                hashtext = "0" + hashtext;
            }

            return hashtext;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendPassword(String passwordHash, Object windowContext) {
        try {
            this.dbBound.transmitPassword(this.email, passwordHash);

            this.alertBound.alert("Password aggiornata con successo.");

            HomePageControl homePageControl = new HomePageControl(this.email);
            homePageControl.createHomePageBoundary(this.email, windowContext);

        } catch (SQLException e) {
            e.printStackTrace();
            this.alertBound.alert("Errore durante l'aggiornamento della password.");
        }
    }
}