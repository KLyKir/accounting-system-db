package server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpConnection{

    private ServerSocket serverSocket;

    public TcpConnection() throws IOException {
        serverSocket = new ServerSocket(8888);
        addClient();
    }

    public void addClient(){
        try {
            Socket socket = serverSocket.accept();
            try {
                new ServerData(socket);
            } catch (IOException e) {
                socket.close();
            }
        } catch (IOException exception) {
        }
    }
}
