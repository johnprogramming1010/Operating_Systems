// Author: John James and Jacob Carlstrom
import java.io.*;
import java.net.*;

public class GroupChatClient3_James {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message;
            do {
                System.out.print(input.readUTF() + " ");
                message = reader.readLine();
                output.writeUTF(message);
            } while (!message.equals("quit"));
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}