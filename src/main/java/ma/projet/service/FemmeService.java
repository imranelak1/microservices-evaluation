package ma.projet.service;

import ma.projet.beans.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;

/** Service métier et requêtes de l’exercice. */
public class FemmeService extends AbstractService<Femme, Integer> {
  public FemmeService() {
    this(HibernateUtil.getSessionFactory());
  }

  public FemmeService(EntityManagerFactory emf) {
    super(Femme.class, emf);
  }

  @Override
  public List<Femme> findAll() {
    return read(
        em -> em.createQuery("select f from Femme f order by f.id", Femme.class).getResultList());
  }

  public Femme plusAgee() {
    return read(
        em ->
            em.createQuery(
                    "select f from Femme f where f.dateNaissance is not null order by f.dateNaissance,f.id",
                    Femme.class)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null));
  }

  public long nombreEnfants(Integer femme, LocalDate debut, LocalDate fin) {
    intervalle(debut, fin);
    return read(
        em ->
            ((Number)
                    em.createNamedQuery("Femme.nombreEnfants")
                        .setParameter("femmeId", femme)
                        .setParameter("debut", java.sql.Date.valueOf(debut))
                        .setParameter("fin", java.sql.Date.valueOf(fin))
                        .getSingleResult())
                .longValue());
  }

  public List<Femme> marieesAuMoinsDeuxFois() {
    return read(
        em -> em.createNamedQuery("Femme.marieesAuMoinsDeuxFois", Femme.class).getResultList());
  }
}
