package ma.projet.service;

import ma.projet.classes.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.*;

/** Service métier et requêtes de l’exercice. */
public class EmployeTacheService extends AbstractService<EmployeTache, EmployeTacheId> {
  public EmployeTacheService() {
    this(HibernateUtil.getSessionFactory());
  }

  public EmployeTacheService(EntityManagerFactory emf) {
    super(EmployeTache.class, emf);
  }

  @Override
  protected void attachAssociations(EntityManager em, EmployeTache affectation) {
    affectation.setEmploye(em.getReference(Employe.class, affectation.getEmploye().getId()));
    affectation.setTache(em.getReference(Tache.class, affectation.getTache().getId()));
  }
}
