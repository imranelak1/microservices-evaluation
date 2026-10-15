package ma.projet.dao;

import java.io.Serializable;
import java.util.List;

/** Modèle et mapping utilisés dans les exercices de l’évaluation. */
public interface IDao<T, ID extends Serializable> {
  T create(T entity);

  T update(T entity);

  void delete(T entity);

  T findById(ID id);

  List<T> findAll();
}
