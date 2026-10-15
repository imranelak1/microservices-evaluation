package ma.projet.service;

import ma.projet.classes.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.*;

/** Service métier et requêtes de l’exercice. */
public class CommandeService extends AbstractService<Commande, Integer> {
  public CommandeService() {
    this(HibernateUtil.getSessionFactory());
  }

  public CommandeService(EntityManagerFactory emf) {
    super(Commande.class, emf);
  }

  @Override
  protected void attachAssociations(EntityManager em, Commande commande) {
    for (LigneCommandeProduit ligne : commande.getLignes())
      ligne.setProduit(em.getReference(Produit.class, ligne.getProduit().getId()));
  }

  public Commande avecProduits(Integer id) {
    return read(
        em ->
            em.createQuery(
                    "select distinct c from Commande c left join fetch c.lignes l left join fetch l.produit where c.id=:id",
                    Commande.class)
                .setParameter("id", id)
                .getSingleResult());
  }

  public java.math.BigDecimal montant(Integer id) {
    return avecProduits(id).getLignes().stream()
        .map(
            l ->
                new java.math.BigDecimal(Float.toString(l.getProduit().getPrix()))
                    .multiply(java.math.BigDecimal.valueOf(l.getQuantite())))
        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
  }
}
