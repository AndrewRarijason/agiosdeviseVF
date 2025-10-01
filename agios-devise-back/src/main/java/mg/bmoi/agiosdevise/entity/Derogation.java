package mg.bmoi.agiosdevise.entity;

import javax.persistence.*;

@Entity
@Table(name = "DEROGATION", schema = "C##MGSTGADR")
public class Derogation {

    @Column(name = "AGE")
    private String age;
    @Column(name = "DEV")
    private String dev;
    @Id
    @Column(name = "NCP")
    private String ncp;
    @Column(name = "CHA")
    private String cha;
    @Column(name = "CLC")
    private String clc;
    @Column(name = "CLI")
    private String cli;
    @Column(name = "INTI")
    private String inti;
    @Column(name = "TYP")
    private String typ;
    @Column(name = "GES")
    private String ges;
    @Column(name = "EN_DEROGATION")
    private Integer enDerogation;

    public Derogation() {
    }



    public String getAge() {return age;}
    public void setAge(String age) {this.age = age;}

    public String getDev() {return dev;}
    public void setDev(String dev) {this.dev = dev;}

    public String getNcp() {return ncp;}
    public void setNcp(String ncp) {this.ncp = ncp;}

    public String getCha() {return cha;}
    public void setCha(String cha) {this.cha = cha;}

    public String getClc() {return clc;}
    public void setClc(String clc) {this.clc = clc;}

    public String getCli() {return cli;}
    public void setCli(String cli) {this.cli = cli;}

    public String getInti() {return inti;}
    public void setInti(String inti) {this.inti = inti;}

    public String getTyp() {return typ;}
    public void setTyp(String typ) {this.typ = typ;}

    public String getGes() {return ges;}
    public void setGes(String ges) {this.ges = ges;}

    public Integer getEnDerogation() {return enDerogation;}
    public void setEnDerogation(Integer enDerogation) {this.enDerogation = enDerogation;}
}
