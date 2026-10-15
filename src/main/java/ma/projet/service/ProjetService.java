package ma.projet.service;

import ma.projet.classes.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.*;
import java.util.List;

/** Service métier et requêtes de l’exercice. */
public class ProjetService extends AbstractService<Projet, Integer> {
  public ProjetService() {
    this(HibernateUtil.getSessionFactory());
  }

  public ProjetService(EntityManagerFactory emf) {
    super(Projet.class, emf);
  }

  public List<Tache> tachesPlanifiees(Integer projet) {
    return read(
        em ->
            em.createQuery("select t from Tache t where t.projet.id=:id order by t.id", Tache.class)
                .setParameter("id", projet)
                .getResultList());
  }

  public List<EmployeTache> tachesRealisees(Integer projet) {
    return read(
        em ->
            em.createQuery(
                    "select a from EmployeTache a join fetch a.tache join fetch a.employe where a.tache.projet.id=:id and a.dateFinReelle is not null order by a.tache.id",
                    EmployeTache.class)
                .setParameter("id", projet)
                .getResultList());
  }

  public double coutPlanifie(Integer projet) {
    return tachesPlanifiees(projet).stream().mapToDouble(Tache::getPrix).sum();
  }

  public double coutRealise(Integer projet) {
    return tachesRealisees(projet).stream()
        .map(EmployeTache::getTache)
        .collect(java.util.stream.Collectors.toMap(Tache::getId, Tache::getPrix, (a, b) -> a))
        .values()
        .stream()
        .mapToDouble(Double::doubleValue)
        .sum();
  }
}
