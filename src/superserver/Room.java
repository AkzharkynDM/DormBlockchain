package superserver;
import java.util.List;
import java.util.ArrayList;

public class Room {		
	//private Object picture;
	private List<String> current_tenants=new ArrayList<String>();
	private String condition;
	private int price;	
	private boolean occupied;
	private int roomNumber;
	
	public Room(int room_number, String current_tenant, String condition, int price) {		
		this.current_tenants.add(current_tenant);		
		this.condition=condition;
		this.price=price;
	}
	
	@Override
	public String toString(){
		return "The room number is "+roomNumber+
				". Status: "+occupied+
				" Current tenants: "+current_tenants.toString()+
				", condition: "+condition+
				", price: "+price;
	}	
}
