package server;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import block.*;
import rmi.Floor;
import rmi.Room;

public class ServerThread extends Thread{
		private ObjectOutputStream oos=null;
		private ObjectInputStream ois=null;
	    private Object lock;
	    private Socket clientsocket;    
	    private PrintWriter print;
		private Blockchain blockchain;
		private Scanner scanner;
		private String choiceOfClient;
		
		//private Socket supersocket;
		
		private Floor floor1;
		private Floor floor2;
		private Floor floor3;
		
		private  String showAllRooms(){
			System.out.println("1: "+floor1.printTheFloorInfo()+"\n2: "+floor2.printTheFloorInfo()+"\n3"+floor3.printTheFloorInfo());
			return "1: "+floor1.printTheFloorInfo()+"\n2: "+floor2.printTheFloorInfo()+"\n3"+floor3.printTheFloorInfo();
		}
		
		private  String occupyRoom(int number_needed_of_room, String name_of_occupant){
			for (Room room: floor1.getRoomsPerFloor()){
				if (room.getRoomNumber()==number_needed_of_room){
					room.addToCurrentTenants(name_of_occupant);
					room.setOccupied(true);
				}
			}
			
			return showAllRooms();
		}
		
	  private  String leaveRoom(int number_needed_of_room, String name_of_left){
	  	for (Room room: floor1.getRoomsPerFloor()){
				if (room.getRoomNumber()==number_needed_of_room){
					room.removeFromCurrentTenants(name_of_left);
					room.setOccupied(false);
				}
			}
			
			return showAllRooms();
		}
	  
	    public ServerThread(Floor floor1, Floor floor2, Floor floor3, Object lock, Socket clientsocket, Blockchain blockchain/*, Socket supersocket*/){
	        this.lock = lock;
	        this.clientsocket = clientsocket;
	        this.blockchain=blockchain;
	        //this.supersocket=supersocket;	
	        this.floor1=floor1;
	        this.floor2=floor2;
	        this.floor3=floor3;
	    }

	    public void run(){
	    	System.out.println("Let's see the info per floor at server's thread");
	    	if (floor1==null){
	    		System.out.println("Warning! The first floor is empty");
			}
			System.out.println(floor1.printTheFloorInfo());
			if (floor2==null){
				System.out.println("Warning! The second floor is empty");
			}
			System.out.println(floor2.printTheFloorInfo());
			if (floor3==null){
				System.out.println("Warning! The third floor is empty");
			}
			System.out.println(floor3.printTheFloorInfo());

	    	synchronized(lock){
	    		init();
	    		//getChoiceBlockFromClient();	
	    		getChoiceStringFromClient();
	    		sendBlockChainToClient();
	    	}
	    }
	    private void init(){
				try{
					System.out.println("The socket was initialized: "+clientsocket.getLocalSocketAddress()+" "+clientsocket.getLocalPort());
					oos = new ObjectOutputStream(clientsocket.getOutputStream());
					oos.reset();
					oos.flush();
					System.out.println("The ObjectOutputStream was initialized");

					//ois= new ObjectInputStream(clientsocket.getInputStream());
					//System.out.println("The ObjectInputStream was initialized");
					scanner=new Scanner(clientsocket.getInputStream());
				}
				catch (EOFException e){
					System.out.println("The ois cannot be initialized properly");
				}
				catch (IOException e) {
					System.out.println("Cannot get the list of all blocks to add for blockchain of this client");
					e.printStackTrace();
				}
				catch(Exception e){
					System.out.println("Cannot connect to server to send the block "+e.getMessage());
				}
				finally{
					//I should not close oos or ois, because it closes underlying socket
					/*try {
						oos.close();
						ois.close();
					} catch (IOException e) {
						System.out.println("Cannot close the connection to server "+e.getMessage());
					}*/
				}

		}

