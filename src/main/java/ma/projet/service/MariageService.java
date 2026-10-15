package ma.projet.service;

import ma.projet.beans.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.*;

/** Service métier et requêtes de l’exercice. */
public class MariageService extends AbstractService<Mariage, MariageId> {
  public MariageService() {
    this(HibernateUtil.getSessionFactory());
  }

  public MariageService(EntityManagerFactory emf) {
    super(Mariage.class, emf);
  }

  @Override
  protected void attachAssociations(EntityManager em, Mariage mariage) {
    mariage.setHomme(em.getReference(Homme.class, mariage.getHomme().getId()));
    mariage.setFemme(em.getReference(Femme.class, mariage.getFemme().getId()));
  }
}
