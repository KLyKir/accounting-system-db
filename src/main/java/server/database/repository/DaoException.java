package server.database.repository;

import java.util.concurrent.ExecutionException;

public class DaoException extends ExecutionException {
    public DaoException(String message, Throwable cause){
        super(message, cause);
    }

    public DaoException(String message){
        super(message);
    }
}
