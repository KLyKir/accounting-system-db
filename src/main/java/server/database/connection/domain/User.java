package server.database.connection.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import server.database.connection.domain.enums.AccessLevel;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {
    private String username;
    private String password;
    private AccessLevel accessLevel;
}
