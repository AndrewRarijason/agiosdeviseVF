package mg.bmoi.agiosdevise.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionDto implements Serializable {
    private String ncp;
    private BigDecimal mon;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Europe/Moscow")
    private Date dva;
    private Character sen;
    private BigDecimal soldeDepart;
    private int nbJoursInactif;
    private BigDecimal nbDebiteur;
    private BigDecimal nbCrediteur;


    public TransactionDto() {}

    public TransactionDto(String ncp, BigDecimal mon, Date dva, Character sen, BigDecimal soldeDepart, int nbJoursInactif, BigDecimal nbDebiteur, BigDecimal nbCrediteur) {
        this.ncp = ncp;
        this.mon = mon;
        this.dva = dva;
        this.sen = sen;
        this.soldeDepart = soldeDepart;
        this.nbJoursInactif = nbJoursInactif;
        this.nbDebiteur = nbDebiteur;
        this.nbCrediteur = nbCrediteur;
    }

    public String getNcp() {return ncp;}
    public void setNcp(String ncp) {this.ncp = ncp;}

    public BigDecimal getMon() {return mon;}
    public void setMon(BigDecimal mon) {this.mon = mon;}

    public Date getDva() {return dva;}
    public void setDva(Date dva) {this.dva = dva;}

    public Character getSen() {return sen;}
    public void setSen(Character sen) {this.sen = sen;}

    public BigDecimal getSoldeDepart() {return soldeDepart;}
    public void setSoldeDepart(BigDecimal soldeDepart) {this.soldeDepart = soldeDepart;}

    public int getNbJoursInactif() {return nbJoursInactif;}
    public void setNbJoursInactif(int nbJoursInactif) {this.nbJoursInactif = nbJoursInactif;}

    public BigDecimal getNbDebiteur() {return nbDebiteur;}
    public void setNbDebiteur(BigDecimal nbDebiteur) {this.nbDebiteur = nbDebiteur;}

    public BigDecimal getNbCrediteur() {return nbCrediteur;}
    public void setNbCrediteur(BigDecimal nbCrediteur) {this.nbCrediteur = nbCrediteur;}
}
