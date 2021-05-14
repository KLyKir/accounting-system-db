package start.point;

import server.connection.TcpConnection;
import server.database.domain.User;
import server.database.domain.enums.AccessLevel;
import server.database.redis.RedisConnection;

import java.io.IOException;

public class Main {

    //adding default test users
    static {
        RedisConnection redisConnection = RedisConnection.getInstance("localhost",6379);
        redisConnection.put(new User("SomeUser_WithLowPriority","password", AccessLevel.READ_ONLY));
        redisConnection.put(new User("SomeUser_WithHighPriority","password", AccessLevel.READ_AND_WRITE));
    }

    public static void main(String[] args) throws IOException {
        new TcpConnection();
    }
}
