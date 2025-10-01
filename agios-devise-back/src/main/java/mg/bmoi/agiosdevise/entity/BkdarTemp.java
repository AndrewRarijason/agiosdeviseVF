package mg.bmoi.agiosdevise.entity;

import mg.bmoi.agiosdevise.entity.identifiants.BkdarTempId;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "BKDAR_D", schema = "C##MGSTGADR")
public class BkdarTemp {

    @EmbeddedId
    private BkdarTempId id;

    private String age;
    private BigDecimal nbc;
    private BigDecimal txc;
    private BigDecimal nbr;
    private BigDecimal taux;
    private BigDecimal solde;
    private String nomrest;
    private String adr1;
    private String adr2;
    private String cpos;
    private String clc;
    private Date datr;
    private String tcli;
    private String vil;

    public BkdarTemp() {

    }

    public BkdarTemp(BkdarTempId id, String age, BigDecimal nbc, BigDecimal txc, BigDecimal nbr, BigDecimal taux, BigDecimal solde, String nomrest, String adr1, String adr2, String cpos, String clc, Date datr, String tcli, String vil) {
        this.id = id;
        this.age = age;
        this.nbc = nbc;
        this.txc = txc;
        this.nbr = nbr;
        this.taux = taux;
        this.solde = solde;
        this.nomrest = nomrest;
        this.adr1 = adr1;
        this.adr2 = adr2;
        this.cpos = cpos;
        this.clc = clc;
        this.datr = datr;
        this.tcli = tcli;
        this.vil = vil;
    }

    public BkdarTempId getId() {
        return id;
    }

    public void setId(BkdarTempId id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public BigDecimal getNbc() {
        return nbc;
    }

    public void setNbc(BigDecimal nbc) {
        this.nbc = nbc;
    }

    public BigDecimal getTxc() {
        return txc;
    }

    public void setTxc(BigDecimal txc) {
        this.txc = txc;
    }

    public BigDecimal getNbr() {
        return nbr;
    }

    public void setNbr(BigDecimal nbr) {
        this.nbr = nbr;
    }

    public BigDecimal getTaux() {
        return taux;
    }

    public void setTaux(BigDecimal taux) {
        this.taux = taux;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public String getNomrest() {
        return nomrest;
    }

    public void setNomrest(String nomrest) {
        this.nomrest = nomrest;
    }

    public String getAdr1() {
        return adr1;
    }

    public void setAdr1(String adr1) {
        this.adr1 = adr1;
    }

    public String getAdr2() {
        return adr2;
    }

    public void setAdr2(String adr2) {
        this.adr2 = adr2;
    }

    public String getCpos() {
        return cpos;
    }

    public void setCpos(String cpos) {
        this.cpos = cpos;
    }

    public String getClc() {
        return clc;
    }

    public void setClc(String clc) {
        this.clc = clc;
    }

    public Date getDatr() {
        return datr;
    }

    public void setDatr(Date datr) {
        this.datr = datr;
    }

    public String getTcli() {
        return tcli;
    }

    public void setTcli(String tcli) {
        this.tcli = tcli;
    }

    public String getVil() {
        return vil;
    }

    public void setVil(String vil) {
        this.vil = vil;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BkdarTemp bkdarTemp = (BkdarTemp) o;
        return Objects.equals(id, bkdarTemp.id) && Objects.equals(age, bkdarTemp.age) && Objects.equals(nbc, bkdarTemp.nbc) && Objects.equals(txc, bkdarTemp.txc) && Objects.equals(nbr, bkdarTemp.nbr) && Objects.equals(taux, bkdarTemp.taux) && Objects.equals(solde, bkdarTemp.solde) && Objects.equals(nomrest, bkdarTemp.nomrest) && Objects.equals(adr1, bkdarTemp.adr1) && Objects.equals(adr2, bkdarTemp.adr2) && Objects.equals(cpos, bkdarTemp.cpos) && Objects.equals(clc, bkdarTemp.clc) && Objects.equals(datr, bkdarTemp.datr) && Objects.equals(tcli, bkdarTemp.tcli) && Objects.equals(vil, bkdarTemp.vil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, nbc, txc, nbr, taux, solde, nomrest, adr1, adr2, cpos, clc, datr, tcli, vil);
    }
}