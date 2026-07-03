package client.GeneralClasses.Entities;
import java.sql.Timestamp;

public class OTPEntity {
    private String code;
    private java.sql.Timestamp ts;

    public OTPEntity(String c, java.sql.Timestamp ts ){


        this.code=c;
        this.ts=ts;

    }
    // Getter per code
    public String getCode() {
        return code;
    }

    // Setter per code
    public void setCode(String code) {
        this.code = code;
    }

    // Getter per ts
    public java.sql.Timestamp getTs() {
        return ts;
    }

    // Setter per ts
    public void setTs(java.sql.Timestamp ts) {
        this.ts = ts;
    }


}
