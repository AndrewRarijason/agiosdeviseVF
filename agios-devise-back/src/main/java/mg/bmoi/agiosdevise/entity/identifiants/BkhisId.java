package mg.bmoi.agiosdevise.entity.identifiants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class BkhisId implements Serializable {

    @Column (name = "NCP")
    private final String ncp;

    @Column (name = "DCO")
    private final LocalDate dco;

    @Column (name = "DVA")
    private final LocalDate dva;

    @Column (name = "PIE")
    private final String pie;

    @Column (name = "EVE")
    private final String eve;

    public BkhisId() {
        this.ncp = null;
        this.dco = null;
        this.dva = null;
        this.pie = null;
        this.eve = null;
    }
    public BkhisId(String ncp, LocalDate dco, LocalDate dva, String pie, String eve) {
        this.ncp = ncp;
        this.dco = dco;
        this.dva = dva;
        this.pie = pie;
        this.eve = eve;
    }

    public String getNcp() { return ncp; }
    public LocalDate getDco() { return dco; }
    public LocalDate getDva() { return dva; }
    public String getPie() { return pie; }
    public String getEve() { return eve; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BkhisId bkhisId = (BkhisId) o;
        return Objects.equals(ncp, bkhisId.ncp) &&
                Objects.equals(dco, bkhisId.dco) &&
                Objects.equals(dva, bkhisId.dva) &&
                Objects.equals(pie, bkhisId.pie) &&
                Objects.equals(eve, bkhisId.eve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ncp, dco, dva, pie, eve);
    }
}
