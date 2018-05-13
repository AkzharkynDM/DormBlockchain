package superserver;
import java.util.Scanner;

public class SuperServer {

	private Floor floor;
	
	public SuperServer(){
		
	}
	
	public void showAllRooms(){
		
	}
	
	public void occupyRoom(){
		
	}
	
    public void leaveRoom(){
		
	}
    
    public void getAnswerFromServer(){
    	
    	switch(choice){
    	case 1: showAllRooms();
    	case 2: occupyRoom();
    	case 3: leaveRoom();
    	default: System.out.println("Please, choose a number from the given range");
    	}
    }
    
    public static void main (String[] args){
    	
    }

}
