package server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blockchain {
	private List<Block> blockchainlist;
	private Block previousBlock;
	
	public Blockchain(){
		 blockchainlist=new ArrayList<Block>();
	}
	
	private Block getGenesisBlock() {
	    return new Block(0, "0".getBytes(), new Date(), "Genesis block", "816534932c2b7154836da6afc367695e6337db8a921823784c14378abed4f7d7".getBytes());
	}
	
	public void createGenesisBlock(){
		blockchainlist.add(getGenesisBlock());
	}
	public String addToBlockchain(Block block){
		if (isValidNewBlock(block, getLatestBlock())){
		block=generateNextBlock(getLatestBlock().getData());
		blockchainlist.add(block);		
		previousBlock=block;
		return "Block "+block.getIndex()+" has been added to the chain"+"\n"+"Hash "+block.getHash()+"\n";
		}
		else return "The block cannot be added into the blockchain";		
	}
	
	private boolean isValidNewBlock(Block newBlock, Block previousBlock){
	    if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
	        System.out.println("invalid index");
	        return false;
	    } else if (previousBlock.getHash() != newBlock.getPreviousHash()) {
	    	System.out.println("invalid previoushash");
	        return false;
	    } else if (calculateHashForBlock(newBlock) != newBlock.getHash()) {
	    	System.out.println("invalid hash: " + calculateHashForBlock(newBlock) + " " + newBlock.getHash());
	        return false;
	    }
	    return true;
	}
	
	private byte[] calculateHash(int index, byte[] previousHash, Date timestamp, String data) {
	    return sha256(Integer.valueOf(index + previousHash.hashCode() + timestamp.hashCode() + data.hashCode()).toString());
	}
	
	private byte[] sha256(String originalString){
		MessageDigest digest=null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}
		byte[] encodedhash = digest.digest(
		   originalString.getBytes(StandardCharsets.UTF_8));
		return encodedhash;
	}
	
	private byte[] calculateHashForBlock(Block block) {
		return calculateHash(block.getIndex(), block.getPreviousHash(), block.getTimestamp(), block.getData());
	}
	
	public Block generateNextBlock (String blockData) {
	    Block previousBlock = getLatestBlock();
	    int nextIndex = previousBlock.getIndex() + 1;
	    Date nextTimestamp = new Date();
	    byte[] nextHash = calculateHash(nextIndex, previousBlock.getHash(), nextTimestamp, blockData);
	    return new Block(nextIndex, previousBlock.getHash(), nextTimestamp, blockData, nextHash);
	}
	
	private Block getLatestBlock(){
		return blockchainlist.get(blockchainlist.size()-1);
	}
	
	/*public int size(){
		return blockchainlist.size();
	}*/
}
