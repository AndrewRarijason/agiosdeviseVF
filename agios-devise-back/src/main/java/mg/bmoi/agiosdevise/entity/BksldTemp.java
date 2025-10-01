package mg.bmoi.agiosdevise.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "BKSLD_D", schema = "C##MGSTGADR")
public class BksldTemp {

    private String age;
    private String dev;
    @Id
    private String ncp;
    private BigDecimal sde;

    public BksldTemp() {

    }

    public BksldTemp(String age, String dev, String ncp, BigDecimal sde) {
        this.age = age;
        this.dev = dev;
        this.ncp = ncp;
        this.sde = sde;
    }

    public String getAge() {
        return age;
    }

    public String getDev() {
        return dev;
    }

    public String getNcp() {
        return ncp;
    }

    public BigDecimal getSde() {
        return sde;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public void setSde(BigDecimal sde) {
        this.sde = sde;
    }
}
