package start.point;

import server.connection.TcpConnection;
import server.database.connection.domain.User;
import server.database.connection.domain.enums.AccessLevel;
import server.database.connection.redis.RedisConnection;

import java.io.IOException;

public class Main {

    //adding default test users
    static {
        RedisConnection redisConnection = RedisConnection.getInstance("localhost",6379);
        redisConnection.put(new User("User1234","1234", AccessLevel.READ_ONLY));
        redisConnection.put(new User("User12345","12345", AccessLevel.READ_AND_WRITE));
    }

    public static void main(String[] args) throws IOException {
        new TcpConnection();
    }
}
