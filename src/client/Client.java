package client;
import java.io.*;
import java.util.*;
import java.net.*;

import block.*;

import static java.lang.Thread.sleep;

public class Client {
	public final static int PORT_NUMBER_CLIENT=17348;
	public final static String LOCAL_HOST="localhost";
	private static Socket s;
	//private static ObjectOutputStream oos=null;
	private static ObjectInputStream ois=null;
	private static PrintWriter print;
	//private static Scanner socketscanner;

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
		if (blockchain==null){
			System.out.println("The blockchain is empty, so genesis block was not generated");
			//blockchain.createGenesisBlock(); //so, as I understood the first block is genesis block, over which we will place our blocks
		}
		return blockchain.generateNextBlock(dataForBlock);
	}
		
	/*private static void connectToServerToSendBlock(String dataForBlock){
		try {
			//oos = new ObjectOutputStream(s.getOutputStream());
			//oos.reset();
			//oos.flush();
			//System.out.println("The ObjectOutputStream was initialized");
			if (requestBlock(dataForBlock)==null){
				System.out.println("Attention! I am going to write and send the null object");
			}
			 oos.writeObject(requestBlock(dataForBlock));
	         addToOwnBlockchain(requestBlock(dataForBlock));
	         oos.reset();
			 oos.flush();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			//This part can be removed
			try {
			oos.close();
			} catch (IOException e) {
				System.out.println("Cannot close the ObjectInputStream and ObjectOutputStream properly");
				e.printStackTrace();
			}
		}
	}*/
	
	private static void addToOwnBlockchain(Block block){
		blockchain.addToBlockchain(block);
	}

	private static void staticwait(){
		try {
			Client.class.wait();
			//sleep(10000);
		} catch (InterruptedException e){
			System.out.println("Caught exception during static waiting");
		} finally{

		}
	}
	private static void connectToServerToGetBlockchain(){
		try {
			//ois=new ObjectInputStream(s.getInputStream());
			//System.out.println("The ObjectInputStream was initialized");
			if (ois.readObject()==null){
				System.out.println("The client is waiting while server is writing smth");
				staticwait();
			}
			System.out.println("Now we need to get all blocks from server and add to blockchain");
			Blockchain	allBlocksToStore = (Blockchain) ois.readObject();
			if (allBlocksToStore ==null){
				System.out.println("The client received null from server instead of blockchain");
			}
			for (Block b: allBlocksToStore.getBlocks()){
				addToOwnBlockchain(b);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			/*try {
			ois.close();
			} catch (IOException e) {
				System.out.println("Cannot close the ObjectInputStream and ObjectOutputStream properly");
				e.printStackTrace();
			}*/
		}
	}

	private static void init(){
		try{ 
			s = new Socket(LOCAL_HOST, PORT_NUMBER_CLIENT);
			System.out.println("The socket was initialized");
			//oos = new ObjectOutputStream(s.getOutputStream());
			//oos.reset();
			//oos.flush();
			//System.out.println("The ObjectOutputStream was initialized");
			ois=new ObjectInputStream(s.getInputStream());
			System.out.println("The ObjectInputStream was initialized");
			//socketscanner=new Scanner(s.getInputStream());
			print=new PrintWriter(s.getOutputStream());
			System.out.println("The printwriter was initialized");
		}
	 catch (IOException e) {
		System.out.println("Cannot get the list of all blocks to add for blockchain of this client");
		e.printStackTrace();
	}
	catch(Exception e){
		System.out.println("Cannot connect to server to send the block "+e.getMessage());
	}
	finally{
		try {
			s.close();
			//oos.close();
			//ois.close();
		} catch (IOException e) {
			System.out.println("Cannot close the connection to server "+e.getMessage());
		}
	}
	}

	private static void connectToServerToSendChoiceString(String toSend){
		print.println(toSend);
		print.flush();
	}

	private static void Menu(){
    	System.out.println("Good day, dear User! Welcome to our dorm.\n Please select from the given options:\n"
    			+"1. Show all rooms\n"
    			+"2. Occupy a room\n"
    			+"3. Leave a room\n");
    	Scanner sca=new Scanner(System.in);
    	init();
    	do{
    	choiceToSend=sca.nextInt();
    	switch (choiceToSend){
    	case 1: {
    		//connectToServerToSendBlock(String.valueOf(choiceToSend));
			connectToServerToSendChoiceString(String.valueOf(choiceToSend));
			connectToServerToGetBlockchain();
			break;
		}
    	case 2: {
    		//connectToServerToSendBlock(choiceToSend+";"+requestRoom());
			connectToServerToSendChoiceString(choiceToSend+";"+requestRoom());
			System.out.println("The client is trying to send this to server: "+choiceToSend+";"+requestRoom());
			connectToServerToGetBlockchain();
			break;
		}
    	case 3:	{
    		//connectToServerToSendBlock(choiceToSend+";"+requestRoom());
			connectToServerToSendChoiceString(choiceToSend+";"+requestRoom());
    		System.out.println("The client is trying to send this to server: "+choiceToSend+";"+requestRoom());
			connectToServerToGetBlockchain();
			break;
		}
    	default: System.out.println("Please, enter the number fom the given range");
    	}
    	} while (sca.hasNext());
    	if (!sca.hasNext()) sca.close();
    	
    }
	
	public static void main (String[] args){
		Menu();
    }
	
	
}
