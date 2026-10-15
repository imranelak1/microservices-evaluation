package ma.projet.beans;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
@Entity @Table(name="hommes")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class Homme extends Personne {
    @OneToMany(mappedBy="homme") private List<Mariage> mariages=new ArrayList<>();
    public Homme(){}
    public Homme(String nom,String prenom,LocalDate naissance){setNom(nom);setPrenom(prenom);setDateNaissance(naissance);setTelephone("0600000000");setAdresse("Casablanca");}
    public List<Mariage> getMariages(){return mariages;}
}
