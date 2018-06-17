package server;
import java.io.*;
import java.util.*;
import java.net.*;

import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import block.*;
import rmi.Floor;
import rmi.Room;

public class Server{
  public final static int PORT_NUMBER_CLIENT=17348;
  //public final static int PORT_NUMBER_SUPERSERVER=18347;
  public final static String LOCAL_HOST="localhost";
  private static ServerSocket ss=null;
  private static Blockchain blockchain;  
	
  //private static Socket supersocket;
  
  private static void connectToClients(){
	  blockchain=new Blockchain();
	  if (blockchain==null){
	  	System.out.println("Attention! genesis block was not created for total blockchain at server's side");
	  }
      Object lock = new Object();
      try{
        ss = new ServerSocket(PORT_NUMBER_CLIENT);
        while (true){
            Socket clientsocket = ss.accept();
            new Thread(new ServerThread(floor1, floor2, floor3, lock, clientsocket, blockchain/*, supersocket*/)).start();
        }
        
      }catch(Exception e){
    	  System.out.println("Server cannot connect to clients");
          //System.out.println(e);
      } finally{
    	  try {
			ss.close();
		} catch (IOException e) {
			System.out.println("The server cannot close the connection to client "+e.getMessage());			
		}
      }
      System.out.println("Currently server is handling the following number of requests: "+blockchain.size());
  }
  
  	private static Floor floor1;
	private static Floor floor2;
	private static Floor floor3;
	
	private static void fillTheRoomsAndFloors(){
		List<Room> rooms1=new ArrayList<>();
		List<Room> rooms2=new ArrayList<>();
		List<Room> rooms3=new ArrayList<>();
		
		rooms1.add(new Room(11, "furniture will be replaced", 30000));
		rooms1.add(new Room(12, "good", 35000));
		rooms1.add(new Room(13, "bad", 20000));
		
		rooms2.add(new Room(21, "excellent", 30000));
		rooms2.add(new Room(22, "good", 35000));
		rooms2.add(new Room(23, "decent", 20000));
		
		rooms3.add(new Room(31, "perfect", 40000));
		rooms3.add(new Room(32, "good", 35000));
		rooms3.add(new Room(33, "needs repairment job", 20000));
		
		floor1=new Floor(rooms1, 1, "Assistant First");
		floor2=new Floor(rooms2, 2, "Assistant Second");
		floor3=new Floor(rooms3, 3, "Assistant Third");

		//System.out.println("We filled the floors and rooms");
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
	  fillTheRoomsAndFloors();
	  connectToClients();  
	  
  }
}

