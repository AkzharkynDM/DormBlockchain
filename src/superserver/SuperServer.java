package superserver;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SuperServer {

	private Floor floor;
	private int choice;
	private Scanner scanner;
	private ServerSocket ss; 
	private PrintWriter print;
	
	public final static int PORT_NUMBER_SUPERSERVER=12346;
	
	public SuperServer(){
		
	}
	
	public String showAllRooms(){
		return floor.printTheFloorInfo();
	}
	
	public String occupyRoom(){
		
	}
	
    public String leaveRoom(){
		
	}
    
    public void getAnswerFromServer(){
    	Socket s=null;
    	try{
    		s = ss.accept();
    	}catch(Exception e){
    		System.out.println(e);
    	} finally{
    	  try {
			ss.close();
		} catch (IOException e) {
			System.out.println(e);
		}
      }
      
    	try{
	    	scanner = new Scanner(s.getInputStream());
	    	choice = scanner.nextInt();
	    	
	    	} catch (Exception e){
	    		System.out.println(e.getMessage());;
	    	} finally{
	    		scanner.close();
	    	}
    	
    	String responseToClient="ERROR. Connection is refused";
    	switch(choice){
    	case 1: responseToClient=showAllRooms();
    	case 2: responseToClient=occupyRoom();
    	case 3: responseToClient=leaveRoom();
    	default: responseToClient=new String("Please, choose a number from the given range");    	
    	}
    	
    	print.println(responseToClient);
    	print.flush();
    }
    
    public /*static*/ void main (String[] args){
    	
    }

}
