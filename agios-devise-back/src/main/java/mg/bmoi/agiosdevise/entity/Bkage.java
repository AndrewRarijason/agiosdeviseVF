package mg.bmoi.agiosdevise.entity;

import javax.persistence.*;

@Entity
@Table(name = "BKAGE", schema = "C##MGSTGADR")
public class Bkage {

    @Id
    @Column (name = "AGE")
    private String age;

    @Column (name = "LIB")
    private String lib;

    public Bkage() {
    }

    public Bkage(String age, String lib) {
        this.age = age;
        this.lib = lib;
    }

    public String getAge() {return age;}
    public void setAge(String age) {this.age = age;}

    public String getLib() {return lib;}
    public void setLib(String lib) {this.lib = lib;}
}
