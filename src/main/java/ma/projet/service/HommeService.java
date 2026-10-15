package ma.projet.service;
import ma.projet.beans.*;
import ma.projet.util.HibernateUtil;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

/** Service métier et requêtes de l’exercice. */
public class HommeService extends AbstractService<Homme,Integer> {
    public HommeService() { this(HibernateUtil.getSessionFactory()); }
    public HommeService(EntityManagerFactory emf) { super(Homme.class,emf); }
    public List<Femme> epousesEntre(Integer homme,LocalDate debut,LocalDate fin) {
        intervalle(debut,fin);return read(em->em.createQuery("select distinct f from Mariage m join m.femme f where m.homme.id=:id and m.id.dateDebut<=:fin and (m.dateFin is null or m.dateFin>=:debut) order by f.id",Femme.class).setParameter("id",homme).setParameter("debut",debut).setParameter("fin",fin).getResultList());
    }
    public long nombreMariesAQuatreFemmes(LocalDate debut,LocalDate fin) {
        intervalle(debut,fin);return read(em->{
            CriteriaBuilder cb=em.getCriteriaBuilder();CriteriaQuery<Long> cq=cb.createQuery(Long.class);Root<Homme> homme=cq.from(Homme.class);
            Subquery<Long> nb=cq.subquery(Long.class);Root<Mariage> mariage=nb.from(Mariage.class);
            nb.select(cb.countDistinct(mariage.get("femme").get("id")));
            nb.where(cb.equal(mariage.get("homme"),homme),cb.lessThanOrEqualTo(mariage.get("id").get("dateDebut"),fin),cb.or(cb.isNull(mariage.get("dateFin")),cb.greaterThanOrEqualTo(mariage.get("dateFin"),debut)));
            cq.select(cb.count(homme)).where(cb.equal(nb,4L));return em.createQuery(cq).getSingleResult();
        });
    }
    public List<Mariage> mariagesDetailles(Integer homme) { return read(em->em.createQuery("select m from Mariage m join fetch m.homme join fetch m.femme where m.homme.id=:id order by m.id.dateDebut",Mariage.class).setParameter("id",homme).getResultList()); }
}
