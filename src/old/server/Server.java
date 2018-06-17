package server;
import java.io.*;
import java.util.*;
import java.net.*;

import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import block.*;

public class Server{
  public final static int PORT_NUMBER_CLIENT=14348;
  public final static int PORT_NUMBER_SUPERSERVER=14347;
  public final static String LOCAL_HOST="localhost";
  private static ServerSocket ss; 
  private static Blockchain blockchain;
  
  private static Socket supersocket;
  
  private static void connectToClients(){
	  blockchain=new Blockchain();
	  
      Object lock = new Object();
      try{
        ss = new ServerSocket(PORT_NUMBER_CLIENT);
        while (true){
            Socket clientsocket = ss.accept();
            new Thread(new ServerThread(lock, clientsocket, blockchain, supersocket)).start();
        }

      }catch(Exception e){
    	  System.out.println("Server cannot connect to clients");
        System.out.println(e);
      } finally{
    	  try {
			ss.close();
		} catch (IOException e) {
			System.out.println("The server cannot close the connection to client "+e.getMessage());			
		}
      }
  }
  
  private static void connectToSuperServerToTestConnection(){
	  try {
		supersocket=new Socket(LOCAL_HOST, PORT_NUMBER_SUPERSERVER);
	} catch (UnknownHostException e) {	
		System.out.println("Server cannot connect to superserver");
		e.printStackTrace();
	} catch (IOException e) {
		System.out.println("Server cannot connect to superserver");
		e.printStackTrace();
	} finally{
		try {
			supersocket.close();
		} catch (IOException e) {	
			System.out.println("The server cannot close the connection to superserver");
			e.printStackTrace();
		}
	}
  } 
  /*public void calculatePrice() throws RemoteException{
  	  Registry reg=LocateRegistry.getRegistry(LOCAL_HOST, PORT_NUMBER_SUPERSERVER);
  	  Computation calculator=null;
	try {
		calculator = (Computation) reg.lookup("calculatePrice");
	} catch (NotBoundException e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
  	  System.out.println("Now based on the request of the client, let's create an object of type Room and pass it to superserver");
  	  Room desiredRoom=new Room(0,"",0);
  	  System.out.println(calculator.calculatePrice(desiredRoom));
    }*/
  
  public static void main (String[] args){
	  //TODO: The order seems to be important here -> need to review code from "Computer Networks" class
	  connectToSuperServerToTestConnection();
	  connectToClients();  
	  connectToSuperServerResponse();
	  
  }
}

