import java.util.Scanner;
import java.io.*;
import java.util.*;
import java.net.*;

public class Client {
	public final static int PORT_NUMBER=12345;
	public final static String LOCAL_HOST="localhost";
	private Socket s;
	private ObjectOutputStream oos;	
	
	public Room requestRoom(){
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
		return new Room(room_number, current_tenant, condition, price);		
	}
	
	public void connectToServer(){
		try{
	          s = new Socket(LOCAL_HOST, 12345);	          

	          oos = new ObjectOutputStream(s.getOutputStream());
	          oos.writeObject(requestRoom());	         
	         
	        }catch(Exception e){
	          System.out.println(e);
	        }
		finally{
			try {
				s.close();				
				oos.close();
			} catch (IOException e) {
				System.out.println("Cannot close"+e);
			}
			
	    }
	}	
	
	public void Menu(){
    	System.out.println("Good day, dear User! Welcome to our dorm.\n Please select from the given options:\n"
    			+"1. Show all rooms\n"
    			+"2. Occupy a room\n"
    			+"3. Leave a room\n");
    	Scanner sc=new Scanner(System.in);
    	int choice=sc.nextInt();
    	
    	sc.close();
    			
    }
	
}
