package ma.projet.service;

import ma.projet.classes.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;

/** Service métier et requêtes de l’exercice. */
public class TacheService extends AbstractService<Tache, Integer> {
  public TacheService() {
    this(HibernateUtil.getSessionFactory());
  }

  public TacheService(EntityManagerFactory emf) {
    super(Tache.class, emf);
  }

  public List<Tache> prixSuperieur1000() {
    return read(em -> em.createNamedQuery("Tache.prixSuperieur1000", Tache.class).getResultList());
  }

  public List<Tache> realiseesEntre(LocalDate debut, LocalDate fin) {
    intervalle(debut, fin);
    return read(
        em ->
            em.createQuery(
                    "select distinct t from EmployeTache a join a.tache t where a.dateDebutReelle>=:debut and a.dateFinReelle<=:fin and a.dateFinReelle is not null order by t.id",
                    Tache.class)
                .setParameter("debut", debut)
                .setParameter("fin", fin)
                .getResultList());
  }
}
