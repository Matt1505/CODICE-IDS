package src.repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


import src.GeneralClasses.Entities.ContenutoEntity;
import src.GeneralClasses.Entities.OTPEntity;
import src.GeneralClasses.Entities.StudenteEntity;
import src.GeneralClasses.*;



public class DBMSBoundary {

    //Dettagli di connessione
    private static final String URL = "jdbc:mysql://localhost:3306/AFAMDB?serverTimezone=UTC";
    private static final String USER = "ADMIN";       
    private static final String PASSWORD = "admin"; 
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Impossibile caricare il driver MySQL Connector/J: " + e.getMessage());
        }
    }
    public DBMSBoundary(){
        try{
            this.eseguiConnessione();
        }catch(SQLException e){

            e.printStackTrace();

        }
        
    }
    /**
     * Stabilisce la connessione fisica con il database.
     * Metodo privato per non esporre l'oggetto Connection (specifico di JDBC) all'esterno.
     */
    private Connection eseguiConnessione() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    

    /**
     * Interroga il DBMS per verificare l'esistenza di duplicati.
     * Accetta solo tipi primitivi/stringhe (nessun accoppiamento con logiche UI o di business).
     *
     * @return true se i dati esistono già (conflitto), false se non esistono (unici).
     */
    public boolean requestUniqueData(String email, String codiceFiscale) throws SQLException {
        String query = "SELECT COUNT(*) FROM utenti WHERE email = ? OR codice_fiscale = ?";
        
        try (Connection conn = eseguiConnessione();
             PreparedStatement stmt = conn.prepareStatement(query)) { //prepare statement (query)
             
            stmt.setString(1, email);
            stmt.setString(2, codiceFiscale);//per evitare sql injection
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    public boolean transmitCredentials(String matricola, String nome, String cognome, String email, String codiceFiscale, String passwordHash) throws SQLException {
        String query = "INSERT INTO utenti (matricola, nome, cognome, email, codice_fiscale, password) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = eseguiConnessione();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, matricola);
            stmt.setString(2, nome);
            stmt.setString(3, cognome);
            stmt.setString(4, email);
            stmt.setString(5, codiceFiscale);
            stmt.setString(6, passwordHash); 
            
            int righeImpatto = stmt.executeUpdate();
            return righeImpatto > 0;
        }
    }

   public boolean transmitOtp(String email, String otpCode) throws SQLException {
        // Inserisce una nuova riga nella tabella otp_codes
        String query = "INSERT INTO otp_codes (email, otp_code) VALUES (?, ?)";
        
        try (Connection conn = eseguiConnessione();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, email);
            stmt.setString(2, otpCode);
            
            int righeImpatto = stmt.executeUpdate();
            return righeImpatto > 0;
        }
    }


    /**
     * Verifica se il codice OTP inserito dall'utente è corretto e non scaduto.
     * Prende l'ultimo OTP (il più recente) associato all'email.
     * * @param email L'email dell'utente
     * @param otpCode Il codice OTP inserito dall'utente
     * @return true se l'OTP è valido e corretto, false altrimenti
     */
public OTPEntity checkOTPExistance(String email,String otp) throws SQLException {
    String query = "SELECT otp_code, created_at FROM otp_codes WHERE email = ? and otp_code=? ORDER BY created_at DESC LIMIT 1";
    
    try (Connection conn = eseguiConnessione();
         PreparedStatement stmt = conn.prepareStatement(query)) {
         
        stmt.setString(1, email);
        stmt.setString(2,otp);
        // Inseriamo anche il ResultSet nel try-with-resources così si chiude da solo in sicurezza
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String savedOtp = rs.getString("otp_code");
                java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
                return new OTPEntity(savedOtp, createdAt);
            }
        }
    }
    return null; // Nessun OTP trovato
}

public void deleteOTP(String email) throws SQLException{

    String query = "DELETE  FROM otp_codes WHERE email = ? ORDER BY created_at DESC LIMIT 1";
       try (Connection conn = eseguiConnessione();
        PreparedStatement stmt = conn.prepareStatement(query)) {
         
        stmt.setString(1, email);
        
        stmt.executeUpdate();
        
    }
    System.err.println("OTP eliminato per l'email: " + email);

}


public boolean addOtp(String email, String otpCode) throws SQLException {
    String query = "INSERT INTO otp_codes (email, otp_code) VALUES (?, ?)";
    
    // Il try-with-resources apre e chiude automaticamente le risorse nelle parentesi tonde
    try (Connection conn = eseguiConnessione();
         PreparedStatement stmt = conn.prepareStatement(query)) {
         
        stmt.setString(1, email);
        stmt.setString(2, otpCode);
        
        int righeImpatto = stmt.executeUpdate();
        return righeImpatto > 0;
    }
}


