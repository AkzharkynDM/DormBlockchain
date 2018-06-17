package superserver;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SuperServer {

	private static Floor floor1;
	private static Floor floor2;
	private static Floor floor3;
	private static Scanner scanner;
	private static ServerSocket ss; 
	private static PrintWriter print;
	private static Socket s;
	public final static int PORT_NUMBER_SUPERSERVER=14347;
	
	//public SuperServer(){
		
	//}
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
		floor1=new Floor(rooms2, 2, "Assistant Second");
		floor1=new Floor(rooms3, 3, "Assistant Third");
	}
	private static String showAllRooms(){
		return "1: "+floor1.printTheFloorInfo()+"\n2: "+floor2.printTheFloorInfo()+"\n3"+floor3.printTheFloorInfo();
	}
	
	private static String occupyRoom(int number_needed_of_room, String name_of_occupant){
		for (Room room: floor1.getRoomsPerFloor()){
			if (room.getRoomNumber()==number_needed_of_room){
				room.addToCurrentTenants(name_of_occupant);
				room.setOccupied(true);
			}
		}
		
		return showAllRooms();
	}
	
    private static String leaveRoom(int number_needed_of_room, String name_of_left){
    	for (Room room: floor1.getRoomsPerFloor()){
			if (room.getRoomNumber()==number_needed_of_room){
				room.removeFromCurrentTenants(name_of_left);
				room.setOccupied(false);
			}
		}
		
		return showAllRooms();
	}
    
    private static void getAnswerFromServerAndSendInfo(){
    	String smthFromClient=null;
    	String nameOfClient;
    	int numberOfNeededRoom;
    	int choice;    	    	
    	
    	try{
    		ss=new ServerSocket(PORT_NUMBER_SUPERSERVER);
    		System.out.println("Listening on port " + ss.getLocalPort());
    		s = ss.accept();
    		TimeUnit.SECONDS.sleep(30); // half a minute
    		do {
    		scanner = new Scanner(s.getInputStream());
	    	smthFromClient = scanner.nextLine();
	    	System.out.println("Good day! We received the following response from you: "+smthFromClient); 	
    		}
	    	while (scanner.hasNext());
    	
    	String[] parts=smthFromClient.split(";");
    	choice=Integer.parseInt(parts[0]);
    	numberOfNeededRoom=Integer.parseInt(parts[1]);
    	nameOfClient=parts[2];
    	
    	String responseToClient="ERROR. Connection from server to superserver is refused";
    	switch(choice){
    	case 1: responseToClient=showAllRooms();
    	case 2: responseToClient=occupyRoom(numberOfNeededRoom, nameOfClient);
    	case 3: responseToClient=leaveRoom(numberOfNeededRoom, nameOfClient);
    	default: responseToClient=new String("The input from client was not proper and didn't match the given range");    	
    	}
    	
    	print=new PrintWriter(s.getOutputStream());
    	print.println(responseToClient);
    	print.flush();
    		
    	}catch(Exception e){
    		System.out.println("Cannot create the socket for superserver-server connection properly");
    		System.out.println(e);
    	} finally{
    		scanner.close();
    		print.close();
    	  try {
    		  s.close();
    		  ss.close();
		} catch (IOException e) {
			System.out.println("Cannot close the socket for superserver-server connection properly");
			System.out.println(e);
		}
      }
    }
    
    private static void deployRMI(){
    	if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "calculatePrice";
            Computation engine = new ComputationEngine();
            //This makes remote objects available to clients
            Computation stub =
                (Computation) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }    
    
    public static void main (String[] args){
    	fillTheRoomsAndFloors();
    	getAnswerFromServerAndSendInfo();
    	//deployRMI();
    }

}
