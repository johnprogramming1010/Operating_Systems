import java.net.*;
import java.io.*;
import java.util.*;

// If interested, check: https://docs.oracle.com/javase/tutorial/essential/io/datastreams.html

public class lab2server{
	public static void main(String[] args){
        
   try{
			//create a server socket
			ServerSocket servSock = new ServerSocket(0,0); //Note, no hard-coded 'port' number
			System.out.println("\n..Server started at "+ new Date() + '\n');
			
            //Listen for a connection request
			Socket sock = servSock.accept();
			
			
			//create data input and data output streams
			DataInputStream input4mclient = new DataInputStream(sock.getInputStream());
			DataOutputStream out2client = new DataOutputStream(sock.getOutputStream());
			
			
            //receive data from client
            double weight = input4mclient.readDouble();
            double height = input4mclient.readDouble();


			// do calculation
			double BMI = (weight*703)/(height*height);

			//send result back to client
            out2client.writeDouble(BMI);

			//closing the socket at server
            sock.close();
            servSock.close();

		 } catch(IOException ioe){
				System.err.println(ioe);
			}

	}//End-of-main
}//End-of-class