package rmi;
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
	
	private static Scanner scanner;
	private static ServerSocket ss; 
	private static PrintWriter print;
	private static Socket s;
	public final static int PORT_NUMBER_SUPERSERVER=14347;	
	
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
    	//deployRMI();
    }

}
