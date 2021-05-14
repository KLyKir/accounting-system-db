package ui;


import connection.ClientSocket;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Scanner;

public class UserConsole extends Thread{

    private Scanner scanner = new Scanner(System.in);

    private ClientSocket clientSocket;

    public UserConsole() throws IOException {
        clientSocket = new ClientSocket("localhost",8888);
        start();
    }

    @SneakyThrows
    @Override
    public void run(){
        while(true){
            System.out.println("Enter user name");
            clientSocket.send(scanner.nextLine());
            Thread.sleep(1500);
            if(clientSocket.getMessage().equals("UserOK")){
                break;
            }
        }

        while(true){
            System.out.println("Enter password");
            clientSocket.send(scanner.nextLine());
            Thread.sleep(1500);
            if(clientSocket.getMessage().equals("PasswordOK")){
                break;
            }
        }

        while(true){
            Thread.sleep(1000);
            if(clientSocket.getAccessLvl().equals("READ_AND_WRITE")){
                optionListWithHighPriority();
                chooseOptionHighPriority();
            } else {
                optionListWithLowPriority();
                chooseOptionLowPriority();
            }
        }
    }

    private void optionListWithHighPriority(){
        System.out.println("" +
                "1 - Create staff\n" +
                "2 - Create invoice\n" +
                "3 - Show staff\n" +
                "4 - Show invoice\n" +
                "5 - Add staff to invoice\n" +
                "6 - Show staff related to invoice\n");
    }

    private void optionListWithLowPriority(){
        System.out.println("" +
                "1 - Show staff\n" +
                "2 - Show invoice\n" +
                "3 - Show staff related to invoice\n");
    }

    private void chooseOptionHighPriority(){
        String message;
        switch (Integer.parseInt(scanner.nextLine())){
            case 1:
                message = "CREATE_STAFF:";
                System.out.println("Enter name");
                message += scanner.nextLine() + ":";
                System.out.println("Enter surname");
                message += scanner.nextLine() + ":";
                System.out.println("Enter salary");
                message += scanner.nextLine() + ":";
                clientSocket.send(message);
                break;
            case 2:
                message = "CREATE_INVOICE:";
                System.out.println("Enter name");
                message += scanner.nextLine() + ":";
                System.out.println("Enter type");
                message += scanner.nextLine();
                clientSocket.send(message);
                break;
            case 3:
                clientSocket.send("SHOW_STAFF:");
                break;
            case 4:
                clientSocket.send("SHOW_INVOICE:");
                break;
            case 5:
                message = "ADD_STAFF_TO_SOFTWARE:";
                System.out.println("Enter staff id ");
                message += scanner.nextLine() + ":";
                System.out.println("Enter invoice id");
                message += scanner.nextLine();
                clientSocket.send(message);
                break;
            case 6:
                message = "SHOW_STAFF_RELATED_TO_INVOICE:";
                System.out.println("Enter invoice id ");
                message += scanner.nextLine();
                clientSocket.send(message);
                break;
        }

    }

    private void chooseOptionLowPriority(){

        switch (Integer.parseInt(scanner.nextLine())){
            case 1:
                clientSocket.send("SHOW_STAFF:");
                break;
            case 2:
                clientSocket.send("SHOW_INVOICE:");
                break;
            case 3:
                String message = "SHOW_STAFF_RELATED_TO_INVOICE:";
                System.out.println("Enter invoice id ");
                message += scanner.nextLine();
                clientSocket.send(message);
                break;
        }
    }
}
