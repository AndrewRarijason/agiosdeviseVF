package mg.bmoi.agiosdevise.entity;

import mg.bmoi.agiosdevise.entity.identifiants.BksldId;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "BKSLD", schema = "BANK")
public class Bksld {

    @EmbeddedId
    private BksldId id;  // Utilise la clé composite définie dans BksldId

    @Column(name = "SDE")
    private BigDecimal solde;

    // Getters & Setters
    public BksldId getId() { return id; }
    public void setId(BksldId id) { this.id = id; }

    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Bksld bksld = (Bksld) o;
        return Objects.equals(id, bksld.id) &&
                Objects.equals(solde, bksld.solde);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, solde);
    }
}