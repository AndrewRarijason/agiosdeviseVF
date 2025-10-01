package mg.bmoi.agiosdevise.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_APP", schema = "C##MGSTGADR")
public class UserApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "USERNAME")
    private String username;

    private String email_bmoi;

    private String mdp;

    @ManyToOne
    @JoinColumn(name = "role", referencedColumnName = "id_role")
    private Role role;

    @Temporal(TemporalType.DATE)
    private Date created_at;

    private int is_active;

    public UserApp() {
    }

    public UserApp(int id, String username, String email_bmoi, String mdp, Role role, Date created_at, int is_active) {
        this.id= id;
        this.username = username;
        this.email_bmoi = email_bmoi;
        this.mdp = mdp;
        this.role = role;
        this.created_at = created_at;
        this.is_active = is_active;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail_bmoi() { return email_bmoi; }
    public void setEmail_bmoi(String email_bmoi) { this.email_bmoi = email_bmoi; }

    public String getMdp() { return mdp; }
    public void setMdp(String mdp) { this.mdp = mdp; }

    public Role getRole() {return role;}
    public void setRole(Role role) {this.role = role;}

    public Date getCreated_at() { return created_at; }
    public void setCreated_at(Date created_at) { this.created_at = created_at; }

    public int getIs_active() { return is_active; }
    public void setIs_active(int is_active) { this.is_active = is_active; }
}