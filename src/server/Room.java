package server;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class Room implements Serializable{		
	//private Object picture;
	private List<String> current_tenants;
	private String condition;
	private int price;	
	private boolean occupied;
	private int roomNumber;
	
	public Room(int room_number, String condition, int price) {		
		current_tenants=new ArrayList<String>();
		this.condition=condition;
		this.price=price;
		occupied=false;
	}
	
	@Override
	public String toString(){
		return "The room number is "+roomNumber+
				". Status: "+occupied+
				" Current tenants: "+current_tenants.toString()+
				", condition: "+condition+
				", price: "+price;
	}	
	
	public int getRoomNumber(){
		return roomNumber;
	}
	
	public void setOccupied(boolean occupied){
		this.occupied=occupied;
	}
	
	public void addToCurrentTenants(String current_tenant){
		this.current_tenants.add(current_tenant);	
	}
	
	public void removeFromCurrentTenants(String name_of_left){
		this.current_tenants.remove(name_of_left);
	}
	
	public String getCondition(){
		return condition;
	}
}
