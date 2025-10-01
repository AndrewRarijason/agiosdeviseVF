package mg.bmoi.agiosdevise.entity.identifiants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class BkhisTempId implements Serializable {

    @Column(name = "AGE")
    private String age;

    @Column(name = "DEV")
    private String dev;

    @Column(name = "NCP")
    private String ncp;

    @Column (name = "DCO")
    private Date dco;

    @Column (name = "DVA")
    private Date dva;

    @Column (name = "SEN")
    private Character sen;

    public BkhisTempId() {
    }

    public BkhisTempId(String age, String dev, String ncp, Date dco, Date dva, Character sen) {
        this.age = age;
        this.dev = dev;
        this.ncp = ncp;
        this.dco = dco;
        this.dva = dva;
        this.sen = sen;
    }

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

    public Date getDco() {
        return dco;
    }

    public void setDco(Date dco) {
        this.dco = dco;
    }

    public Date getDva() {
        return dva;
    }

    public void setDva(Date dva) {
        this.dva = dva;
    }

    public Character getSen() {
        return sen;
    }

    public void setSen(Character sen) {
        this.sen = sen;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BkhisTempId that = (BkhisTempId) o;
        return Objects.equals(age, that.age) && Objects.equals(dev, that.dev) && Objects.equals(ncp, that.ncp) && Objects.equals(dco, that.dco) && Objects.equals(dva, that.dva) && Objects.equals(sen, that.sen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, dev, ncp, dco, dva, sen);
    }
}
