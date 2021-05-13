package server.database.connection.redis;

import redis.clients.jedis.Jedis;
import server.database.connection.domain.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class RedisConnection {

    private static RedisConnection instance;

    private final Jedis jedis;

    private RedisConnection(String host, int port){
        jedis = new Jedis(host, port);
        try {
            jedis.connect();
        }catch (Exception e) {
            instance = null;
            System.out.println(e.getMessage());
        }
    }

    public static RedisConnection getInstance(String host, int port){
        if(instance == null){
            instance = new RedisConnection(host,port);
        }
        return instance;
    }

    public static RedisConnection getInstance(){
        return instance;
    }

    private boolean checkConnection(){
        return jedis.isConnected();
    }


    public void put(User user) {
        if(!checkConnection()){
            System.out.println("No connection with Redis server");
            System.exit(1);
        }
        byte []keyByteCode = user.getUsername().getBytes(StandardCharsets.UTF_8);
        keyByteCode = cipherData(keyByteCode);
        byte[] valueBytes = serializeUser(user);;

        jedis.set(keyByteCode,valueBytes);
    }

    public Optional<User> get(String username) {
        if(!checkConnection()){
            System.out.println("No connection with Redis server");
            System.exit(1);
        }

        byte [] keyByteCode = username.getBytes(StandardCharsets.UTF_8);
        byte [] byteUser = jedis.get(cipherData(keyByteCode));

        if(byteUser != null){
            return Optional.of( deserializeUser(byteUser));
        }
        return Optional.empty();
    }

    private byte[] cipherData(byte[] bytes){
        for (int i = 0 ; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] + 12);
        }
        return bytes;
    }

    private byte[] serializeUser(Object object){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        byte[] byteCode = {};
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            byteCode = bos.toByteArray();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ignored) { }
        }

        return byteCode;
    }

    private User deserializeUser(byte [] byteCode){
        ByteArrayInputStream bis = new ByteArrayInputStream(byteCode);
        ObjectInput in = null;
        User user = null;
        try {
            in = new ObjectInputStream(bis);
            user = (User) in.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignored) { }
        }

        return user;
    }
}
