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
	    private Socket clientsocket;    
	    private PrintWriter print;
		private Blockchain blockchain;
		private Scanner scanner;
		private String choiceOfClient;
		
		private Socket supersocket;
		
	    public ServerThread(Object lock, Socket clientsocket, Blockchain blockchain, Socket supersocket){
	        this.lock = lock;
	        this.clientsocket = clientsocket;
	        this.blockchain=blockchain;
	        this.supersocket=supersocket;	        
	    }

	    public void run(){
	    	synchronized(lock){
	    		getChoiceBlockFromClient();
	    		sendChoiceToSuperServer();
	    		getInfoFromSuperServer();
	    		sendBlockChainToClient();
	    	}
	    }
	    
	    private void getChoiceBlockFromClient(){
	    	try{
	    	
	    	ois=new ObjectInputStream(clientsocket.getInputStream());
	    	Block blockFromClient=(Block) ois.readObject();
	    	choiceOfClient=blockFromClient.getData();
	    	System.out.println("Thank you! The server received the following request from the client: "+choiceOfClient);
	    	} catch (Exception e){
	    		System.out.println(e.getMessage());;
	    	} finally{
	    		try {
					ois.close();
					clientsocket.close();
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
				try {
					supersocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	
	    }

	    private void getInfoFromSuperServer(){
	    	try {
				scanner=new Scanner(supersocket.getInputStream());
				String responseText=scanner.nextLine();
				System.out.println("Server sent the following through server"+responseText);
			} catch (IOException e) {				
				e.printStackTrace();
			} finally{
				print.close();
				try {
					supersocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    }
	    
	    private void sendBlockChainToClient(){	    	
	    	try {
	    		oos=new ObjectOutputStream(clientsocket.getOutputStream());	    	
				oos.writeObject(blockchain);
				System.out.println("The server wrote the client back/sent the blockchain");
			} catch (IOException e) {
				System.out.println("Error with sending the blockchain from server to client");
				e.printStackTrace();
			}	    	
	    	finally{
	    		try {
					clientsocket.close();
				} catch (IOException e) {
					System.out.println("Cannot close the socket for client at server side");
					e.printStackTrace();
				}
	    	}
	    }  
	    
	    
	
}
