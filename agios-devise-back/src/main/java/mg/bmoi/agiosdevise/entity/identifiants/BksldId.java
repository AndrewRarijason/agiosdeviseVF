package mg.bmoi.agiosdevise.entity.identifiants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class BksldId implements Serializable {  // Doit implémenter Serializable

    @Column(name = "AGE")
    private final String age;

    @Column(name = "DEV")
    private final String dev;

    @Column(name = "NCP")
    private final String ncp;

    @Column(name = "DCO")
    private final LocalDate dco;

    public BksldId() {
        this.age = null;
        this.dev = null;
        this.ncp = null;
        this.dco = null;
    }

    public BksldId(String age, String dev, String ncp, LocalDate dco) {
        this.age = age;
        this.dev = dev;
        this.ncp = ncp;
        this.dco = dco;
    }

    // Getters & Setters
    public String getAge() { return age; }
    public String getDev() { return dev; }
    public String getNcp() { return ncp; }
    public LocalDate getDco() { return dco; }


    // Obligatoire pour une clé composite : equals() et hashCode()
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BksldId bksldId = (BksldId) o;
        return Objects.equals(age, bksldId.age) &&
                Objects.equals(dev, bksldId.dev) &&
                Objects.equals(ncp, bksldId.ncp) &&
                Objects.equals(dco, bksldId.dco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, dev, ncp, dco);
    }
}