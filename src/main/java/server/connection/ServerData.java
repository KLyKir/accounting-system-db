package server.connection;

import server.database.connection.dao.DaoException;
import server.database.connection.dao.jdbc.SoftwareJdbc;
import server.database.connection.dao.jdbc.StaffJdbc;
import server.database.connection.domain.Software;
import server.database.connection.domain.Staff;
import server.database.connection.domain.enums.SoftwareType;
import server.database.connection.redis.RedisConnection;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class ServerData extends Thread{
    private final Socket socket;

    private final BufferedReader readFromSocket;
    private final BufferedWriter writeInSocket;

    public ServerData(Socket socket) throws IOException {
        this.socket = socket;

        readFromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writeInSocket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String message;
        String username;
        try {
            while(true){
                username = readFromSocket.readLine();
                System.out.println(username);
                if (RedisConnection.getInstance().get(username).isPresent()) {
                    sendMessageForUser("User " + username + " is existing.");
                    sendMessageForProgram("UserOK");
                    break;
                } else {
                    sendMessageForUser("User " + username + " is not existing. Enter new");
                }
            }

            while(true){
                String password = readFromSocket.readLine();
                System.out.println(password);
                if (RedisConnection.getInstance().get(username).get().getPassword().equals(password)) {
                    sendMessageForUser("Access accepted. Access level is " + RedisConnection.getInstance().get(username).get().getAccessLevel());
                    sendMessageForProgram("AcLvl:" + RedisConnection.getInstance().get(username).get().getAccessLevel().name());
                    sendMessageForProgram("PasswordOK");
                    break;
                } else {
                    sendMessageForUser("Access denied. Wrong password");
                }
            }

            try {
                while (true) {
                    message = readFromSocket.readLine();
                    System.out.println(message);
                    String[] command = message.split(":");
                    chooseCommand(command);
                }
            } catch (NullPointerException ignored) {} catch (DaoException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            this.downService();
        }
    }

    private void chooseCommand(String[] command) throws DaoException {
        switch (command[0]){
            case "CREATE_STAFF":
                createStaff(command);
                break;
            case "SHOW_STAFF":
                showStaff();
                break;
            case "CREATE_SOFTWARE":
                createSoftware(command);
                break;
            case "SHOW_SOFTWARE":
                showSoftware();
                break;
            case "ADD_STAFF_TO_SOFTWARE":
                addStaffToSoftware(command);
                break;
            case "SHOW_STAFF_RELATED_TO_SOFTWARE":
                showStaffRelatedToSoftware(command);
                break;
        }
    }

    private void createStaff(String[] command) throws DaoException {
        new StaffJdbc().create(new Staff(command[1],command[2],Long.parseLong(command[3]),command[4]));
        sendMessageForUser("Staff was created");
    }

    private void showStaff() throws DaoException {
        List<Staff> staffList = new StaffJdbc().findAll().get();
        String message = staffList.stream()
                .map(Staff::toString)
                .collect(Collectors.joining("\n"));
        sendMessageForUser(message);
    }

    private void createSoftware(String[] command) throws DaoException {
        new SoftwareJdbc().create(new Software(command[1], SoftwareType.valueOf(command[2])));
        sendMessageForUser("Software was created");
    }

    private void showSoftware() throws DaoException {
        List<Software> staffList = new SoftwareJdbc().findAll().get();
        String message = staffList.stream()
                .map(Software::toString)
                .collect(Collectors.joining("\n"));
        sendMessageForUser(message);
    }

    private void addStaffToSoftware(String[] command) throws DaoException {
        new SoftwareJdbc().addStaffToProject(
                new Staff(Long.parseLong(command[1]),"","",1L,""),
                new Software(Long.parseLong(command[2]),"",SoftwareType.API)
                );
        sendMessageForUser("Staff was added to software");
    }

    private void showStaffRelatedToSoftware(String[] command) throws DaoException {
        List<Staff> staffList = new StaffJdbc().showStaffRelatedToSoftware(Long.parseLong(command[1])).get();
        String message = staffList.stream()
                .map(Staff::toString)
                .collect(Collectors.joining("\n"));
        sendMessageForUser(message);
    }

    private void sendMessageForUser(String msg) {
        try {
            System.out.println(msg);
            writeInSocket.write("PB:" + msg + "\n");
            writeInSocket.flush();
        } catch (IOException ignored) {}

    }
    private void sendMessageForProgram(String msg) {
        try {
            writeInSocket.write("PV:" + msg + "\n");
            writeInSocket.flush();
        } catch (IOException ignored) {}

    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                readFromSocket.close();
                writeInSocket.close();
            }
        } catch (IOException ignored) {}
    }

}
