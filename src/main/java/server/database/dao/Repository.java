package server.database.dao;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    void create(T t) throws DaoException;

    void update(ID id, T t) throws DaoException;

    void delete(ID id) throws DaoException;

    Optional<List<T>> findAll() throws DaoException;
}
