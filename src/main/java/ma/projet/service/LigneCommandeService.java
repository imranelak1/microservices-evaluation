package ma.projet.service;
import ma.projet.classes.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.*;

/** Service métier et requêtes de l’exercice. */
public class LigneCommandeService extends AbstractService<LigneCommandeProduit,LigneCommandeId> {
    public LigneCommandeService() { this(HibernateUtil.getSessionFactory()); }
    public LigneCommandeService(EntityManagerFactory emf) { super(LigneCommandeProduit.class,emf); }
    @Override protected void attachAssociations(EntityManager em,LigneCommandeProduit ligne) { ligne.setProduit(em.getReference(Produit.class,ligne.getProduit().getId()));ligne.setCommande(em.getReference(Commande.class,ligne.getCommande().getId())); }
}
