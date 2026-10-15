package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "commandes")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class Commande {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "date_commande", nullable = false)
  private LocalDate date;

  @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<LigneCommandeProduit> lignes = new ArrayList<>();

  public Commande() {}

  public Commande(LocalDate date) {
    this.date = date;
  }

  public Integer getId() {
    return id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate value) {
    date = value;
  }

  public List<LigneCommandeProduit> getLignes() {
    return lignes;
  }

  public void ajouterProduit(Produit produit, int quantite) {
    if (quantite <= 0) throw new IllegalArgumentException("Quantite positive requise");
    if (lignes.stream().anyMatch(l -> Objects.equals(l.getProduit().getId(), produit.getId())))
      throw new IllegalArgumentException("Produit duplique");
    lignes.add(new LigneCommandeProduit(this, produit, quantite));
  }
}
