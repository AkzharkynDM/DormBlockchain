package superserver;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SuperServer {

	private Floor floor1;
	private Floor floor2;
	private Floor floor3;
	private Scanner scanner;
	private ServerSocket ss; 
	private PrintWriter print;
	
	public final static int PORT_NUMBER_SUPERSERVER=12346;
	
	public SuperServer(){
		
	}
	private void fillTheRoomsAndFloors(){
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
		floor1=new Floor(rooms2, 2, "Assistant Second");
		floor1=new Floor(rooms3, 3, "Assistant Third");
	}
	private String showAllRooms(){
		return "1: "+floor1.printTheFloorInfo()+"\n2: "+floor2.printTheFloorInfo()+"\n3"+floor3.printTheFloorInfo();
	}
	
	private String occupyRoom(int number_needed_of_room, String name_of_occupant){
		for (Room room: floor1.getRoomsPerFloor()){
			if (room.getRoomNumber()==number_needed_of_room){
				room.addToCurrentTenants(name_of_occupant);
				room.setOccupied(true);
			}
		}
		
		return showAllRooms();
	}
	
    private String leaveRoom(int number_needed_of_room, String name_of_left){
    	for (Room room: floor1.getRoomsPerFloor()){
			if (room.getRoomNumber()==number_needed_of_room){
				room.removeFromCurrentTenants(name_of_left);
				room.setOccupied(false);
			}
		}
		
		return showAllRooms();
	}
    
    private void getAnswerFromServer(){
    	String smthFromClient=null;
    	String nameOfClient;
    	int numberOfNeededRoom;
    	int choice;

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
	    	smthFromClient = scanner.nextLine();
	    	
	    	} catch (Exception e){
	    		System.out.println(e.getMessage());;
	    	} finally{
	    		scanner.close();
	    	}
    	
    	String[] parts=smthFromClient.split(";");
    	choice=Integer.parseInt(parts[0]);
    	numberOfNeededRoom=Integer.parseInt(parts[1]);
    	nameOfClient=parts[2];
    	
    	String responseToClient="ERROR. Connection is refused";
    	switch(choice){
    	case 1: responseToClient=showAllRooms();
    	case 2: responseToClient=occupyRoom(numberOfNeededRoom, nameOfClient);
    	case 3: responseToClient=leaveRoom(numberOfNeededRoom, nameOfClient);
    	default: responseToClient=new String("Please, choose a number from the given range");    	
    	}
    	
    	print.println(responseToClient);
    	print.flush();
    }
    
    public /*static*/ void main (String[] args){
    	fillTheRoomsAndFloors();
    	getAnswerFromServer();
    }

}
