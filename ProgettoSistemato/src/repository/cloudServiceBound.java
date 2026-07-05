package src.repository;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.oauth.DbxCredential;

import src.GeneralClasses.Entities.ContenutoEntity;
import src.GeneralClasses.Entities.StudenteEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class cloudServiceBound {
    
    private static final String APP_KEY = "6tqvlrj1ryiqnpj";
    private static final String APP_SECRET = "7uyddw6rnaaduki";
    private static final String REFRESH_TOKEN = "wIIEEtZ5uYAAAAAAAAAAASeE_k0cQbPBVQU1QTEpzYb7uRsRvzlVryT0KD_zsICh";
    
    private final DbxClientV2 client;

    public cloudServiceBound() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("sistema-identita-afam").build();
        
        // Utilizziamo il Builder per creare le credenziali, garantendo compatibilità assoluta ed evitando errori di tipi
       DbxCredential credential = new DbxCredential(
    "",            // 1. accessToken iniziale (lascia vuoto "")
    -1L,           // 2. expiresAt (forza il rinnovo immediato)
    REFRESH_TOKEN, // 3. refreshToken
    APP_KEY,       // 4. appKey
    APP_SECRET     // 5. appSecret
);

        // Inizializzazione del client globale con le credenziali auto-rinnovanti
        this.client = new DbxClientV2(config, credential);
    }

    public String caricaSelezioneContenuti(List<ContenutoEntity> ce) {
        if (ce == null || ce.isEmpty()) {
            System.out.println("Nessun contenuto selezionato per il caricamento Cloud.");
            return null;
        }

        try {
            // NOTA: Rimossa la ridefinizione locale di 'config' e 'client' che causava l'errore!
            // Da questo punto in poi usiamo l'attributo di classe 'this.client'
           
            // Usiamo un timestamp per rendere il nome della cartella univoco (es: /Condivisioni/Sessione_1719839402)
            String nomeCartella = "Sessione_" + System.currentTimeMillis();
            String pathCartella = "/Condivisioni/" + nomeCartella;

            // Creiamo esplicitamente la cartella su Dropbox usando il client configurato a livello di istanza
            this.client.files().createFolderV2(pathCartella);
            System.out.println("Cartella creata su Dropbox: " + pathCartella);

            // 3. Ciclo di upload per ciascun contenuto all'interno della cartella creata
            for (ContenutoEntity contenuto : ce) {
                byte[] fileBytes = contenuto.getFile(); 
                
                if (fileBytes != null && fileBytes.length > 0) {
                    try (InputStream in = new ByteArrayInputStream(fileBytes)) {
                        String estensione = contenuto.getTipo().toLowerCase();
                        if (estensione.contains("/")) {
                            estensione = estensione.substring(estensione.lastIndexOf("/") + 1);
                        }
                        
                        String pathFileCompleto = pathCartella + "/" + contenuto.getTitolo() + "." + estensione;

                        // Eseguiamo l'upload del file usando il client di istanza
                        FileMetadata metadata = this.client.files().uploadBuilder(pathFileCompleto)
                                .withMode(WriteMode.OVERWRITE)
                                .uploadAndFinish(in);
                        
                        System.out.println("Caricato file: " + metadata.getName());
                    }
                }
            }

            // 4. Generazione del link di condivisione pubblico per l'intera cartella
            var sharingFolderLink = this.client.sharing().createSharedLinkWithSettings(pathCartella);
            String urlCondivisione = sharingFolderLink.getUrl();
            
            System.out.println("Link cloud generato con successo: " + urlCondivisione);
            return urlCondivisione;

        } catch (Exception e) {
            System.err.println("Errore critico durante l'interazione con il servizio Cloud di Dropbox:");
            e.printStackTrace();
            return null;
        }
    }
}