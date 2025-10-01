package mg.bmoi.agiosdevise.DTO;

public class DerogImportExcelDto {
    private String age;
    private String dev;
    private String ncp;
    private String clc;
    private String cli;
    private String inti;
    private String typ;
    private String ges;
    private String derogation;

    public DerogImportExcelDto() {

    }

    public DerogImportExcelDto(String age, String dev, String ncp, String clc, String cli,
                               String inti, String typ, String ges, String derogation) {
        this.age = age;
        this.dev = dev;
        this.ncp = ncp;
        this.clc = clc;
        this.cli = cli;
        this.inti = inti;
        this.typ = typ;
        this.ges = ges;
        this.derogation = derogation;
    }


    public String getAge() {return age;}
    public String getDev() {return dev;}
    public String getNcp() {return ncp;}
    public String getClc() {return clc;}
    public String getCli() {return cli;}
    public String getInti() {return inti;}
    public String getTyp() {return typ;}
    public String getGes() {return ges;}
    public String getDerogation() {return derogation;}

    public void setAge(String age) {this.age = age;}
    public void setDev(String dev) {this.dev = dev;}
    public void setNcp(String ncp) {this.ncp = ncp;}
    public void setClc(String clc) {this.clc = clc;}
    public void setCli(String cli) {this.cli = cli;}
    public void setInti(String inti) {this.inti = inti;}
    public void setTyp(String typ) {this.typ = typ;}
    public void setGes(String ges) {this.ges = ges;}
    public void setDerogation(String derogation) {this.derogation = derogation;}
}
