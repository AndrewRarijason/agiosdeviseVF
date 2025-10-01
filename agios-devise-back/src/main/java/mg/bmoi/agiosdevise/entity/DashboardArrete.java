package mg.bmoi.agiosdevise.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "DASHBOARD_ARRETE", schema = "C##MGSTGADR")
public class DashboardArrete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TOT_COMPTES_TRAITES")
    private Integer nbTotalComptes;

    @Column(name = "NB_COMPTE_KO")
    private Integer nbCompteKo;

    @Column(name = "DATE_DEBUT_ARRETE")
    private Date dateDebutArrete;

    @Column(name = "DATE_FIN_ARRETE")
    private Date dateFinArrete;

    @Column(name = "DATE_HEURE_DEBUT_CALCUL")
    private LocalDateTime dateHeureDebutCalcul;

    @Column(name="DATE_HEURE_FIN_CALCUL")
    private LocalDateTime dateHeureFinCalcul;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Integer getNbTotalComptes() {return nbTotalComptes;}
    public void setNbTotalComptes(Integer nbTotalComptes) {this.nbTotalComptes = nbTotalComptes;}

    public Integer getNbCompteKo() {return nbCompteKo;}
    public void setNbCompteKo(Integer nbCompteKo) {this.nbCompteKo = nbCompteKo;}

    public Date getDateDebuArrete() {return dateDebutArrete;}
    public void setDateDebuArrete(Date dateDebutArrete) {this.dateDebutArrete = dateDebutArrete;}

    public Date getDateFinArrete() {return dateFinArrete;}
    public void setDateFinArrete(Date dateFinArrete) {this.dateFinArrete = dateFinArrete;}

    public LocalDateTime getDateHeureDebutCalcul() {return dateHeureDebutCalcul;}
    public void setDateHeureDebutCalcul(LocalDateTime dateHeureDebutCalcul) {this.dateHeureDebutCalcul = dateHeureDebutCalcul;}

    public LocalDateTime getDateHeureFinCalcul() {return dateHeureFinCalcul;}
    public void setDateHeureFinCalcul(LocalDateTime dateHeureFinCalcul) {this.dateHeureFinCalcul = dateHeureFinCalcul;}
}