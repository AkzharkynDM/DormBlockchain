import java.util.List;
import java.util.ArrayList;

public class Room {	
	private int room_number;
	//private Object picture;
	private List<String> current_tenants=new ArrayList<String>();
	private String condition;
	private int price;	
	
	public Room(int room_number, String current_tenant, String condition, int price) {
		this.room_number=room_number;
		this.current_tenants.add(current_tenant);		
		this.condition=condition;
		this.price=price;
	}
}
