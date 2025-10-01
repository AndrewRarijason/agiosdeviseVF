package mg.bmoi.agiosdevise.entity.identifiants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class HistoriqueArreteId implements Serializable {

    @Column(name = "NCP")
    private String ncp;

    @Column(name = "DATE_DEBUT_ARRETE")
    private Date dateDebutArrete;

    @Column(name = "DATE_FIN_ARRETE")
    private Date dateFinArrete;

    public HistoriqueArreteId() {}

    public HistoriqueArreteId(String ncp, Date dateDebutArrete, Date dateFinArrete) {
        this.ncp = ncp;
        this.dateDebutArrete = dateDebutArrete;
        this.dateFinArrete = dateFinArrete;
    }

    public String getNcp() {
        return ncp;
    }

    public void setNcp(String ncp) {
        this.ncp = ncp;
    }

    public Date getDateDebutArrete() {
        return dateDebutArrete;
    }

    public void setDateDebutArrete(Date dateDebutArrete) {
        this.dateDebutArrete = dateDebutArrete;
    }

    public Date getDateFinArrete() {
        return dateFinArrete;
    }

    public void setDateFinArrete(Date dateFinArrete) {
        this.dateFinArrete = dateFinArrete;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HistoriqueArreteId that = (HistoriqueArreteId) o;
        return Objects.equals(ncp, that.ncp) && Objects.equals(dateDebutArrete, that.dateDebutArrete) && Objects.equals(dateFinArrete, that.dateFinArrete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ncp, dateDebutArrete, dateFinArrete);
    }
}