	    /*private void getChoiceBlockFromClient(){
	    	System.out.println("We entered the void for getting choice from client");
	    	try{
	    	//ois=new ObjectInputStream(clientsocket.getInputStream());
			if (ois.readObject()!=null){
				System.out.println("Error! Somehow server is reading from ois. You need to fix this");
			}
			if (ois.readObject()==null){
				System.out.println("The server is waiting while client is writing smth");
				wait();
				//sleep(10000);
			}

	    	Block blockFromClient=(Block) ois.readObject();
	    	System.out.println("We have read the block from client");
	    	choiceOfClient=blockFromClient.getData();
	    	System.out.println("Thank you! The server received the following request from the client: "+choiceOfClient);
	    	int numberOfNeededRoom=0;
	    	String nameOfClient="";
	    	if (!choiceOfClient.equals("1")){
	    	String[] parts=choiceOfClient.split(";");
	    	numberOfNeededRoom=Integer.parseInt(parts[1]);
	    	nameOfClient=parts[2];
	    	}
	    	String responseToClient=null;
	    	
	    	try{
	    	switch(choiceOfClient){
	    	case "1": responseToClient=showAllRooms();
	    	case "2": responseToClient=occupyRoom(numberOfNeededRoom, nameOfClient);
	    	case "3": responseToClient=leaveRoom(numberOfNeededRoom, nameOfClient);	    		    	
	    	default: responseToClient="The input from client was not proper and didn't match the given range";
	    	}
	    	}
	    	catch (NullPointerException e){
	    		System.out.println("Error in filling rooms at server's side");
	    	}
	    	System.out.println("The server will send this to client: "+responseToClient);
	    	Block block=new Block(blockFromClient.getIndex()+1, blockFromClient.getPreviousHash(), new Date(), responseToClient, blockFromClient.getHash());
	    	blockchain.addToBlockchain(block);
	    	System.out.println("the block was added to blockchain, now the blockchain's size is: "+blockchain.size());	    	
	    	
	    	} catch (EOFException e) {
	    		//This can be connected to reading Null
				System.out.println("We received EOF Exception, because we ran out of input");
				e.printStackTrace();
			} catch (Exception e){
				System.out.println("We received another type of exception");
				e.printStackTrace();
			}
	    	finally{
	    		try {
					ois.close();
					clientsocket.close();
				} catch (IOException e) {
					System.out.println("Cannot close Object Input Stream properly");
					e.printStackTrace();
				}
	    	}
	    	
	    } */
	    
	    private void getChoiceStringFromClient(){
	    	System.out.println("We entered the void for getting choice from client");
	    	try{
	    	choiceOfClient=scanner.nextLine();
	    	System.out.println("Thank you! The server received the following request from the client: "+choiceOfClient);
	    	int numberOfNeededRoom=0;
	    	String nameOfClient="";
	    	if (!choiceOfClient.equals("1")){
	    	String[] parts=choiceOfClient.split(";");
	    	numberOfNeededRoom=Integer.parseInt(parts[1]);
	    	nameOfClient=parts[2];
	    	}
	    	String responseToClient=null;
	    	
	    	try{
	    	switch(choiceOfClient){
	    	case "1": responseToClient=showAllRooms();
	    	case "2": responseToClient=occupyRoom(numberOfNeededRoom, nameOfClient);
	    	case "3": responseToClient=leaveRoom(numberOfNeededRoom, nameOfClient);	    		    	
	    	default: responseToClient="The input from client was not proper and didn't match the given range";
	    	}
	    	}
	    	catch (NullPointerException e){
	    		System.out.println("Error in filling rooms at server's side");
	    	}
	    	System.out.println("The server will send this to client: "+responseToClient);
	    	Block block=blockchain.generateNextBlock(responseToClient);
	    	blockchain.addToBlockchain(block);
	    	System.out.println("the block was added to blockchain, now the blockchain's size is: "+blockchain.size());	    	
	    	
	    	//} catch (EOFException e) {
	    		//This can be connected to reading Null
				//System.out.println("We received EOF Exception, because we ran out of input");
				//e.printStackTrace();
			} catch (Exception e){
				System.out.println("We received another type of exception");
				e.printStackTrace();
			}
	    	finally{
	    		try {
					ois.close();
					clientsocket.close();
				} catch (IOException e) {
					System.out.println("Cannot close Object Input Stream properly");
					e.printStackTrace();
				}
	    	}
	    	
	    }
	    private void sendBlockChainToClient(){	    	
	    	try {
	    		//oos=new ObjectOutputStream(clientsocket.getOutputStream());
				oos.writeObject(blockchain);
				System.out.println("The server wrote the client back/sent the blockchain");
				oos.reset();
				oos.flush();
				System.out.println("ObjectOutputObject was reset and flushed");
			} catch (IOException e) {
				System.out.println("Error with sending the blockchain from server to client");
				e.printStackTrace();
			}	    	
	    	finally{
	    		/*try {
	    			oos.close();
					clientsocket.close();
				} catch (IOException e) {
					System.out.println("Cannot close the socket for client at server side");
					e.printStackTrace();
				}*/
	    	}
	    }
	
}
