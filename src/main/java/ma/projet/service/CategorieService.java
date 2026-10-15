package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.util.HibernateUtil;
import javax.persistence.EntityManagerFactory;

/** Service métier et requêtes de l’exercice. */
public class CategorieService extends AbstractService<Categorie, Integer> {
  public CategorieService() {
    this(HibernateUtil.getSessionFactory());
  }

  public CategorieService(EntityManagerFactory emf) {
    super(Categorie.class, emf);
  }
}
