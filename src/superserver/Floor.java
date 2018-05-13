package superserver;
import java.util.List;
import java.util.ArrayList;

public class Floor {

	private List<Room> roomsPerFloor;
	private int numberOfFloor;
	private String floorAssistant;
	
	public Floor(int numberOfFloor, String floorAssistant){
		roomsPerFloor=new ArrayList<Room>();
		this.numberOfFloor=numberOfFloor;
		this.floorAssistant=floorAssistant;
	}
	
	public List<Room> getRoomsPerFloor(){
		return roomsPerFloor;
	}
	
	public String printTheFloorInfo(){
		String floorInfo = "";
		floorInfo+="Floor No."+numberOfFloor+": Rooms "+getRoomsPerFloor().toString()+" . Facilities will be given further upon arrival.";
		return floorInfo;
	}
}
