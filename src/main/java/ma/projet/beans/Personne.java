package ma.projet.beans;
import javax.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="personnes") @Inheritance(strategy=InheritanceType.JOINED)
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public abstract class Personne {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
    @Column(nullable=false) private String nom;
    @Column(nullable=false) private String prenom;
    private String telephone;
    private String adresse;
    private LocalDate dateNaissance;
    public Integer getId(){return id;}
    public String getNom(){return nom;} public void setNom(String value){nom=value;}
    public String getPrenom(){return prenom;} public void setPrenom(String value){prenom=value;}
    public String getTelephone(){return telephone;} public void setTelephone(String value){telephone=value;}
    public String getAdresse(){return adresse;} public void setAdresse(String value){adresse=value;}
    public LocalDate getDateNaissance(){return dateNaissance;} public void setDateNaissance(LocalDate value){dateNaissance=value;}
    @Override public String toString(){return nom+" "+prenom+" ("+dateNaissance+")";}
}
