import java.io.*;
import java.util.*;
import java.net.*;

public class Server{
  public final static int PORT_NUMBER=12345;
  private ServerSocket ss; 
  private Blockchain blockchain;
  
  public void connectToClients(){
	  blockchain=new Blockchain();
	  
      Object lock = new Object();
      try{
        ss = new ServerSocket(PORT_NUMBER);
        while (true){
            Socket s = ss.accept();
            new Thread(new ServerThread(lock, s, blockchain)).start();
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
  
}

class ServerThread extends Thread{
	private ObjectInputStream ois;
    private Object lock;
    private Socket s;
    private Room room=null;
    private PrintWriter print;
	private Blockchain blockchain;

    public ServerThread(Object lock, Socket s, Blockchain blockchain){
        this.lock = lock;
        this.s = s;
        this.blockchain=blockchain;
    }

    public void run(){
      try{       

        ois = new ObjectInputStream(s.getInputStream());        
        print = new PrintWriter(s.getOutputStream());        
        
          synchronized(lock){
        	room = (Room) ois.readObject(); 
        	print.println(blockchain.addToBlockChain(block));
            print.flush();
          }        

      }catch(Exception e){
        System.out.println(e);
      }
      finally{
    	  try {
			ois.close();
			s.close();
			print.close();
		} catch (IOException e) {
			 System.out.println(e);
		}
    	  
      }
    }
}