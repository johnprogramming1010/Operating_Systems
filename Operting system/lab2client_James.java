import java.net.*;
import java.io.*;
import java.util.*;

public class lab2client_James{
    public static void main(String[] args){
      
      int port = Integer.parseInt(args[2]); 
      double weight = Double.parseDouble(args[0]); 
      double height = Double.parseDouble(args[1]);
		
      try{
         //create a socket to make connection to server socket
         Socket sock = new Socket("127.0.0.1", port);
         
         //create an output stream to send data to the server (Hint: DataOutPutStream)
         DataOutputStream data2server = new DataOutputStream(sock.getOutputStream());
         
         //create an input stream to receive data from server
         DataInputStream response = new DataInputStream(sock.getInputStream());        

         //send weight and height to the server to calculate BMI
         data2server.writeDouble(weight);
         data2server.writeDouble(height);
         System.out.println("Data sent to server at "+ new Date() + '\n');
         
         data2server.flush(); // it is a good idea to flush the socket content after sending
         
         //receive the result from the server    
         double BMI = response.readDouble();
         System.out.println("BMI calculated by server is: " + BMI);

         sock.close(); //closing the socket at client
         
      } catch(IOException ioe){
         System.err.println(ioe);
      }
   }//End-of-main
}//End-ofclass
