package mg.bmoi.agiosdevise.entity;

import mg.bmoi.agiosdevise.entity.identifiants.BkcomId;

import javax.persistence.*;

@Entity
@Table(name = "BKCOM", schema = "BANK")
public class Bkcom {

    @EmbeddedId
    private BkcomId id;
    private String cha;
    private String cpro;

    // Getters and setters
    public BkcomId getId() { return id; }
    public void setId(BkcomId id) { this.id = id; }

    public String getCha() { return cha; }
    public void setCha(String cha) { this.cha = cha; }

    public String getCpro() { return cpro; }
    public void setCpro(String cpro) { this.cpro = cpro; }
}