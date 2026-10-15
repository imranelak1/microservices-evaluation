package ma.projet.classes;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity @Table(name="taches")
@NamedQuery(name="Tache.prixSuperieur1000",query="select t from Tache t where t.prix>1000 order by t.id")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class Tache {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
    @Column(nullable=false) private String nom;
     private LocalDate dateDebut;
     private LocalDate dateFin;
    @Column(nullable=false) private double prix;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="projet_id",nullable=false) private Projet projet;
    @OneToMany(mappedBy="tache") private List<EmployeTache> affectations=new ArrayList<>();
    public Tache() {}
    public Tache(String nom,LocalDate dateDebut,LocalDate dateFin,double prix,Projet projet){this.nom=nom;this.dateDebut=dateDebut;this.dateFin=dateFin;this.prix=prix;this.projet=projet;}
    public Integer getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String value) { nom=value; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate value) { dateDebut=value; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate value) { dateFin=value; }
    public double getPrix() { return prix; }
    public void setPrix(double value) { prix=value; }
    public Projet getProjet() { return projet; }
    public void setProjet(Projet value) { projet=value; }
    public List<EmployeTache> getAffectations() { return affectations; }
}
