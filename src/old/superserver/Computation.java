package superserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Computation extends Remote {
public int calculatePrice(Room room) throws RemoteException;

}
