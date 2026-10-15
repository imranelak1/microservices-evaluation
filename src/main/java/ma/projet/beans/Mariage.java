package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "mariages")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class Mariage {
  @EmbeddedId private MariageId id = new MariageId();

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("hommeId")
  @JoinColumn(name = "homme_id")
  private Homme homme;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("femmeId")
  @JoinColumn(name = "femme_id")
  private Femme femme;

  @Column(name = "date_fin")
  private LocalDate dateFin;

  @Column(name = "nbr_enfant", nullable = false)
  private int nbrEnfant;

  public Mariage() {}

  public Mariage(Homme homme, Femme femme, LocalDate debut, LocalDate fin, int enfants) {
    if (enfants < 0 || debut == null || (fin != null && fin.isBefore(debut)))
      throw new IllegalArgumentException("Mariage invalide");
    this.homme = homme;
    this.femme = femme;
    this.id = new MariageId(homme.getId(), femme.getId(), debut);
    this.dateFin = fin;
    this.nbrEnfant = enfants;
  }

  public MariageId getId() {
    return id;
  }

  public Homme getHomme() {
    return homme;
  }

  public void setHomme(Homme value) {
    homme = value;
  }

  public Femme getFemme() {
    return femme;
  }

  public void setFemme(Femme value) {
    femme = value;
  }

  public LocalDate getDateFin() {
    return dateFin;
  }

  public void setDateFin(LocalDate value) {
    dateFin = value;
  }

  public int getNbrEnfant() {
    return nbrEnfant;
  }

  public void setNbrEnfant(int value) {
    nbrEnfant = value;
  }

  public LocalDate getDateDebut() {
    return id.getDateDebut();
  }
}
