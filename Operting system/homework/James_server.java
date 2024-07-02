// /* John James */
// 
// import java.net.*;
// import java.io.*;
// import java.util.*;
// 
// // If interested, check: https://docs.oracle.com/javase/tutorial/essential/io/datastreams.html
// 
// public class James_server{
// 	public static void main(String[] args){
//    
//    try{
// 			//create a server socket
// 			ServerSocket servsock = new ServerSocket(5000);
// 			System.out.println("Server started at "+ new Date() + '\n');
// 
// 			//Listen for a connection request
// 			Socket sock = servsock.accept();
// 			
// 			//create data input and data output streams
// 			DataInputStream client_input = new DataInputStream(sock.getInputStream());
// 			DataOutputStream output_for_client = new DataOutputStream(sock.getOutputStream());
//          
//          //receive data from client
// 			String name = client_input.readUTF();
// 
// 			//send result back to client
//          String newClient = name + " has entered the chat \n";
//          output_for_client.writeUTF(newClient);
//          
//          //send new message to clients
//          while(true) {
//             String msg = client_input.readUTF();
//             if(msg.equals("@end")) {
//                 output_for_client.writeUTF(msg);
//                 break;
//             }
//             output_for_client.writeUTF(msg);
//          }
//          
// 			//closing the socket at server
//          sock.close();
// 			
// 		 } catch(IOException ioe){
// 				System.err.println(ioe);
// 			}
// 
// 	}//End-of-main
// }//End-of-class
// 

import java.net.*;
import java.io.*;
import java.util.*;

public class James_server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Group chat started on port 5000...");

            // create a map to store the connected clients and their output streams
            Map<String, DataOutputStream> clients = new HashMap<>();

            while (true) {
                // accept incoming client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted new client connection: " + clientSocket);

                // create an input stream to receive data from the client
                DataInputStream dataFromClient = new DataInputStream(clientSocket.getInputStream());

                // read the client's name
                String clientName = dataFromClient.readUTF();
                System.out.println("Client name received: " + clientName);

                // create an output stream to send data to the client
                DataOutputStream dataToClient = new DataOutputStream(clientSocket.getOutputStream());
                clients.put(clientName, dataToClient); // add the client to the map

                // broadcast a message to all connected clients to notify them of the new client
                String newUserMsg = clientName + " has entered the chat\n";
                System.out.print(newUserMsg);
                broadcast(clients, newUserMsg);

                // create a new thread to listen for incoming messages from this client
                new Thread(() -> {
                    while (true) {
                        try {
                            String msgFromClient = dataFromClient.readUTF();
                            System.out.println(clientName + ": " + msgFromClient);
                            String msgToSend = clientName + ": " + msgFromClient + '\n';
                            broadcast(clients, msgToSend); // broadcast the message to all clients
                        } catch (IOException e) {
                            System.err.println("Error while receiving message from client: " + e.getMessage());
                            break;
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            System.err.println("Error while running the server: " + e.getMessage());
        }
    }

    // method to broadcast a message to all connected clients
    private static void broadcast(Map<String, DataOutputStream> clients, String msg) {
        for (DataOutputStream clientStream : clients.values()) {
            try {
                clientStream.writeUTF(msg);
                clientStream.flush();
            } catch (IOException e) {
                System.err.println("Error while broadcasting message to client: " + e.getMessage());
            }
        }
    }
}
