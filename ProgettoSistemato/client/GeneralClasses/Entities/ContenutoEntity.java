package client.GeneralClasses.Entities;

public class ContenutoEntity {
    private int id;
    private byte[] file;
    private String titolo;
    private String descrizione;
    private String tipo;
    private int posizione;

    public ContenutoEntity(int id,byte[] file, String titolo, String descrizione, String tipo,int p) {
       
        this.id = id;
        this.file = file;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.tipo = tipo;
        this.posizione = p;
    }

    public int getId() {
        return id;
    }
    public byte[] getFile() {
        return file;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getTipo() {
        return tipo;
    }

    public int getPosizione() {
        return posizione;
    }
    public void setPosizione(int posizione) {
        this.posizione = posizione;
    }
}
