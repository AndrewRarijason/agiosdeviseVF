package mg.bmoi.agiosdevise.entity;

import mg.bmoi.agiosdevise.entity.identifiants.BkhisId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "BKHIS", schema = "BANK")
public class Bkhis {

    @EmbeddedId
    private BkhisId id;

    @Column(name = "AGE")
    private String age;

    @Column(name = "DEV")
    private String dev;

    @Column(name = "MON")
    private BigDecimal mon;

    @Column(name = "SEN")
    private Character sen;

    public BkhisId getId() { return id; }
    public void setId(BkhisId id) { this.id = id; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getDev() { return dev; }
    public void setDev(String dev) { this.dev = dev; }

    public BigDecimal getMon() { return mon; }
    public void setMon(BigDecimal mon) { this.mon = mon; }

    public Character getSen() { return sen; }
    public void setSen(Character sen) { this.sen = sen; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Bkhis bkhis = (Bkhis) o;
        return Objects.equals(id, bkhis.id) && Objects.equals(age, bkhis.age) && Objects.equals(dev, bkhis.dev) && Objects.equals(mon, bkhis.mon) && Objects.equals(sen, bkhis.sen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, dev, mon, sen);
    }
}
