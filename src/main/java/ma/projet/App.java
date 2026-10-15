package ma.projet;

import ma.projet.service.*;
import ma.projet.classes.*;
import ma.projet.beans.*;
import ma.projet.util.HibernateUtil;
import org.hibernate.SessionFactory;
import ma.projet.config.EvaluationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.time.LocalDate;
import java.util.Locale;

/** Démonstration des trois exercices avec les services obtenus depuis Spring IoC. */
public class App {
  public static void main(String[] args) {
    var context = new AnnotationConfigApplicationContext(EvaluationConfig.class);
    SessionFactory emf = context.getBean(SessionFactory.class);
    try {
      try (var session = emf.openSession()) {
        session.doWork(
            connection ->
                System.out.println(
                    "BASE REELLE : "
                        + connection.getMetaData().getDatabaseProductName()
                        + " "
                        + connection.getMetaData().getDatabaseProductVersion()));
      }
      var fixture = DemoData.seed(emf);
      System.out.println("EVALUATION — EXERCICE 1 : GESTION DE STOCK");
      ProduitService produits = context.getBean(ProduitService.class);
      System.out.println(
          "Produits par categorie : " + produits.parCategorie(fixture.categorie().getId()));
      System.out.println(
          "Commandes entre 01/03/2013 et 31/03/2013 : "
              + produits.commandesEntre(LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 31)));
      System.out.println(
          "Commande : " + fixture.commande().getId() + " Date : " + fixture.commande().getDate());
      System.out.println("Reference   Prix        Quantite");
      for (LigneCommandeProduit ligne : produits.dansCommande(fixture.commande().getId()))
        System.out.printf(
            Locale.ROOT,
            "%-11s %7.2f DH  %d%n",
            ligne.getProduit().getReference(),
            ligne.getProduit().getPrix(),
            ligne.getQuantite());
      System.out.println("Requete nommee prix >100 DH : " + produits.prixSuperieur100());
      System.out.println(
          "Montant commande : "
              + context.getBean(CommandeService.class).montant(fixture.commande().getId())
              + " DH");

      System.out.println("EVALUATION — EXERCICE 2 : GESTION DE PROJETS");
      ProjetService projets = context.getBean(ProjetService.class);
      EmployeService employes = context.getBean(EmployeService.class);
      TacheService taches = context.getBean(TacheService.class);
      System.out.println(
          "Projet : "
              + fixture.projet().getId()
              + " Nom : "
              + fixture.projet().getNom()
              + " Date debut : "
              + fixture.projet().getDateDebut());
      System.out.println(
          "Taches planifiees : "
              + projets.tachesPlanifiees(fixture.projet().getId()).stream()
                  .map(Tache::getNom)
                  .toList());
      System.out.println("Num Nom               Date debut reelle   Date fin reelle");
      for (EmployeTache affectation : projets.tachesRealisees(fixture.projet().getId()))
        System.out.printf(
            "%-3d %-18s %s          %s%n",
            affectation.getTache().getId(),
            affectation.getTache().getNom(),
            affectation.getDateDebutReelle(),
            affectation.getDateFinReelle());
      System.out.println(
          "Taches realisees employe : "
              + employes.tachesRealisees(fixture.employe().getId()).stream()
                  .map(Tache::getNom)
                  .toList());
      System.out.println(
          "Projets geres employe : "
              + employes.projetsGeres(fixture.employe().getId()).stream()
                  .map(Projet::getNom)
                  .toList());
      System.out.println(
          "Requete nommee taches >1000 DH : "
              + taches.prixSuperieur1000().stream().map(Tache::getNom).toList());
      System.out.println(
          "Taches realisees fevrier-mars : "
              + taches.realiseesEntre(LocalDate.of(2013, 2, 1), LocalDate.of(2013, 3, 31)).stream()
                  .map(Tache::getNom)
                  .toList());
      System.out.println(
          "Cout planifie : "
              + projets.coutPlanifie(fixture.projet().getId())
              + " DH ; cout realise : "
              + projets.coutRealise(fixture.projet().getId())
              + " DH ; jours passes : "
              + employes.joursRealises(fixture.employe().getId()));

      System.out.println("EVALUATION — EXERCICE 3 : ETAT CIVIL");
      FemmeService femmes = context.getBean(FemmeService.class);
      HommeService hommes = context.getBean(HommeService.class);
      System.out.println(
          "Population : "
              + femmes.findAll().size()
              + " femmes, "
              + hommes.findAll().size()
              + " hommes");
      System.out.println("Liste des femmes : " + femmes.findAll());
      System.out.println("Femme la plus agee : " + femmes.plusAgee());
      Integer homme = fixture.hommes().get(0).getId();
      LocalDate debut = LocalDate.of(2000, 1, 1), fin = LocalDate.of(2020, 12, 31);
      System.out.println(
          "Epouses de SAFI Said (2000..2020) : " + hommes.epousesEntre(homme, debut, fin));
      System.out.println(
          "Requete native nommee enfants de RAMI Salima : "
              + femmes.nombreEnfants(fixture.femmes().get(1).getId(), debut, fin));
      System.out.println(
          "Requete nommee femmes mariees >=2 fois : " + femmes.marieesAuMoinsDeuxFois());
      System.out.println(
          "Criteria nombre hommes maries a 4 femmes (2000..2020) : "
              + hommes.nombreMariesAQuatreFemmes(debut, fin));
      var mariages = hommes.mariagesDetailles(homme);
      System.out.println("Nom : SAFI Said — Mariages en cours :");
      mariages.stream().filter(m -> m.getDateFin() == null).forEach(App::afficherMariage);
      System.out.println("Mariages echoues :");
      mariages.stream().filter(m -> m.getDateFin() != null).forEach(App::afficherMariage);
      require(produits.prixSuperieur100().size() == 2, "stock prix");
      require(projets.tachesRealisees(fixture.projet().getId()).size() == 3, "taches realisees");
      require(femmes.findAll().size() == 10 && hommes.findAll().size() == 5, "population");
      require(hommes.nombreMariesAQuatreFemmes(debut, fin) == 1, "Criteria quatre epouses");
      require(
          femmes.nombreEnfants(fixture.femmes().get(1).getId(), debut, fin) == 4,
          "requete native enfants");
      System.out.println("VERIFICATION BASE : toutes les invariants metier sont satisfaits.");
    } finally {
      context.close();
      com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
    }
  }

  private static void afficherMariage(Mariage mariage) {
    System.out.println(
        "Femme : "
            + mariage.getFemme().getNom()
            + " "
            + mariage.getFemme().getPrenom()
            + " Date debut : "
            + mariage.getDateDebut()
            + " Date fin : "
            + mariage.getDateFin()
            + " Nbr enfants : "
            + mariage.getNbrEnfant());
  }

  private static void require(boolean ok, String message) {
    if (!ok) throw new IllegalStateException("Verification echouee : " + message);
  }
}
