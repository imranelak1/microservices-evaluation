package ma.projet.config;

import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.*;
import org.hibernate.SessionFactory;

/** Spring IoC construit les services et injecte une factory partagée entre les trois exercices. */
@Configuration
public class EvaluationConfig {
  @Bean(destroyMethod = "close")
  public SessionFactory sessionFactory() {
    return HibernateUtil.getSessionFactory();
  }

  @Bean
  public ProduitService produitService(SessionFactory factory) {
    return new ProduitService(factory);
  }

  @Bean
  public CategorieService categorieService(SessionFactory factory) {
    return new CategorieService(factory);
  }

  @Bean
  public CommandeService commandeService(SessionFactory factory) {
    return new CommandeService(factory);
  }

  @Bean
  public LigneCommandeService ligneCommandeService(SessionFactory factory) {
    return new LigneCommandeService(factory);
  }

  @Bean
  public ProjetService projetService(SessionFactory factory) {
    return new ProjetService(factory);
  }

  @Bean
  public TacheService tacheService(SessionFactory factory) {
    return new TacheService(factory);
  }

  @Bean
  public EmployeService employeService(SessionFactory factory) {
    return new EmployeService(factory);
  }

  @Bean
  public EmployeTacheService employeTacheService(SessionFactory factory) {
    return new EmployeTacheService(factory);
  }

  @Bean
  public HommeService hommeService(SessionFactory factory) {
    return new HommeService(factory);
  }

  @Bean
  public FemmeService femmeService(SessionFactory factory) {
    return new FemmeService(factory);
  }

  @Bean
  public MariageService mariageService(SessionFactory factory) {
    return new MariageService(factory);
  }
}
