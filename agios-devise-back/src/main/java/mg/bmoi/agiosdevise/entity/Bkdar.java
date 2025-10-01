package mg.bmoi.agiosdevise.entity;

import mg.bmoi.agiosdevise.entity.identifiants.BkdarId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "BKDAR", schema = "BANK")
public class Bkdar {

    @EmbeddedId
    private BkdarId id;

    private String age;
    private String dev;
    private BigDecimal solde;
    private String cli;

    public BkdarId getId() { return id; }
    public void setId(BkdarId id) { this.id = id; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getDev() { return dev; }
    public void setDev(String dev) { this.dev = dev; }

    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }

    public String getCli() { return cli; }
    public void setCli(String cli) { this.cli = cli; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Bkdar bkdar = (Bkdar) o;
        return Objects.equals(id, bkdar.id) &&
                Objects.equals(age, bkdar.age) &&
                Objects.equals(dev, bkdar.dev) &&
                Objects.equals(solde, bkdar.solde) &&
                Objects.equals(cli, bkdar.cli);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, dev, solde, cli);
    }
}