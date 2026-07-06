package src.GeneralClasses.Entities;

import java.sql.Timestamp;

public class CondivisioneEntity {
    
    private int id;
    
    private String linkEsterno;
    private String linkLocale;
    private String emailAssociata;
    private String emailMittente;
    private Timestamp dataCondivisione;

    // Costruttore vuoto
    public CondivisioneEntity() {}

    // Costruttore completo
    public CondivisioneEntity(int id,  String linkEsterno, String linkLocale, 
                              String emailAssociata, String emailMittente, Timestamp dataCondivisione) {
        this.id = id;
        
        this.linkEsterno = linkEsterno;
        this.linkLocale = linkLocale;
        this.emailAssociata = emailAssociata;
        this.emailMittente = emailMittente;
        this.dataCondivisione = dataCondivisione;
    }

    // Getter e Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

   
    

    public String getLinkEsterno() { return linkEsterno; }
    public void setLinkEsterno(String linkEsterno) { this.linkEsterno = linkEsterno; }

    public String getLinkLocale() { return linkLocale; }
    public void setLinkLocale(String linkLocale) { this.linkLocale = linkLocale; }

    public String getEmailAssociata() { return emailAssociata; }
    public void setEmailAssociata(String emailAssociata) { this.emailAssociata = emailAssociata; }

    public String getEmailMittente() { return emailMittente; }
    public void setEmailMittente(String emailMittente) { this.emailMittente = emailMittente; }

    public Timestamp getDataCondivisione() { return dataCondivisione; }
    public void setDataCondivisione(Timestamp dataCondivisione) { this.dataCondivisione = dataCondivisione; }
}