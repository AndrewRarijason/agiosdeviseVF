package mg.bmoi.agiosdevise.entity;

import mg.bmoi.agiosdevise.entity.identifiants.BkhisTempId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "BKHIS_D", schema = "C##MGSTGADR")
public class BkhisTemp {
    @EmbeddedId
    private BkhisTempId id;

    private BigDecimal mon;

    public BkhisTemp() {

    }

    public BkhisTemp(BkhisTempId id, BigDecimal mon) {
        this.id = id;
        this.mon = mon;
    }

    public BkhisTempId getId() {
        return id;
    }

    public void setId(BkhisTempId id) {
        this.id = id;
    }

    public BigDecimal getMon() {
        return mon;
    }

    public void setMon(BigDecimal mon) {
        this.mon = mon;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BkhisTemp bkhisTemp = (BkhisTemp) o;
        return Objects.equals(id, bkhisTemp.id) && Objects.equals(mon, bkhisTemp.mon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mon);
    }
}
