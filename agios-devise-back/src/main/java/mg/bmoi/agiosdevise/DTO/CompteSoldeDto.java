package mg.bmoi.agiosdevise.DTO;

import java.math.BigDecimal;

public class CompteSoldeDto {
    private String age;
    private String dev;
    private String ncp;
    private BigDecimal sde;

    public CompteSoldeDto(String age, String dev, String ncp, BigDecimal sde) {
        this.age = age;
        this.dev = dev;
        this.ncp = ncp;
        this.sde = sde;
    }

    // Getters and Setters
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

    public String getNcp() {
        return ncp;
    }

    public void setNcp(String ncp) {
        this.ncp = ncp;
    }

    public BigDecimal getSde() {
        return sde;
    }

    public void setSde(BigDecimal sde) {
        this.sde = sde;
    }
}