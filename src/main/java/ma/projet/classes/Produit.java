package ma.projet.classes;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity @Table(name="produits")
@NamedQuery(name="Produit.prixSuperieur100",query="select p from Produit p where p.prix>100 order by p.id")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class Produit {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
    @Column(nullable=false,unique=true) private String reference;
    @Column(nullable=false) private float prix;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="categorie_id",nullable=false) private Categorie categorie;
    public Produit() {}
    public Produit(String reference,float prix,Categorie categorie){if(prix<0)throw new IllegalArgumentException("Prix negatif");this.reference=reference;this.prix=prix;this.categorie=categorie;}
    public Integer getId() { return id; }
    public String getReference() { return reference; }
    public void setReference(String value) { reference=value; }
    public float getPrix() { return prix; }
    public void setPrix(float value) { prix=value; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie value) { categorie=value; }
    @Override public String toString(){return reference+" "+prix+" DH";}
}
