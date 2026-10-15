package ma.projet;

import ma.projet.beans.*;
import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.junit.Assert.*;

public class EvaluationPersistenceTest {
  private static SessionFactory emf;
  private DemoData.Fixture fixture;

  @BeforeClass
  public static void open() {
    emf = HibernateUtil.build("evaluation_test", true);
  }

  @Before
  public void init() {
    // Une factory pour la classe ; seules les données sont isolées entre les cas.
    try (var session = emf.openSession()) {
      var tx = session.beginTransaction();
      for (String entity :
          List.of(
              "EmployeTache",
              "LigneCommandeProduit",
              "Mariage",
              "Tache",
              "Projet",
              "Employe",
              "Commande",
              "Produit",
              "Categorie",
              "Femme",
              "Homme")) session.createQuery("delete from " + entity).executeUpdate();
      tx.commit();
    }
    fixture = DemoData.seed(emf);
  }

  @AfterClass
  public static void close() {
    emf.close();
  }

  @Test
  public void stockCategorieEtRequeteNommeeSeuilStrict() {
    ProduitService service = new ProduitService(emf);
    assertEquals(3, service.parCategorie(fixture.categorie().getId()).size());
    assertEquals(
        List.of("ES12", "EE85"),
        service.prixSuperieur100().stream().map(Produit::getReference).toList());
    assertEquals(0, service.parCategorie(-1).size());
  }

  @Test
  public void stockDatesInclusivesSansDoublons() {
    ProduitService service = new ProduitService(emf);
    assertEquals(
        3, service.commandesEntre(LocalDate.of(2013, 3, 14), LocalDate.of(2013, 3, 14)).size());
    assertEquals(
        4, service.commandesEntre(LocalDate.of(2013, 3, 1), LocalDate.of(2013, 4, 1)).size());
    assertEquals(
        0, service.commandesEntre(LocalDate.of(2013, 2, 1), LocalDate.of(2013, 2, 28)).size());
    assertThrows(
        IllegalArgumentException.class,
        () -> service.commandesEntre(LocalDate.of(2013, 4, 1), LocalDate.of(2013, 3, 1)));
  }

  @Test
  public void stockCommandeAfficheQuantitesEtMontant() {
    var lignes = new ProduitService(emf).dansCommande(fixture.commande().getId());
    assertEquals(
        List.of(7, 14, 5), lignes.stream().map(LigneCommandeProduit::getQuantite).toList());
    assertEquals("ES12", lignes.get(0).getProduit().getReference());
    assertEquals(
        0,
        new BigDecimal("3240")
            .compareTo(new CommandeService(emf).montant(fixture.commande().getId())));
  }

  @Test
  public void stockCleComposeeCRUDPersistant() {
    var service = new LigneCommandeService(emf);
    var ligne = new ProduitService(emf).dansCommande(fixture.commande().getId()).get(0);
    assertNotNull(service.findById(ligne.getId()));
    ligne.setQuantite(8);
    service.update(ligne);
    assertEquals(8, service.findById(ligne.getId()).getQuantite());
    service.delete(ligne);
    assertNull(service.findById(ligne.getId()));
    assertThrows(
        IllegalArgumentException.class,
        () -> fixture.commande().ajouterProduit(ligne.getProduit(), 0));
  }

  @Test
  public void projetsEmployeTachesEtProjetsGeres() {
    EmployeService service = new EmployeService(emf);
    assertEquals(3, service.tachesRealisees(fixture.employe().getId()).size());
    assertEquals(1, service.projetsGeres(fixture.employe().getId()).size());
    assertEquals(30, service.joursRealises(fixture.employe().getId()));
  }

  @Test
  public void projetsPlanificationDatesReellesEtCouts() {
    ProjetService service = new ProjetService(emf);
    Integer id = fixture.projet().getId();
    assertEquals(4, service.tachesPlanifiees(id).size());
    var realisees = service.tachesRealisees(id);
    assertEquals(3, realisees.size());
    assertEquals(LocalDate.of(2013, 2, 10), realisees.get(0).getDateDebutReelle());
    assertEquals(LocalDate.of(2013, 2, 20), realisees.get(0).getDateFinReelle());
    assertEquals(5500, service.coutPlanifie(id), 0.001);
    assertEquals(4700, service.coutRealise(id), 0.001);
  }

