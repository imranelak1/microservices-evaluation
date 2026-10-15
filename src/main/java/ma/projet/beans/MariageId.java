package ma.projet.beans;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
@Embeddable
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class MariageId implements Serializable {
    @Column(name="homme_id") private Integer hommeId;
    @Column(name="femme_id") private Integer femmeId;
    @Column(name="date_debut") private LocalDate dateDebut;
    public MariageId(){}
    public MariageId(Integer hommeId,Integer femmeId,LocalDate dateDebut){this.hommeId=hommeId;this.femmeId=femmeId;this.dateDebut=dateDebut;}
    public Integer getHommeId(){return hommeId;}
    public Integer getFemmeId(){return femmeId;}
    public LocalDate getDateDebut(){return dateDebut;}
    @Override public boolean equals(Object o){return o instanceof MariageId k && Objects.equals(hommeId,k.hommeId)&&Objects.equals(femmeId,k.femmeId)&&Objects.equals(dateDebut,k.dateDebut);}
    @Override public int hashCode(){return Objects.hash(hommeId,femmeId,dateDebut);}
}
