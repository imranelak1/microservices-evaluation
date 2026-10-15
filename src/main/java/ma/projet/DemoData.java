package ma.projet;
import ma.projet.classes.*;
import ma.projet.beans.*;
import ma.projet.service.*;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.*;

public final class DemoData {
    /** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public record Fixture(Categorie categorie,Commande commande,Employe employe,Projet projet,List<Femme> femmes,List<Homme> hommes) {}
    public static Fixture seed(EntityManagerFactory emf) {
        Categorie categorie=new CategorieService(emf).create(new Categorie("INFO","Informatique"));
        Categorie divers=new CategorieService(emf).create(new Categorie("DIV","Divers"));
        ProduitService produits=new ProduitService(emf);
        Produit es=produits.create(new Produit("ES12",120,categorie));
        Produit zr=produits.create(new Produit("ZR85",100,categorie));
        Produit ee=produits.create(new Produit("EE85",200,categorie));
        Produit autre=produits.create(new Produit("AA01",50,divers));
        Commande commande=new Commande(LocalDate.of(2013,3,14));commande.ajouterProduit(es,7);commande.ajouterProduit(zr,14);commande.ajouterProduit(ee,5);new CommandeService(emf).create(commande);
        Commande horsIntervalle=new Commande(LocalDate.of(2013,4,1));horsIntervalle.ajouterProduit(es,1);horsIntervalle.ajouterProduit(autre,2);new CommandeService(emf).create(horsIntervalle);

        Employe chef=new EmployeService(emf).create(new Employe("SAFI","Said","0612345678"));
        Employe autreEmploye=new EmployeService(emf).create(new Employe("ALAMI","Sara","0698765432"));
        Projet projet=new ProjetService(emf).create(new Projet("Gestion de stock",LocalDate.of(2013,1,14),LocalDate.of(2013,5,1),chef));
        TacheService taches=new TacheService(emf);EmployeTacheService affectations=new EmployeTacheService(emf);
        String[] noms={"Analyse","Conception","Developpement","Recette"};double[] prix={1200,1000,2500,800};
        LocalDate[] debuts={LocalDate.of(2013,2,10),LocalDate.of(2013,3,10),LocalDate.of(2013,4,10),LocalDate.of(2013,4,26)};
        LocalDate[] fins={LocalDate.of(2013,2,20),LocalDate.of(2013,3,15),LocalDate.of(2013,4,25),LocalDate.of(2013,4,30)};
        for(int i=0;i<4;i++) {
            Tache tache=taches.create(new Tache(noms[i],debuts[i].minusDays(2),fins[i],prix[i],projet));
            affectations.create(new EmployeTache(i<3?chef:autreEmploye,tache,i<3?debuts[i]:null,i<3?fins[i]:null));
        }

        FemmeService femmeService=new FemmeService(emf);HommeService hommeService=new HommeService(emf);MariageService mariages=new MariageService(emf);
        List<Femme> femmes=new ArrayList<>();List<Homme> hommes=new ArrayList<>();
        String[][] nomsFemmes={{"ALAMI","Karima"},{"RAMI","Salima"},{"ALI","Amal"},{"ALAOUI","Wafa"},{"HAMDI","Hana"},{"BENALI","Nora"},{"IDRISSI","Asma"},{"TAZI","Hiba"},{"FASSI","Meryem"},{"AMRANI","Lina"}};
        for(int i=0;i<10;i++)femmes.add(femmeService.create(new Femme(nomsFemmes[i][0],nomsFemmes[i][1],LocalDate.of(1960+i,1,1))));
        for(int i=0;i<5;i++)hommes.add(hommeService.create(new Homme(i==0?"SAFI":"Homme"+(i+1),i==0?"Said":"Prenom"+(i+1),LocalDate.of(1955+i,2,1))));
        mariages.create(new Mariage(hommes.get(0),femmes.get(0),LocalDate.of(1989,9,3),LocalDate.of(1990,9,3),0));
        mariages.create(new Mariage(hommes.get(0),femmes.get(1),LocalDate.of(1990,9,3),null,4));
        mariages.create(new Mariage(hommes.get(0),femmes.get(2),LocalDate.of(1995,9,3),null,2));
        mariages.create(new Mariage(hommes.get(0),femmes.get(3),LocalDate.of(2000,11,4),null,3));
        mariages.create(new Mariage(hommes.get(0),femmes.get(4),LocalDate.of(2005,1,1),null,1));
        mariages.create(new Mariage(hommes.get(1),femmes.get(0),LocalDate.of(1992,1,1),LocalDate.of(1999,12,31),2));
        return new Fixture(categorie,commande,chef,projet,List.copyOf(femmes),List.copyOf(hommes));
    }
    private DemoData() {}
}
