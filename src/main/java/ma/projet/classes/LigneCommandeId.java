package ma.projet.classes;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class LigneCommandeId implements Serializable {
  @Column(name = "commande_id")
  private Integer commandeId;

  @Column(name = "produit_id")
  private Integer produitId;

  public LigneCommandeId() {}

  public LigneCommandeId(Integer commandeId, Integer produitId) {
    this.commandeId = commandeId;
    this.produitId = produitId;
  }

  public Integer getCommandeId() {
    return commandeId;
  }

  public Integer getProduitId() {
    return produitId;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof LigneCommandeId k
        && Objects.equals(commandeId, k.commandeId)
        && Objects.equals(produitId, k.produitId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commandeId, produitId);
  }
}
