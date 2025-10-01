package mg.bmoi.agiosdevise.DTO;

import java.math.BigDecimal;
import java.util.Date;

public class ExtractionBkdarDto {
    private final String ageBkdar;
    private final String dev;
    private final String ncp;
    private final BigDecimal nbc;
    private final BigDecimal txc; // decimal 10 - 7
    private final BigDecimal nbr; // decimal 19 - 4
    private final BigDecimal taux; // decimal 10 - 7
    private final BigDecimal solde; // decimal 19 - 4
    private final String nomrest; // String
    private final String adr1;
    private final String adr2;
    private final String cpos; // Char (10)
    private final String clc; // Char (2)
    private final Date datr; // Date
    private final Character tcli; // Char (1)
    private final String vil; // Char
    private final String cli;

    public ExtractionBkdarDto(String ageBkdar, String dev, String ncp, BigDecimal nbc, BigDecimal txc, BigDecimal nbr,
                              BigDecimal taux, BigDecimal solde, String nomrest, String adr1, String adr2, String cpos,
                              String clc, Date datr, Character tcli, String vil, String cli) {
        this.ageBkdar = ageBkdar;
        this.dev = dev;
        this.ncp = ncp;
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
        this.cli = cli;
    }

    public String getAgeBkdar() {return ageBkdar;}
    public String getDev() {return dev;}
    public String getNcp() {return ncp;}
    public BigDecimal getNbc() {return nbc;}
    public BigDecimal getTxc() {return txc;}
    public BigDecimal getNbr() {return nbr;}
    public BigDecimal getTaux() {return taux;}
    public BigDecimal getSolde() {return solde;}
    public String getNomrest() {return nomrest;}
    public String getAdr1() {return adr1;}
    public String getAdr2() {return adr2;}
    public String getCpos() {return cpos;}
    public String getClc() {return clc;}
    public Date getDatr() {return datr;}
    public Character getTcli() {return tcli;}
    public String getVil() {return vil;}
    public String getCli() {return cli;}
}