package pl.coderstrust.database;

import java.io.Serializable;
import java.util.Optional;

public interface Database<T, ID extends Serializable> {

  <S extends T> S save(S var1) throws DatabaseOperationException;

  Optional<T> findById(ID var1) throws DatabaseOperationException;

  boolean existsById(ID var1) throws DatabaseOperationException;

  Iterable<T> findAll() throws DatabaseOperationException;

  long count() throws DatabaseOperationException;

  void deleteById(ID var1) throws DatabaseOperationException;

  void deleteAll() throws DatabaseOperationException;
}