public boolean getCredentials(String email, String password){

    String query = "SELECT COUNT(*) FROM utenti WHERE email = ? AND password = ?";
    
    try (Connection conn = eseguiConnessione();
         PreparedStatement stmt = conn.prepareStatement(query)) {
         
        stmt.setString(1, email);
        stmt.setString(2, password);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // Se il conteggio è maggiore di 0, le credenziali sono corrette
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.err.println("Errore durante la verifica delle credenziali: " + e.getMessage());
        // In caso di errore del database, per sicurezza restituiamo false
        return false; 
    }
    
    return false;



}

public byte[] getProfilePicture(String email){
String query="SELECT foto_profilo from utenti WHERE email=?";
try (Connection conn = eseguiConnessione();
     PreparedStatement stmt = conn.prepareStatement(query)) {   

        stmt.setString(1, email);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getBytes("foto_profilo");
       }  
    }
    }catch (SQLException e) {
        System.err.println("Errore durante il recupero della foto profilo: " + e.getMessage());
        return null;
    }
    return null;
}


public boolean inserisciContenuto(byte[] file, String titolo, String descrizione, String tipo, String email, int posizione, boolean isPubblic) throws SQLException {
    String query = "INSERT INTO contenuti (titolo, descrizione, tipo, file_blob, studente_email, posizione, isPubblic) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = eseguiConnessione();
         PreparedStatement stmt = conn.prepareStatement(query)) {
         
        stmt.setString(1, titolo);
        stmt.setString(2, descrizione);
        stmt.setString(3, tipo);
        stmt.setBytes(4, file);
        stmt.setString(5, email);
        stmt.setInt(6, posizione);
        stmt.setBoolean(7, isPubblic);
        
        int righeImpatto = stmt.executeUpdate();
        return righeImpatto > 0;
    }
}

public List<ContenutoEntity> getPublicResources(String email) {
    List<ContenutoEntity> userResources = new ArrayList<>();
    String query = "SELECT id, file_blob, titolo, descrizione, tipo, posizione FROM contenuti WHERE studente_email = ? AND isPubblic = TRUE ORDER BY posizione ASC";

    try (Connection conn = eseguiConnessione();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, email);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                byte[] file = rs.getBytes("file_blob");
                String titolo = rs.getString("titolo");
                String descrizione = rs.getString("descrizione");
                String tipo = rs.getString("tipo");
                int posizione = rs.getInt("posizione");

                userResources.add(new ContenutoEntity(id, file, titolo, descrizione, tipo, posizione));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return userResources;
}

public List<ContenutoEntity> getResources(String email) {
        List<ContenutoEntity> userResources = new ArrayList<>();
        String query = "SELECT id,file_blob, titolo, descrizione, tipo, posizione FROM contenuti WHERE studente_email = ? ORDER BY posizione ASC";

        try (Connection conn = eseguiConnessione();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    byte[] file = rs.getBytes("file_blob");
                    String titolo = rs.getString("titolo");
                    String descrizione = rs.getString("descrizione");
                    String tipo = rs.getString("tipo");
                    int posizione = rs.getInt("posizione");

                    userResources.add(new ContenutoEntity(id, file, titolo, descrizione, tipo, posizione));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userResources;
    }

    public int getMaxPosizione(String email) throws SQLException {
        String query = "SELECT MAX(posizione) AS max_posizione FROM contenuti WHERE studente_email = ?";
        
        try (Connection conn = eseguiConnessione();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_posizione");
                }
            }
        }
        return 0; // Nessuna risorsa trovata
    }
    

    public StudenteEntity getUserInfo(String email) throws SQLException {
        String query = "SELECT matricola, nome, cognome, email, codice_fiscale, password, descrizione, foto_profilo FROM utenti WHERE email = ?";
        
        try (Connection conn = eseguiConnessione();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String matricola = rs.getString("matricola");
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");
                    String cf = rs.getString("codice_fiscale");
                    String pw = rs.getString("password");
                    String descrizione = rs.getString("descrizione");
                    byte[] fotoProfilo = rs.getBytes("foto_profilo");

                    StudenteEntity studente = new StudenteEntity(matricola, nome, cognome, email, cf, pw);
                    studente.setDescrizione(descrizione);
                    studente.setFotoProfilo(fotoProfilo);

                    return studente;
                }
            }
        }
        return null; // Nessun utente trovato
    }
    public void aggiornaFotoProfilo(byte[] file, String email) throws SQLException {
        String query = "UPDATE utenti SET foto_profilo = ? WHERE email = ?";
        
        try (Connection conn = eseguiConnessione();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setBytes(1, file);
            stmt.setString(2, email);
            
            int righeImpatto = stmt.executeUpdate();
            if (righeImpatto == 0) {
                throw new SQLException("Nessun utente trovato con l'email specificata.");
            }
        }
    }
    public void aggiornaPosizioniContenuti(HashMap<Integer, Integer> mappaPosizioni) throws SQLException {
        String query = "UPDATE contenuti SET posizione = ? WHERE id = ?";
        
        try (Connection conn = eseguiConnessione();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            for (HashMap.Entry<Integer, Integer> entry : mappaPosizioni.entrySet()) {
                int id = entry.getKey();
                int nuovaPosizione = entry.getValue();
                
                stmt.setInt(1, nuovaPosizione);
                stmt.setInt(2, id);
                
                
                stmt.addBatch(); // Aggiunge l'aggiornamento al batch
            }
            
            stmt.executeBatch(); // Esegue tutti gli aggiornamenti in batch
        }
    }

       public ArrayList<StudenteEntity> cerca(String query, String emailCorrente) throws SQLException {
        ArrayList<StudenteEntity> risultati = new ArrayList<>();

        String sql = """
            SELECT matricola, nome, cognome, email, codice_fiscale, password, descrizione, foto_profilo
            FROM utenti
            WHERE email <> ?
            AND (
                nome LIKE ?
                OR cognome LIKE ?
                OR CONCAT(nome, ' ', cognome) LIKE ?
                OR CONCAT(cognome, ' ', nome) LIKE ?
            )
            LIMIT 10
        """;

        try (Connection conn = eseguiConnessione();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + query.trim() + "%";

            stmt.setString(1, emailCorrente);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            stmt.setString(4, pattern);
            stmt.setString(5, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StudenteEntity studente = new StudenteEntity(
                        rs.getString("matricola"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("codice_fiscale"),
                        rs.getString("password")
                    );

                    studente.setDescrizione(rs.getString("descrizione"));
                    studente.setFotoProfilo(rs.getBytes("foto_profilo"));

                    risultati.add(studente);
                }
            }
        }

        return risultati;
    }
