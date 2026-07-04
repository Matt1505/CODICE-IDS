package src.GeneralClasses.Entities;

public class StudenteEntity{

    private String matricola;
    private String nome;
    private String cognome;
    private String email;
    private String cf;
    private String pw;
    private String descrizione;
    private byte[] fotoProfilo; // Aggiunta della variabile per la foto del profilo

    public StudenteEntity(String m, String n, String c, String e, String cf, String pw){
        
        this.matricola=m;
        this.nome=n;
        this.cognome=c;
        this.email=e;
        this.cf=cf;
        this.pw=pw;

    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public byte[] getFotoProfilo() {
        return fotoProfilo;
    }

    public void setFotoProfilo(byte[] fotoProfilo) {
        this.fotoProfilo = fotoProfilo;
    }

    public String getMatricola(){

        return this.matricola;

    }

    public void setMatricola(String m){

        this.matricola=m;

    }

    public String getNome(){

        return this.nome;

    }


    public String getCognome(){

        return this.cognome;

    }

    public String getEmail(){

        return this.email;

    }

    public String getCf(){

        return this.cf;
    }

    public String getPw(){

        return this.pw;

    }

    public void setNome(String n){

        this.nome=n;

    }

    public void setCognome(String n){

        this.cognome=n;

    }

    public void setEmail(String n){

        this.email=n;

    }

    public void setCf(String c){

        this.cf=c;

    }

    public void setPw(String p){

        this.pw=p;

    }

}