package server;
import java.io.*;
import java.util.*;
import java.net.*;

public class Server{
  public final static int PORT_NUMBER_CLIENT=12345;
  public final static int PORT_NUMBER_SUPERSERVER=12346;
  public final static String LOCAL_HOST="localhost";
  private ServerSocket ss; 
  private Blockchain blockchain;
  
  private Socket supersocket;
  
  private void connectToClients(){
	  blockchain=new Blockchain();
	  
      Object lock = new Object();
      try{
        ss = new ServerSocket(PORT_NUMBER_CLIENT);
        while (true){
            Socket s = ss.accept();
            new Thread(new ServerThread(lock, s, blockchain, supersocket)).start();
        }

      }catch(Exception e){
        System.out.println(e);
      } finally{
    	  try {
			ss.close();
		} catch (IOException e) {
			System.out.println(e);
		}
      }
  }
  
  private void connectToSuperServer(){
	  try {
		supersocket=new Socket(LOCAL_HOST, PORT_NUMBER_SUPERSERVER);
	} catch (UnknownHostException e) {	
		e.printStackTrace();
	} catch (IOException e) {	
		e.printStackTrace();
	} finally{
		try {
			supersocket.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
  }
  
  public /*static*/ void main (String[] args){
	  connectToSuperServer();
	  connectToClients();
  }
}

