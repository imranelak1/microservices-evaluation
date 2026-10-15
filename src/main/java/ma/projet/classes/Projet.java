package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "projets")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class Projet {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String nom;

  private LocalDate dateDebut;
  private LocalDate dateFin;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chef_id", nullable = false)
  private Employe chef;

  @OneToMany(mappedBy = "projet")
  private List<Tache> taches = new ArrayList<>();

  public Projet() {}

  public Projet(String nom, LocalDate dateDebut, LocalDate dateFin, Employe chef) {
    this.nom = nom;
    this.dateDebut = dateDebut;
    this.dateFin = dateFin;
    this.chef = chef;
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

  public LocalDate getDateDebut() {
    return dateDebut;
  }

  public void setDateDebut(LocalDate value) {
    dateDebut = value;
  }

  public LocalDate getDateFin() {
    return dateFin;
  }

  public void setDateFin(LocalDate value) {
    dateFin = value;
  }

  public Employe getChef() {
    return chef;
  }

  public void setChef(Employe value) {
    chef = value;
  }

  public List<Tache> getTaches() {
    return taches;
  }
}
