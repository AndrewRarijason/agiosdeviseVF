package mg.bmoi.agiosdevise.entity.identifiants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

// Clé composite
@Embeddable
public class BkcomId implements Serializable {
    @Column(name = "AGE")
    private final String age;

    @Column(name = "DEV")
    private final String dev;

    @Column(name = "NCP")
    private final String ncp;

    public BkcomId() {
        this.age = null;
        this.dev = null;
        this.ncp = null;
    }

    public BkcomId(String age, String dev, String ncp) {
        this.age = age;
        this.dev = dev;
        this.ncp = ncp;
    }

    // Getters, equals, hashCode
    public String getAge() {
        return age;
    }
    public String getDev() {
        return dev;
    }
    public String getNcp() {
        return ncp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BkcomId bkcomId = (BkcomId) o;
        return Objects.equals(age, bkcomId.age) &&
                Objects.equals(dev, bkcomId.dev) &&
                Objects.equals(ncp, bkcomId.ncp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, dev, ncp);
    }
}