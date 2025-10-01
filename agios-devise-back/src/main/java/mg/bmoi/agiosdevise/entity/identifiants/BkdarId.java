package mg.bmoi.agiosdevise.entity.identifiants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BkdarId implements Serializable {

    @Column (name = "NCP")
    private final String ncp;

    @Column (name = "ANNEE")
    private final int annee;

    @Column (name = "MOIS")
    private final int mois;

    public BkdarId() {
        this.ncp = null;
        this.annee = 0;
        this.mois = 0;
    }
    public BkdarId(String ncp, int annee, int mois) {
        this.ncp = ncp;
        this.annee = annee;
        this.mois = mois;
    }

    public String getNcp() { return ncp; }
    public int getAnnee() { return annee; }
    public int getMois() { return mois; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BkdarId bkdarId = (BkdarId) o;
        return annee == bkdarId.annee &&
                mois == bkdarId.mois &&
                Objects.equals(ncp, bkdarId.ncp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ncp, annee, mois);
    }
}
