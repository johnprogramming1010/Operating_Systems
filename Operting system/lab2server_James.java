import java.net.*;
import java.io.*;
import java.util.*;

public class lab2server_James{
    public static void main(String[] args){
   
        int port = Integer.parseInt(args[0]); 
        // the above will capture the port number supplied as command-line argument at the beginning 
        // of running the program. However, need to convert it to an integer as the argument is a string
   
        // when running it at the command prompt, use the command: java lab2server_classwork 7077
        // Here, lab2server_classwork is this program. Change this according to your program name
        // Also, port 7077 is an arbitrary choice. You can choose any number beyond 1024
   
        try{
            //create a server socket
            ServerSocket servSock = new ServerSocket(port); //Note, no hard-coded 'port' number
            System.out.println("\n..Server started at "+ new Date() + '\n');
            
            //Listen for a connection request
            Socket sock = servSock.accept();
            
            //create data input and data output streams
            DataInputStream input4mclient = new DataInputStream(sock.getInputStream());
            DataOutputStream out2client = new DataOutputStream(sock.getOutputStream());
            
            //receive weight and height values from client
            double weight = input4mclient.readDouble();
            double height = input4mclient.readDouble();
         
            // calculate BMI using the formula: BMI = (weight * 703) / (height^2)
            double BMI = (weight * 703) / (height * height);

            //send BMI back to client
            out2client.writeDouble(BMI);
            
            //closing the socket at server
            sock.close();
         
        } catch(IOException ioe){
            System.err.println(ioe);
        }

    }//End-of-main
}//End-of-class
