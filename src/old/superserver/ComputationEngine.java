package superserver;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ComputationEngine implements Computation{

	public ComputationEngine(){
		super();		
	}
	
	public int calculatePrice(Room room){
		int price=50000;
		if (room.getCondition().contains("bad") || room.getCondition().contains("repairment") || room.getCondition().contains("replaced"))
			price=-10000;
		if (room.getCondition().contains("perfect") || room.getCondition().contains("excellent"))
			price=+10000;
		if (room.getCondition().contains("good"))
			price=+5000;
		if (room.getCondition().contains("decent"))
			price=-5000;
		return price;
	}        
}
