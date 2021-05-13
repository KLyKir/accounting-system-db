package server.database.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    void create(T t) throws RepositoryException;

    void update(ID id, T t) throws RepositoryException;

    void delete(ID id) throws RepositoryException;

    Optional<List<T>> findAll() throws RepositoryException;
}
