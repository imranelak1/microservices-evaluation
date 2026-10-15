package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "employe_tache")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class EmployeTache {
  @EmbeddedId private EmployeTacheId id = new EmployeTacheId();

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("employeId")
  @JoinColumn(name = "employe_id")
  private Employe employe;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("tacheId")
  @JoinColumn(name = "tache_id")
  private Tache tache;

  private LocalDate dateDebutReelle;
  private LocalDate dateFinReelle;

  public EmployeTache() {}

  public EmployeTache(Employe employe, Tache tache, LocalDate debut, LocalDate fin) {
    if (fin != null && (debut == null || fin.isBefore(debut)))
      throw new IllegalArgumentException("Dates reelles invalides");
    this.employe = employe;
    this.tache = tache;
    this.dateDebutReelle = debut;
    this.dateFinReelle = fin;
  }

  public EmployeTacheId getId() {
    return id;
  }

  public Employe getEmploye() {
    return employe;
  }

  public void setEmploye(Employe value) {
    employe = value;
  }

  public Tache getTache() {
    return tache;
  }

  public void setTache(Tache value) {
    tache = value;
  }

  public LocalDate getDateDebutReelle() {
    return dateDebutReelle;
  }

  public void setDateDebutReelle(LocalDate value) {
    dateDebutReelle = value;
  }

  public LocalDate getDateFinReelle() {
    return dateFinReelle;
  }

  public void setDateFinReelle(LocalDate value) {
    dateFinReelle = value;
  }
}
