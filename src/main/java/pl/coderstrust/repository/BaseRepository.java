package pl.coderstrust.repository;

import java.io.Serializable;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

  <S extends T> S save(S var1) throws RepositoryOperationException;

  Optional<T> findById(ID var1) throws RepositoryOperationException;

  boolean existsById(ID var1) throws RepositoryOperationException;

  Iterable<T> findAll() throws RepositoryOperationException;

  long count() throws RepositoryOperationException;

  void deleteById(ID var1) throws RepositoryOperationException;

  void deleteAll() throws RepositoryOperationException;
}
