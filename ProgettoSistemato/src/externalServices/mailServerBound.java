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

public void mailCondivisione(String toEmail, String link) {
        if (toEmail == null || toEmail.trim().isEmpty() || link == null || link.trim().isEmpty()) {
            System.err.println("Errore: Destinatario o link mancanti. Impossibile inviare la mail.");
            return;
        }

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
            // 3. Creazione del messaggio email per il singolo destinatario
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail.trim()));
            
            // Oggetto formale in linea con le specifiche del sistema AFAM
            message.setSubject("Condivisione Identità Digitale Studente - Istituzione AFAM");

            // Contenuto HTML della mail con il link che punta al tuo ShareServerBoundary
            String htmlContent = "<h3>Consultazione Materiale Artistico e Formativo</h3>"
                               + "<p>Un utente del sistema di Identità Digitale AFAM ha condiviso con te "
                               + "una selezione della propria produzione artistica (portfolio o documenti).</p>"
                               + "<p>Puoi accedere ai contenuti multimediali cliccando sul seguente collegamento sicuro:</p>"
                               + "<p style='margin: 20px 0;'>"
                               + "   <a href='" + link + "' style='background-color: #007bff; color: white; padding: 10px 15px; text-decoration: none; border-radius: 5px; font-weight: bold;'>"
                               + "      Visualizza Contenuti Condivisi"
                               + "   </a>"
                               + "</p>"
                               + "<p style='font-size: 12px; color: #6c757d;'>Se il pulsante non funziona, copia e incolla questo indirizzo nel browser:<br>" + link + "</p>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // 4. Invio dell'email
            Transport.send(message);

            System.out.println("Email di condivisione inviata con successo a: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("Errore durante l'invio dell'email di condivisione: " + e.getMessage());
            e.printStackTrace();
        }
    }


}

