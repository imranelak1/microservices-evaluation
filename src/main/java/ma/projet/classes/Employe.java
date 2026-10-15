package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "employes")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class Employe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String nom;

  @Column(nullable = false)
  private String prenom;

  private String telephone;

  @OneToMany(mappedBy = "chef")
  private List<Projet> projets = new ArrayList<>();

  @OneToMany(mappedBy = "employe")
  private List<EmployeTache> affectations = new ArrayList<>();

  public Employe() {}

  public Employe(String nom, String prenom, String telephone) {
    this.nom = nom;
    this.prenom = prenom;
    this.telephone = telephone;
  }

  public Integer getId() {
    return id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String value) {
    nom = value;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String value) {
    prenom = value;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String value) {
    telephone = value;
  }

  public List<Projet> getProjets() {
    return projets;
  }

  public List<EmployeTache> getAffectations() {
    return affectations;
  }
}
