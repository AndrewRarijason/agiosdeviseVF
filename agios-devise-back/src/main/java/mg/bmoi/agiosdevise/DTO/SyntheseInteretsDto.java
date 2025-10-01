package mg.bmoi.agiosdevise.DTO;

import java.math.BigDecimal;

public class SyntheseInteretsDto {
    private BigDecimal interetCrediteur;
    private BigDecimal tauxCrediteur;
    private BigDecimal interetDebiteur;
    private BigDecimal tauxDebiteur;
    private String ircm;
    private String tauxIrcm;
    private BigDecimal netAgios;
    private BigDecimal sumMvtDebiteur;
    private BigDecimal sumMvtCrediteur;
    private int totalNbJours;

    public SyntheseInteretsDto() {
    }

    public SyntheseInteretsDto(BigDecimal interetCrediteur, BigDecimal tauxCrediteur, BigDecimal interetDebiteur,
                               BigDecimal tauxDebiteur, String ircm, String tauxIrcm,
                               BigDecimal netAgios, BigDecimal sumMvtDebiteur, BigDecimal sumMvtCrediteur,
                               int totalNbJours) {
        this.interetCrediteur = interetCrediteur;
        this.tauxCrediteur = tauxCrediteur;
        this.interetDebiteur = interetDebiteur;
        this.tauxDebiteur = tauxDebiteur;
        this.ircm = ircm;
        this.tauxIrcm = tauxIrcm;
        this.netAgios = netAgios;
        this.sumMvtDebiteur = sumMvtDebiteur;
        this.sumMvtCrediteur = sumMvtCrediteur;
        this.totalNbJours = totalNbJours;
    }

    public BigDecimal getInteretCrediteur() {return interetCrediteur;}
    public void setInteretCrediteur(BigDecimal interetCrediteur) {this.interetCrediteur = interetCrediteur;}

    public BigDecimal getTauxCrediteur() {return tauxCrediteur;}
    public void setTauxCrediteur(BigDecimal tauxCrediteur) {this.tauxCrediteur = tauxCrediteur;}

    public BigDecimal getInteretDebiteur() {return interetDebiteur;}
    public void setInteretDebiteur(BigDecimal interetDebiteur) {this.interetDebiteur = interetDebiteur;}

    public BigDecimal getTauxDebiteur() {return tauxDebiteur;}
    public void setTauxDebiteur(BigDecimal tauxDebiteur) {this.tauxDebiteur = tauxDebiteur;}

    public String getIrcm() {return ircm;}
    public void setIrcm(String ircm) {this.ircm = ircm;}

    public String getTauxIrcm() {return tauxIrcm;}
    public void setTauxIrcm(String tauxIrcm) {this.tauxIrcm = tauxIrcm;}

    public BigDecimal getNetAgios() {return netAgios;}
    public void setNetAgios(BigDecimal netAgios) {this.netAgios = netAgios;}

    public BigDecimal getSumMvtDebiteur() {return sumMvtDebiteur;}
    public void setSumMvtDebiteur(BigDecimal sumMvtDebiteur) {this.sumMvtDebiteur = sumMvtDebiteur;}

    public BigDecimal getSumMvtCrediteur() {return sumMvtCrediteur;}
    public void setSumMvtCrediteur(BigDecimal sumMvtCrediteur) {this.sumMvtCrediteur = sumMvtCrediteur;}

    public int getTotalNbJours() {return totalNbJours;}
    public void setTotalNbJours(int totalNbJours) {this.totalNbJours = totalNbJours;}
}