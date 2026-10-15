package ma.projet.classes;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity @Table(name="lignes_commande")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class LigneCommandeProduit {
    @EmbeddedId private LigneCommandeId id=new LigneCommandeId();
    @ManyToOne(fetch=FetchType.LAZY) @MapsId("commandeId") @JoinColumn(name="commande_id") private Commande commande;
    @ManyToOne(fetch=FetchType.LAZY) @MapsId("produitId") @JoinColumn(name="produit_id") private Produit produit;
    @Column(nullable=false) private int quantite;
    public LigneCommandeProduit() {}
    public LigneCommandeProduit(Commande commande,Produit produit,int quantite){this.commande=commande;this.produit=produit;this.quantite=quantite;}
    public LigneCommandeId getId() { return id; }
    public Commande getCommande() { return commande; }
    public void setCommande(Commande value) { commande=value; }
    public Produit getProduit() { return produit; }
    public void setProduit(Produit value) { produit=value; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int value) { quantite=value; }
}
