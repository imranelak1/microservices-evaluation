package ma.projet.service;

import ma.projet.classes.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/** Service métier et requêtes de l’exercice. */
public class EmployeService extends AbstractService<Employe, Integer> {
  public EmployeService() {
    this(HibernateUtil.getSessionFactory());
  }

  public EmployeService(EntityManagerFactory emf) {
    super(Employe.class, emf);
  }

  public List<Tache> tachesRealisees(Integer employe) {
    return read(
        em ->
            em.createQuery(
                    "select a.tache from EmployeTache a where a.employe.id=:id and a.dateFinReelle is not null order by a.tache.id",
                    Tache.class)
                .setParameter("id", employe)
                .getResultList());
  }

  public List<Projet> projetsGeres(Integer employe) {
    return read(
        em ->
            em.createQuery("select p from Projet p where p.chef.id=:id order by p.id", Projet.class)
                .setParameter("id", employe)
                .getResultList());
  }

  public long joursRealises(Integer employe) {
    return read(
        em ->
            em
                .createQuery(
                    "select a from EmployeTache a where a.employe.id=:id and a.dateFinReelle is not null",
                    EmployeTache.class)
                .setParameter("id", employe)
                .getResultList()
                .stream()
                .mapToLong(
                    a ->
                        java.time.temporal.ChronoUnit.DAYS.between(
                            a.getDateDebutReelle(), a.getDateFinReelle()))
                .sum());
  }
}
