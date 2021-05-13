package connection;

import lombok.SneakyThrows;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class ClientSocket extends Thread{
    private Socket socket;

    private String message;
    private String accessLvl;

    private static BufferedReader readFromSocket;
    private static BufferedWriter writeInSocket;

    public ClientSocket(String host, int port) throws IOException {
        InetAddress address = InetAddress.getByName(host);
        socket = new Socket(address,port);
        readFromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writeInSocket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @SneakyThrows
    @Override
    public void run(){
        while(true){
            message = readFromSocket.readLine();
            String[] splitMessage = message.split(":");
            System.out.println(splitMessage.length);
            message = splitMessage[1];
            String[] showSplit = message.split("|");
            if(showSplit.length > 1){
                Arrays.stream(showSplit).forEach(System.out::println);
            } else {
                if (splitMessage[0].equals("PB")) {
                    System.out.println(message);
                }
            }
            if(message.equals("AcLvl")){
                accessLvl = splitMessage[2];
            }
        }
    }

    public void send(String msg) {
        try {
            writeInSocket.write(msg + "\n");
            writeInSocket.flush();
        } catch (IOException ignored) {}

    }

    public String getMessage(){
        return message;
    }
    public String getAccessLvl(){ return accessLvl;}
}
