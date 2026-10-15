package ma.projet.classes;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public class EmployeTacheId implements Serializable {
  @Column(name = "employe_id")
  private Integer employeId;

  @Column(name = "tache_id")
  private Integer tacheId;

  public EmployeTacheId() {}

  public EmployeTacheId(Integer employeId, Integer tacheId) {
    this.employeId = employeId;
    this.tacheId = tacheId;
  }

  public Integer getEmployeId() {
    return employeId;
  }

  public Integer getTacheId() {
    return tacheId;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof EmployeTacheId k
        && Objects.equals(employeId, k.employeId)
        && Objects.equals(tacheId, k.tacheId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeId, tacheId);
  }
}
