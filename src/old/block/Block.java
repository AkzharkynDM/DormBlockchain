package block;
import java.io.Serializable;
import java.util.Date;

public class Block implements Serializable{
	private int index;
	private Date timestamp;
	private byte[] previousHash;
	private String data; 
	private byte[] hash;
	
	public Block(int index,  byte[] previousHash, Date timestamp, String data, byte[] hash) {
        this.index = index;
        //this.previousHash = previousHash.toString();
        this.previousHash = previousHash;
        this.timestamp = timestamp;
        this.data = data;
        //this.hash = hash.toString();
        this.hash = hash;
    }
	
	public int getIndex(){
		return index;
	}
	
	public byte[] getHash(){
		return hash;
	}
	
	public byte[] getPreviousHash(){
		return previousHash;
	}
	
	public String getData(){
		return data;
	}
	
	public Date getTimestamp(){
		return timestamp;
	}
}
