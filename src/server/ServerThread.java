package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import block.*;

public class ServerThread extends Thread{	
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
	    private Object lock;
	    private Socket s;    
	    private PrintWriter print;
		private Blockchain blockchain;
		//private Scanner scanner;
		private String choiceOfClient;
		
		private Socket supersocket;
		
	    public ServerThread(Object lock, Socket s, Blockchain blockchain, Socket supersocket){
	        this.lock = lock;
	        this.s = s;
	        this.blockchain=blockchain;
	        this.supersocket=supersocket;	        
	    }

	    public void run(){
	    	synchronized(lock){
	    		getChoiceFromClient();
	    		sendChoiceToSuperServer();
	    		//getBlockFromClient();
	    		sendBlockChainToClient();
	    	}
	    }
	    
	    private void getChoiceFromClient(){
	    	try{
	    	
	    	ois=new ObjectInputStream(s.getInputStream());
	    	Block blockFromClient=(Block) ois.readObject();
	    	choiceOfClient=blockFromClient.getData();
	    	System.out.println("Thank you! The server received the following request from the client: "+choiceOfClient);
	    	} catch (Exception e){
	    		System.out.println(e.getMessage());;
	    	} finally{
	    		try {
					ois.close();
				} catch (IOException e) {
					System.out.println("Cannot close Object Input Stream properly");
					e.printStackTrace();
				}
	    	}
	    	
	    }
	    
	    private void sendChoiceToSuperServer(){
	    	try {
				print=new PrintWriter(supersocket.getOutputStream());
				print.println(choiceOfClient);
			} catch (IOException e) {				
				e.printStackTrace();
			} finally{
				print.close();
			}
	    	
	    }

	    /*private void getBlockFromClient(){
	    	try{       

		        ois = new ObjectInputStream(s.getInputStream());        
		        print = new PrintWriter(s.getOutputStream());
		        Block block = (Block) ois.readObject(); 
		        print.println(blockchain.addToBlockchain(block));
		        print.flush();	                 

		      }catch(Exception e){
		        System.out.println(e);
		      }
		      finally{
		    	  try {
					ois.close();
					s.close();
					print.close();
				} catch (IOException e) {
					 System.out.println(e);
				}
		    	  
		      }
	    }*/
	    
	    private void sendBlockChainToClient(){	    	
	    	try {
	    		oos=new ObjectOutputStream(s.getOutputStream());	    	
				oos.writeObject(blockchain);
			} catch (IOException e) {
				System.out.println("Error with sending the blockchain from server to client");
				e.printStackTrace();
			}
	    	System.out.println("The server wrote the client back/sent the blockchain");
	    }  
	    
	    
	
}
