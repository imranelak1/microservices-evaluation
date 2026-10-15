package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;

/** Service métier et requêtes de l’exercice. */
public class ProduitService extends AbstractService<Produit,Integer> {
    public ProduitService() { this(HibernateUtil.getSessionFactory()); }
    public ProduitService(EntityManagerFactory emf) { super(Produit.class,emf); }
    public List<Produit> parCategorie(Integer categorie) { return read(em->em.createQuery("select p from Produit p where p.categorie.id=:id order by p.id",Produit.class).setParameter("id",categorie).getResultList()); }
    public List<Produit> commandesEntre(LocalDate debut,LocalDate fin) {
        intervalle(debut,fin);return read(em->em.createQuery("select distinct p from LigneCommandeProduit l join l.produit p where l.commande.date between :debut and :fin order by p.id",Produit.class).setParameter("debut",debut).setParameter("fin",fin).getResultList());
    }
    public List<LigneCommandeProduit> dansCommande(Integer commande) { return read(em->em.createQuery("select l from LigneCommandeProduit l join fetch l.produit where l.commande.id=:id order by l.produit.id",LigneCommandeProduit.class).setParameter("id",commande).getResultList()); }
    public List<Produit> prixSuperieur100() { return read(em->em.createNamedQuery("Produit.prixSuperieur100",Produit.class).getResultList()); }
}
