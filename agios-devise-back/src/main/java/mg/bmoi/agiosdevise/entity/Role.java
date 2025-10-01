package mg.bmoi.agiosdevise.entity;

import javax.persistence.*;

@Entity
@Table(name = "ROLE", schema = "C##MGSTGADR")
public class Role {
    @Id
    @Column(name = "id_role")
    private int id;

    @Column(name = "nom_role")
    private String name;

    public Role() {
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
