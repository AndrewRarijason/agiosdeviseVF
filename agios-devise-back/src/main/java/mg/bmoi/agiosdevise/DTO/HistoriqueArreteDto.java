package mg.bmoi.agiosdevise.DTO;

import java.math.BigDecimal;
import java.util.Date;

public class HistoriqueArreteDto {
    private String age;
    private String dev;
    private String ncp;
    private String nomrest;
    private BigDecimal soldeFinalCalcule;
    private Date dateDebutArrete;
    private Date dateFinArrete;
    private BigDecimal sumMvtCrediteur;
    private BigDecimal tauxCrediteur;
    private BigDecimal interetCrediteur;
    private BigDecimal sumMvtDebiteur;
    private BigDecimal tauxDebiteur;
    private BigDecimal interetDebiteur;
    private BigDecimal netAgios;
    private Boolean isKo;
    private String tcli;

    public HistoriqueArreteDto() {
    }

    public HistoriqueArreteDto (String age, String dev, String ncp, String nomrest, BigDecimal soldeFinalCalcule,
                                Date dateDebutArrete, Date dateFinArrete, BigDecimal sumMvtCrediteur, BigDecimal tauxCrediteur,
                                BigDecimal interetCrediteur, BigDecimal sumMvtDebiteur, BigDecimal tauxDebiteur,
                                BigDecimal interetDebiteur, BigDecimal netAgios, Boolean isKo, String tcli) {
        this.age = age;
        this.dev = dev;
        this.ncp = ncp;
        this.nomrest = nomrest;
        this.soldeFinalCalcule = soldeFinalCalcule;
        this.dateDebutArrete = dateDebutArrete;
        this.dateFinArrete = dateFinArrete;
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

    public String getAge() {return age;}
    public void setAge(String age) {this.age = age;}

    public String getDev() {return dev;}
    public void setDev(String dev) {this.dev = dev;}

    public String getNcp() {return ncp;}
    public void setNcp(String ncp) {this.ncp = ncp;}

    public String getNomrest() {return nomrest;}
    public void setNomrest(String nomrest) {this.nomrest = nomrest;}

    public BigDecimal getSoldeFinalCalcule() {return soldeFinalCalcule;}
    public void setSoldeFinalCalcule(BigDecimal soldeFinalCalcule) {this.soldeFinalCalcule = soldeFinalCalcule;}

    public Date getDateDebutArrete() {return dateDebutArrete;}
    public void setDateDebutArrete(Date dateDebutArrete) {this.dateDebutArrete = dateDebutArrete;}

    public Date getDateFinArrete() {return dateFinArrete;}
    public void setDateFinArrete(Date dateFinArrete) {this.dateFinArrete = dateFinArrete;}

    public BigDecimal getSumMvtCrediteur() {return sumMvtCrediteur;}
    public void setSumMvtCrediteur(BigDecimal sumMvtCrediteur) {this.sumMvtCrediteur = sumMvtCrediteur;}

    public BigDecimal getTauxCrediteur() {return tauxCrediteur;}
    public void setTauxCrediteur(BigDecimal tauxCrediteur) {this.tauxCrediteur = tauxCrediteur;}

    public BigDecimal getInteretCrediteur() {return interetCrediteur;}
    public void setInteretCrediteur(BigDecimal interetCrediteur) {this.interetCrediteur = interetCrediteur;}

    public BigDecimal getSumMvtDebiteur() {return sumMvtDebiteur;}
    public void setSumMvtDebiteur(BigDecimal sumMvtDebiteur) {this.sumMvtDebiteur = sumMvtDebiteur;}

    public BigDecimal getTauxDebiteur() {return tauxDebiteur;}
    public void setTauxDebiteur(BigDecimal tauxDebiteur) {this.tauxDebiteur = tauxDebiteur;}

    public BigDecimal getInteretDebiteur() {return interetDebiteur;}
    public void setInteretDebiteur(BigDecimal interetDebiteur) {this.interetDebiteur = interetDebiteur;}

    public BigDecimal getNetAgios() {return netAgios;}
    public void setNetAgios(BigDecimal netAgios) {this.netAgios = netAgios;}

    public Boolean getKo() {return isKo;}
    public void setKo(Boolean ko) {isKo = ko;}

    public String getTcli() {return tcli;}
    public void setTcli(String tcli) {this.tcli = tcli;}
}
