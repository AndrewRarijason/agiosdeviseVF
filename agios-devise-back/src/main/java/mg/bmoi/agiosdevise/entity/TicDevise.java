package mg.bmoi.agiosdevise.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="TICDEVISE", schema="C##MGSTGADR")
public class TicDevise {

    @Id
    @Column(name="CDALPHA")
    private String cdAlpha;

    @Column(name="CDISO")
    private String cdIso;

    @Column(name="LIBELE")
    private String libele;

    @Column(name= "VALEUR")
    private BigDecimal valeur;

    public TicDevise() {}

    public TicDevise(String cdAlpha, String cdIso, String libele, BigDecimal valeur) {
        this.cdAlpha = cdAlpha;
        this.cdIso = cdIso;
        this.libele = libele;
        this.valeur = valeur;
    }

    public String getCdAlpha() {
        return cdAlpha;
    }

    public void setCdAlpha(String cdAlpha) {
        this.cdAlpha = cdAlpha;
    }

    public String getCdIso() {
        return cdIso;
    }

    public void setCdDiso(String cdIso) {
        this.cdIso = cdIso;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }
}
