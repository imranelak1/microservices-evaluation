package ma.projet.beans;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
@Entity @Table(name="femmes")
@NamedQuery(name="Femme.marieesAuMoinsDeuxFois",query="select f from Femme f where (select count(m) from Mariage m where m.femme=f)>=2 order by f.id")
@NamedNativeQuery(name="Femme.nombreEnfants",query="select coalesce(sum(m.nbr_enfant),0) from mariages m where m.femme_id=:femmeId and m.date_debut<=:fin and (m.date_fin is null or m.date_fin>=:debut)")
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class Femme extends Personne {
    @OneToMany(mappedBy="femme") private List<Mariage> mariages=new ArrayList<>();
    public Femme(){}
    public Femme(String nom,String prenom,LocalDate naissance){setNom(nom);setPrenom(prenom);setDateNaissance(naissance);setTelephone("0600000000");setAdresse("Casablanca");}
    public List<Mariage> getMariages(){return mariages;}
}