  @Test
  public void projetsRequeteNommeeEtDatesInclusives() {
    TacheService service = new TacheService(emf);
    assertEquals(
        List.of("Analyse", "Developpement"),
        service.prixSuperieur1000().stream().map(Tache::getNom).toList());
    assertEquals(
        2, service.realiseesEntre(LocalDate.of(2013, 2, 10), LocalDate.of(2013, 3, 15)).size());
    assertEquals(
        0, service.realiseesEntre(LocalDate.of(2013, 5, 1), LocalDate.of(2013, 5, 31)).size());
  }

  @Test
  public void projetsAssociationCompositeEtDatesInvalides() {
    var association = new ProjetService(emf).tachesRealisees(fixture.projet().getId()).get(0);
    assertEquals(
        association.getDateDebutReelle(),
        new EmployeTacheService(emf).findById(association.getId()).getDateDebutReelle());
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new EmployeTache(
                fixture.employe(),
                association.getTache(),
                LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 2, 1)));
  }

  @Test
  public void civilPopulationHeritageEtFemmePlusAgee() {
    FemmeService service = new FemmeService(emf);
    assertEquals(10, service.findAll().size());
    assertEquals(5, new HommeService(emf).findAll().size());
    assertEquals("Karima", service.plusAgee().getPrenom());
    assertEquals("Casablanca", service.findById(fixture.femmes().get(0).getId()).getAdresse());
  }

  @Test
  public void civilEpousesSurPeriodeEtHistoriqueComplet() {
    HommeService service = new HommeService(emf);
    Integer id = fixture.hommes().get(0).getId();
    assertEquals(
        4, service.epousesEntre(id, LocalDate.of(2000, 1, 1), LocalDate.of(2020, 12, 31)).size());
    assertEquals(
        0, service.epousesEntre(id, LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31)).size());
    var mariages = service.mariagesDetailles(id);
    assertEquals(5, mariages.size());
    assertEquals(1, mariages.stream().filter(m -> m.getDateFin() != null).count());
    assertEquals("Karima", mariages.get(0).getFemme().getPrenom());
  }

  @Test
  public void civilRequeteNativeNommeeEnfantsEtAbsence() {
    FemmeService service = new FemmeService(emf);
    assertEquals(
        4,
        service.nombreEnfants(
            fixture.femmes().get(1).getId(), LocalDate.of(2000, 1, 1), LocalDate.of(2020, 12, 31)));
    assertEquals(
        0,
        service.nombreEnfants(
            fixture.femmes().get(9).getId(), LocalDate.of(2000, 1, 1), LocalDate.of(2020, 12, 31)));
    assertEquals(
        2,
        service.nombreEnfants(
            fixture.femmes().get(0).getId(), LocalDate.of(1992, 1, 1), LocalDate.of(1999, 12, 31)));
  }

  @Test
  public void civilRequeteNommeeFemmesMarieesAuMoinsDeuxFois() {
    var femmes = new FemmeService(emf).marieesAuMoinsDeuxFois();
    assertEquals(1, femmes.size());
    assertEquals("Karima", femmes.get(0).getPrenom());
  }

  @Test
  public void civilCriteriaQuatreEpousesCompteDesHommesDistincts() {
    HommeService service = new HommeService(emf);
    assertEquals(
        1, service.nombreMariesAQuatreFemmes(LocalDate.of(2000, 1, 1), LocalDate.of(2020, 12, 31)));
    assertEquals(
        0, service.nombreMariesAQuatreFemmes(LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31)));
    assertEquals(
        0, service.nombreMariesAQuatreFemmes(LocalDate.of(1995, 1, 1), LocalDate.of(1995, 12, 31)));
  }

  @Test
  public void transactionAnnuleeNeCreeAucunCitoyenEtFermeLaSession() {
    FemmeService service = new FemmeService(emf);
    long open = emf.getStatistics().getSessionOpenCount(),
        closed = emf.getStatistics().getSessionCloseCount();
    assertThrows(
        IllegalStateException.class,
        () ->
            service.transaction(
                em -> {
                  em.persist(new Femme("Annulee", "Test", LocalDate.of(1970, 1, 1)));
                  throw new IllegalStateException("Erreur simulee");
                }));
    assertEquals(10, service.findAll().size());
    assertEquals(
        emf.getStatistics().getSessionOpenCount() - open,
        emf.getStatistics().getSessionCloseCount() - closed);
  }
}
