import java.net.*;
import java.io.*;
import java.util.*;

public class James_client3 {
    public static void main(String[] args) {

        try {
            // create a socket to make connection to server socket
            Socket sock = new Socket("127.0.0.1", 5000);

            // create an output stream to send data to the server
            DataOutputStream data_for_server = new DataOutputStream(sock.getOutputStream());

            // create an input stream to receive data from server
            DataInputStream data_from_server = new DataInputStream(sock.getInputStream());

            // Scanner to capture client name
            System.out.println("Enter your name: ");
            Scanner scnr = new Scanner(System.in);
            String name = scnr.nextLine(); // changed to read entire line instead of just next

            // send the data to the server
            data_for_server.writeUTF(name);
            System.out.println("Data sent to server at " + new Date() + '\n');

            // enter group chat
            String newUser = data_from_server.readUTF();
            System.out.println(newUser + '\n');

            // create a separate thread to listen for incoming messages from the server
            new Thread(() -> {
                while (true) {
                    try {
                        String newMsg = data_from_server.readUTF();
                        // only print incoming messages from the server
                        if (!newMsg.startsWith(name + ": ")) {
                        System.out.println(newMsg + '\n');
                        }
                    } catch (IOException e) {
                        System.err.println("Error while receiving message from server: " + e.getMessage());
                        break;
                    }
                }
            }).start();

            // constantly checks for new message to send to server
            String msg = "";
            while (true) {
                msg = scnr.nextLine();
                if (msg.equals("@exit")) {
                    String end = " has left the chat \n";
                    data_for_server.writeUTF(end);
                    data_for_server.flush();
                    break;
                }
                data_for_server.writeUTF(msg); // added name to message
                data_for_server.flush();
            }

            // closing the scanner and socket
            scnr.close();
            sock.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
