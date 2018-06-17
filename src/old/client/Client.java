package client;
import java.io.*;
import java.util.*;
import java.net.*;

import block.*;

public class Client {
	public final static int PORT_NUMBER_CLIENT=14348;
	public final static String LOCAL_HOST="localhost";
	private static Socket s;
	private static ObjectOutputStream oos;	
	private static ObjectInputStream ois;	
	//private static PrintWriter print;
	
	private static Blockchain blockchain=new Blockchain();
	private static int choiceToSend;
	
	private static String requestRoom(){
		Scanner sc=new Scanner(System.in);
		System.out.println("Write down the room number");
		int room_number=sc.nextInt();
		System.out.println("Write down the name of current tenant");
		String current_tenant=sc.nextLine();
		System.out.println("Write down about the conditions");
		String condition=sc.nextLine();
		System.out.println("Write down the price");
		int price=sc.nextInt();
		sc.close();
		return new String(room_number+";"+ current_tenant+";"+ condition+";"+ price);		
	}

	private static Block requestBlock(String dataForBlock){	
		//if (blockchain==null){
		//	blockchain.createGenesisBlock(); //so, as I understood the first block is genesis block, over which we will place our blocks
		//}
		return (Block) blockchain.generateNextBlock(dataForBlock);
	}
		
	private static void connectToServerToSendBlock(String dataForBlock){
		try{
	          s = new Socket(LOCAL_HOST, PORT_NUMBER_CLIENT);	          

	          oos = new ObjectOutputStream(s.getOutputStream());
	          oos.writeObject(requestBlock(dataForBlock));	         
	         
	        }catch(Exception e){
	          System.out.println("Cannot connect to server to send the block "+e.getMessage());
	        }
		finally{
			try {
				s.close();				
				oos.close();
			} catch (IOException e) {
				System.out.println("Cannot close the connection to server "+e.getMessage());
			}
			
	    }
		addToOwnBlockchain(requestBlock(dataForBlock));
		
		System.out.println("Now we need to get all blocks from server and add to blockchain");
		try {
			List<Block> allBlocksToStore;
			ois=new ObjectInputStream(s.getInputStream());
			allBlocksToStore = (List<Block>) ois.readObject();			
			for (Block b: allBlocksToStore)
			addToOwnBlockchain(b);
		} catch (IOException e) {
			System.out.println("Cannot get the list of all blocks to add for blockchain of this client");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				System.out.println("Cannot close Object Input Stream");
				e.printStackTrace();
			}
		}
       
	}
	
	private static void addToOwnBlockchain(Block block){
		blockchain.addToBlockchain(block);
	}
	
	private static void Menu(){
    	System.out.println("Good day, dear User! Welcome to our dorm.\n Please select from the given options:\n"
    			+"1. Show all rooms\n"
    			+"2. Occupy a room\n"
    			+"3. Leave a room\n");
    	Scanner sc=new Scanner(System.in);
    	do{
    	choiceToSend=sc.nextInt();        	
    	switch (choiceToSend){
    	case 1: connectToServerToSendBlock(String.valueOf(choiceToSend));
    	case 2: connectToServerToSendBlock(choiceToSend+";"+requestRoom());
    	case 3:	connectToServerToSendBlock(choiceToSend+";"+requestRoom());
    	default: System.out.println("Please, enter the number fom the given range");
    	}
    	} while (sc.hasNext());
    	if (!sc.hasNext()) sc.close();
    	
    }
	
	public static void main (String[] args){    	
    		Menu();    	
    }
	
	
}
