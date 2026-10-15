package ma.projet.classes;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity @Table(name="categories")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class Categorie {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
    @Column(nullable=false,unique=true) private String code;
    @Column(nullable=false) private String libelle;
    @OneToMany(mappedBy="categorie") private List<Produit> produits=new ArrayList<>();
    public Categorie() {}
    public Categorie(String code,String libelle) {this.code=code;this.libelle=libelle;}
    public Integer getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String value) { code=value; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String value) { libelle=value; }
    public List<Produit> getProduits() { return produits; }
}
