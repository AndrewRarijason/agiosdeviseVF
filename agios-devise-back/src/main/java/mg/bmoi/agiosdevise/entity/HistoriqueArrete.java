package mg.bmoi.agiosdevise.entity;

import mg.bmoi.agiosdevise.entity.identifiants.HistoriqueArreteId;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "HISTORIQUE_ARRETE", schema = "C##MGSTGADR")
public class HistoriqueArrete {

    @EmbeddedId
    private HistoriqueArreteId id;

    private String age;
    private String dev;
    private String nomrest;
    private BigDecimal soldeFinalCalcule;
    private BigDecimal sumMvtCrediteur;
    private BigDecimal tauxCrediteur;
    private BigDecimal interetCrediteur;
    private BigDecimal sumMvtDebiteur;
    private BigDecimal tauxDebiteur;
    private BigDecimal interetDebiteur;
    private BigDecimal netAgios;

    @Column(name = "ISKO")
    private Boolean isKo;

    @Column(name = "TCLI")
    private String tcli;

    public HistoriqueArrete() {}

    public HistoriqueArrete(HistoriqueArreteId id, String age, String dev, String nomrest, BigDecimal soldeFinalCalcule, BigDecimal sumMvtCrediteur,
                            BigDecimal tauxCrediteur, BigDecimal interetCrediteur, BigDecimal sumMvtDebiteur,
                            BigDecimal tauxDebiteur, BigDecimal interetDebiteur, BigDecimal netAgios,
                            Boolean isKo, String tcli) {
        this.id = id;
        this.age = age;
        this.dev = dev;
        this.nomrest = nomrest;
        this.soldeFinalCalcule = soldeFinalCalcule;
        this.sumMvtCrediteur = sumMvtCrediteur;
        this.tauxCrediteur = tauxCrediteur;
        this.interetCrediteur = interetCrediteur;
        this.sumMvtDebiteur = sumMvtDebiteur;
        this.tauxDebiteur = tauxDebiteur;
        this.interetDebiteur = interetDebiteur;
        this.netAgios = netAgios;
        this.isKo = isKo;
        this.tcli = tcli;
    }

    public HistoriqueArreteId getId() {
        return id;
    }
    public void setId(HistoriqueArreteId id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getDev() {
        return dev;
    }
    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getNomrest() {
        return nomrest;
    }
    public void setNomrest(String nomrest) {
        this.nomrest = nomrest;
    }

    public BigDecimal getSoldeFinalCalcule() {
        return soldeFinalCalcule;
    }
    public void setSoldeFinalCalcule(BigDecimal soldeFinalCalcule) {
        this.soldeFinalCalcule = soldeFinalCalcule;
    }

    public BigDecimal getSumMvtCrediteur() {
        return sumMvtCrediteur;
    }
    public void setSumMvtCrediteur(BigDecimal sumMvtCrediteur) {
        this.sumMvtCrediteur = sumMvtCrediteur;
    }

    public BigDecimal getTauxCrediteur() {
        return tauxCrediteur;
    }
    public void setTauxCrediteur(BigDecimal tauxCrediteur) {
        this.tauxCrediteur = tauxCrediteur;
    }

    public BigDecimal getInteretCrediteur() {
        return interetCrediteur;
    }
    public void setInteretCrediteur(BigDecimal interetCrediteur) {
        this.interetCrediteur = interetCrediteur;
    }

    public BigDecimal getSumMvtDebiteur() {
        return sumMvtDebiteur;
    }
    public void setSumMvtDebiteur(BigDecimal sumMvtDebiteur) {
        this.sumMvtDebiteur = sumMvtDebiteur;
    }

    public BigDecimal getTauxDebiteur() {
        return tauxDebiteur;
    }
    public void setTauxDebiteur(BigDecimal tauxDebiteur) {
        this.tauxDebiteur = tauxDebiteur;
    }

    public BigDecimal getInteretDebiteur() {
        return interetDebiteur;
    }
    public void setInteretDebiteur(BigDecimal interetDebiteur) {
        this.interetDebiteur = interetDebiteur;
    }

    public BigDecimal getNetAgios() {
        return netAgios;
    }
    public void setNetAgios(BigDecimal netAgios) {
        this.netAgios = netAgios;
    }

    public Boolean getKo() {return isKo;}
    public void setKo(Boolean ko) {isKo = ko;}

    public String getTcli() {return tcli;}
    public void setTcli(String tcli) {this.tcli = tcli;}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HistoriqueArrete that = (HistoriqueArrete) o;
        return Objects.equals(id, that.id) && Objects.equals(age, that.age) && Objects.equals(dev, that.dev) &&
                Objects.equals(nomrest, that.nomrest) && Objects.equals(soldeFinalCalcule, that.soldeFinalCalcule) &&
                Objects.equals(sumMvtCrediteur, that.sumMvtCrediteur) && Objects.equals(tauxCrediteur, that.tauxCrediteur) &&
                Objects.equals(interetCrediteur, that.interetCrediteur) && Objects.equals(sumMvtDebiteur, that.sumMvtDebiteur) &&
                Objects.equals(tauxDebiteur, that.tauxDebiteur) && Objects.equals(interetDebiteur, that.interetDebiteur) &&
                Objects.equals(netAgios, that.netAgios) && Objects.equals(isKo, that.isKo) && Objects.equals(tcli, that.tcli);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, dev, nomrest, soldeFinalCalcule, sumMvtCrediteur, tauxCrediteur, interetCrediteur,
                sumMvtDebiteur, tauxDebiteur, interetDebiteur, netAgios, isKo, tcli);
    }
}