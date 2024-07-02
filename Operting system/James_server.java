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
import java.util.concurrent.*;

public class James_server{
    
    //create a fixed size thread pool
    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2));
    
    public static void main(String[] args){
   
        try{
            //create a server socket
            ServerSocket servsock = new ServerSocket(5000);
            System.out.println("Server started at "+ new Date() + '\n');

            while(true) {
                //Listen for a connection request
                Socket sock = servsock.accept();
                System.out.println("Connected to client at "+ new Date() + '\n');

                //create a new ClientHandler for the new client and execute it in the thread pool
                ClientHandler clientHandler = new ClientHandler(sock);
                threadPoolExecutor.execute(clientHandler);
            }
            
        } catch(IOException ioe){
            System.err.println(ioe);
        } finally {
            //shutdown the thread pool
            threadPoolExecutor.shutdown();
        }
    }//End-of-main
    
    //ClientHandler inner class to handle client connections
    private static class ClientHandler implements Runnable {
        private final Socket client;
        private String name;
        
        public ClientHandler(Socket client) {
            this.client = client;
        }
        
        @Override
        public void run() {
            try{
                //create data input and data output streams
                DataInputStream client_input = new DataInputStream(client.getInputStream());
                DataOutputStream client_output = new DataOutputStream(client.getOutputStream());

                //receive data from client
                name = client_input.readUTF();
                
                //send result back to client
                String newClient = name + " has entered the chat \n";
                client_output.writeUTF(newClient);

                //send new message to clients
                while(true) {
                    String msg = client_input.readUTF();
                    if(msg.equals("@end")) {
                        String leaveMessage = name + " has left the chat \n";
                        client_output.writeUTF(leaveMessage);
                        break;
                    }
                    String newMsg = name + ": " + msg;
                    
                    //iterate over the active threads in the thread pool and send the message to each one
                    for (Runnable thread : threadPoolExecutor.getQueue()) {
                        if (thread instanceof ClientHandler && thread != this) {
                            ClientHandler handler = (ClientHandler) thread;
                            DataOutputStream output = new DataOutputStream(handler.client.getOutputStream());
                            output.writeUTF(newMsg);
                        }
                    }
                }
            } catch(IOException ioe){
                System.err.println(ioe);
            } finally {
                try {
                    //notify other clients that the user has left
                    String leaveMessage = name + " has left the chat \n";
                    for (Runnable thread : threadPoolExecutor.getQueue()) {
                        if (thread instanceof ClientHandler && thread != this) {
                            ClientHandler handler = (ClientHandler) thread;
                            DataOutputStream output = new DataOutputStream(handler.client.getOutputStream());
                            output.writeUTF(leaveMessage);
                        }
                    }
                    client.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }
}//End-of-class