public void aggiornaDescrizioneProfilo(String email, String descrizione) throws SQLException {
    String query = "UPDATE utenti SET descrizione = ? WHERE email = ?";

    try (Connection conn = eseguiConnessione();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, descrizione);
        stmt.setString(2, email);

        int righeImpatto = stmt.executeUpdate();

        if (righeImpatto == 0) {
            throw new SQLException("Nessun utente trovato con l'email specificata.");
        }
    }
}
public void richiediEliminazioneContenuto(int idContenuto, String emailStudente) throws SQLException {
    this.eliminaContenuto(idContenuto, emailStudente);
}

    private void eliminaContenuto(int idContenuto, String emailStudente) throws SQLException {
        String query = "DELETE FROM contenuti WHERE id = ? AND studente_email = ?";

        try (Connection conn = eseguiConnessione();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idContenuto);
            stmt.setString(2, emailStudente);

            int righeImpatto = stmt.executeUpdate();

            if (righeImpatto == 0) {
                throw new SQLException("Nessun contenuto trovato oppure contenuto non appartenente all'utente.");
            }
        }
    }

    public void modifica(int idContenuto, String emailStudente, byte[] file, String titolo, String descrizione, String tipo) throws SQLException {
        String query;

        if (file != null) {
            query = "UPDATE contenuti SET file_blob = ?, titolo = ?, descrizione = ?, tipo = ? WHERE id = ? AND studente_email = ?";
        } else {
            query = "UPDATE contenuti SET titolo = ?, descrizione = ? WHERE id = ? AND studente_email = ?";
        }

        try (Connection conn = eseguiConnessione();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            if (file != null) {
                stmt.setBytes(1, file);
                stmt.setString(2, titolo);
                stmt.setString(3, descrizione);
                stmt.setString(4, tipo);
                stmt.setInt(5, idContenuto);
                stmt.setString(6, emailStudente);
            } else {
                stmt.setString(1, titolo);
                stmt.setString(2, descrizione);
                stmt.setInt(3, idContenuto);
                stmt.setString(4, emailStudente);
            }

            int righeImpatto = stmt.executeUpdate();

            if (righeImpatto == 0) {
                throw new SQLException("Nessun contenuto trovato oppure contenuto non appartenente all'utente.");
            }
        }
    }
        public boolean verifyIfEmailExists(String email) throws SQLException {
    String query = "SELECT COUNT(*) FROM utenti WHERE email = ?";

    try (Connection conn = eseguiConnessione();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, email);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }

    return false;
}

public void transmitPassword(String email, String passwordHash) throws SQLException {
    String query = "UPDATE utenti SET password = ? WHERE email = ?";

    try (Connection conn = eseguiConnessione();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, passwordHash);
        stmt.setString(2, email);

        int righeImpatto = stmt.executeUpdate();

        if (righeImpatto == 0) {
            throw new SQLException("Nessun utente trovato con l'email specificata.");
        }
    }
}
} 