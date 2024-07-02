// Authors: John James and Jacob Carlstrom
import java.io.*;
import java.net.*;
import java.util.*;

public class ExtraCredit_James_Carlstrom {

    private static final int PORT = 5000; // arbitrary port number
    private static List<ClientHandler> clients = new ArrayList<>(); // list of connected clients

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Group Chat Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting Group Chat Server: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {

        private Socket clientSocket;
        private List<ClientHandler> clients;
        private String username;
        private DataInputStream input;
        private DataOutputStream output;

        public ClientHandler(Socket clientSocket, List<ClientHandler> clients) {
            this.clientSocket = clientSocket;
            this.clients = clients;
        }

        @Override
        public void run() {
            try {
                input = new DataInputStream(clientSocket.getInputStream());
                output = new DataOutputStream(clientSocket.getOutputStream());
                output.writeUTF("Enter your name:");
                username = input.readUTF();
                broadcast(username + " has joined the group");
                String message;
                do {
                    message = input.readUTF();
                    if (message.startsWith("@")) { // personal message
                        int index = message.indexOf(" ");
                        String recipient = message.substring(1, index);
                        String pm = message.substring(index + 1);
                        sendPM(username, recipient, pm);
                    } else { // group message
                        broadcast(username + ": " + message);
                    }
                } while (!message.equals("quit"));
                clients.remove(this);
                broadcast(username + " has left the group");
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients) {
                if (!client.equals(this)) { // exclude the sender
                    try {
                        DataOutputStream clientOutput = new DataOutputStream(client.clientSocket.getOutputStream());
                        clientOutput.writeUTF(message);
                    } catch (IOException e) {
                        System.err.println("Error broadcasting message: " + e.getMessage());
                    }
                }
            }
        }

        private void sendPM(String sender, String recipient, String message) {
            for (ClientHandler client : clients) {
                if (client.username.equals(recipient)) {
                    try {
                        DataOutputStream clientOutput = new DataOutputStream(client.clientSocket.getOutputStream());
                        clientOutput.writeUTF("[PM from " + sender + "]: " + message);
                        output.writeUTF("[PM to " + recipient + "]: " + message);
                    } catch (IOException e) {
                        System.err.println("Error sending PM: " + e.getMessage());
                    }
                    break;
                }
            }
        }
    }
}
