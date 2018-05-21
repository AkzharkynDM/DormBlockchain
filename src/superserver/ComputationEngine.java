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
	
	//Here it plays a role of Server
	public static void main(String[] args) {
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
}
