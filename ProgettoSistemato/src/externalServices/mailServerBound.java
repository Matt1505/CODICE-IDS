package src.externalServices;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import java.util.Random;


public class mailServerBound {
    
    private final String username = "afamapp@gmail.com";
    private final String password = "foca fkjn bvxd nvoe"; // Password per le app di Google
    private final String host = "smtp.gmail.com";
    private final String port = "587";
    
    


    public void sendOtpEmail(String toEmail, String otp) {
        
        // 1. Impostazione delle proprietà del server SMTP
        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        // 2. Creazione della sessione autenticata
        jakarta.mail.Session session = jakarta.mail.Session.getInstance(prop, new jakarta.mail.Authenticator() {
        @Override
    protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
        return new jakarta.mail.PasswordAuthentication(username, password);
                }
        });

        try {
            // 3. Creazione del messaggio email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Il tuo codice di verifica OTP");

            // Contenuto HTML della mail
            String htmlContent = "<h3>Verifica del tuo account</h3>"
                               + "<p>Usa il seguente codice OTP per completare la registrazione:</p>"
                               + "<h2 style='color: #007bff; letter-spacing: 2px;'>" + otp + "</h2>"
                               + "<p>Il codice scadrà tra 15 minuti.</p>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // 4. Invio dell'email
            Transport.send(message);

            System.out.println("Email OTP inviata con successo a: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("Errore durante l'invio dell'email: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
