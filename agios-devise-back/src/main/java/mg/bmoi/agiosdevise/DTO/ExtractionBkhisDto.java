package mg.bmoi.agiosdevise.DTO;

import java.math.BigDecimal;
import java.util.Date;

public class ExtractionBkhisDto {
    private final String age;
    private final String dev;
    private final String ncp;
    private final Date dco;
    private final Date dva;
    private final BigDecimal mon;
    private final Character sen;

    public ExtractionBkhisDto(String age, String dev, String ncp, Date dco, Date dva,
                              BigDecimal mon,Character sen) {
        this.age = age;
        this.dev = dev;
        this.ncp = ncp;
        this.dco = dco;
        this.dva = dva;
        this.mon = mon;
        this.sen = sen;
    }

    public String getAge() {return age;}
    public String getDev() {return dev;}
    public String getNcp() {return ncp;}
    public Date getDco() {return dco;}
    public Date getDva() {return dva;}
    public BigDecimal getMon() {return mon;}
    public Character getSen() {return sen;}

}
