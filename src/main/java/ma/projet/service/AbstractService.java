package ma.projet.service;
import ma.projet.dao.IDao;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

/** Service métier et requêtes de l’exercice. */
public abstract class AbstractService<T,ID extends Serializable> implements IDao<T,ID> {
    protected final EntityManagerFactory emf;
    protected final Class<T> type;
    protected AbstractService(Class<T> type,EntityManagerFactory emf) { this.type=type;this.emf=emf; }
    public <R> R read(Function<EntityManager,R> work) { EntityManager em=emf.createEntityManager();try{return work.apply(em);}finally{em.close();} }
    public <R> R transaction(Function<EntityManager,R> work) {
        EntityManager em=emf.createEntityManager();EntityTransaction tx=em.getTransaction();
        try { tx.begin();R result=work.apply(em);tx.commit();return result; }
        catch(RuntimeException e) { if(tx.isActive())tx.rollback();throw e; }
        finally { em.close(); }
    }
    protected void attachAssociations(EntityManager em,T entity) {}
    @Override public T create(T entity) { Objects.requireNonNull(entity);return transaction(em->{attachAssociations(em,entity);em.persist(entity);return entity;}); }
    @Override public T update(T entity) { Objects.requireNonNull(entity);return transaction(em->em.merge(entity)); }
    @Override public void delete(T entity) { Objects.requireNonNull(entity);transaction(em->{em.remove(em.merge(entity));return null;}); }
    @Override public T findById(ID id) { return read(em->em.find(type,id)); }
    @Override public List<T> findAll() { return read(em->em.createQuery("select e from "+type.getSimpleName()+" e",type).getResultList()); }
    protected static void intervalle(LocalDate debut,LocalDate fin) { if(debut==null||fin==null||fin.isBefore(debut))throw new IllegalArgumentException("Intervalle de dates invalide"); }
}
