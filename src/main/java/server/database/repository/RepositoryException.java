package server.database.repository;

import java.util.concurrent.ExecutionException;

public class RepositoryException extends ExecutionException {
    public RepositoryException(String message, Throwable cause){
        super(message, cause);
    }

    public RepositoryException(String message){
        super(message);
    }
}
