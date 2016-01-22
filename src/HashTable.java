import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class implements a hashtable that using chaining for collision handling.
 * The chains are implemented using ArrayLists.  When a hashtable is created, 
 * it's initial size, maximum load factor, and (optionally) maximum chain length
 * is specified.  The hashtable can hold arbitrarily many items and resizes 
 * itself whenever it reaches its maximum load factor or whenever it reaches 
 * its maximum chain length (if a maximum chain length has been specified).
 * Note that this hashtable allows duplicate entries.
 */
public class HashTable<T> {
	
	private ArrayList<T>[] hash;//hash table
	private int tableSize;//current size of the table
	private int numItems;//number if items in the table
	private int maxChainLength;//maximum chain length.
	private double maxLoadFactor;//maximum load factor of table (number of items/size)
	
    /**
     * Constructs an empty hashtable with the given initial size, maximum load
     * factor, and no maximum chain length.  The load factor should be a real 
     * number greater than 0.0 (not a percentage).  For example, to create a 
     * hash table with an initial size of 10 and a load factor of 0.85, one 
     * would use:
     * <dir><tt>HashTable ht = new HashTable(10, 0.85);</tt></dir>
     *
     * @param initSize The initial size of the hashtable.  If the size is less
     * than or equal to 0, an IllegalArgumentException is thrown.
     * @param loadFactor The load factor expressed as a real number.  If the
     * load factor is less than or equal to 0.0, an IllegalArgumentException is
     * thrown.
     **/
    public HashTable(int initSize, double loadFactor) {
    	//throw exception if initial size or load factor less than or equal to 0
    	if(initSize <= 0 || loadFactor <= 0){
    		throw new IllegalArgumentException();
    	}
    	tableSize = initSize;//size of the table
    	//create new hash table
    	hash = (ArrayList<T>[])(new ArrayList[initSize]);
    	//initialize each index of table to contain empty array
    	for(int i = 0; i < hash.length; i++){
    		hash[i] = new ArrayList<T>();
    	}
    	maxChainLength = 0;//sentinel value
    	numItems = 0;//initialize items in table to 0
    	maxLoadFactor = loadFactor;
    }
    
    
    /**
     * Constructs an empty hashtable with the given initial size, maximum load
     * factor, and maximum chain length.  The load factor should be a real 
     * number greater than 0.0 (and not a percentage).  For example, to create 
     * a hash table with an initial size of 10, a load factor of 0.85, and a 
     * maximum chain length of 20, one would use:
     * <dir><tt>HashTable ht = new HashTable(10, 0.85, 20);</tt></dir>
     *
     * @param initSize The initial size of the hashtable.  If the size is less
     * than or equal to 0, an IllegalArgumentException is thrown.
     * @param loadFactor The load factor expressed as a real number.  If the
     * load factor is less than or equal to 0.0, an IllegalArgumentException is
     * thrown.
     * @param maxChainLength The maximum chain length.  If the maximum chain
     * length is less than or equal to 0, an IllegalArgumentException is thrown.
     **/
    public HashTable(int initSize, double loadFactor, int maxChainLength) {
    	//throw exception if initial size, load factor, or max chain length
    	//less than or equal to 0
    	if(initSize <= 0 || loadFactor <= 0 || maxChainLength <= 0){
    		throw new IllegalArgumentException();
    	}
    	tableSize = initSize;//size of the table
    	//create new hash table
    	hash = (ArrayList<T>[])(new ArrayList[initSize]);
    	//initialize each index of table to contain empty array
    	for(int i = 0; i < hash.length; i++){
    		hash[i] = new ArrayList<T>();
    	}
    	this.maxChainLength = maxChainLength;//set maximum chain length
    	numItems = 0;//initialize items in table to 0
    	
    }
    
    
    /**
     * Determines if the given item is in the hashtable and returns it if 
     * present.  If more than one copy of the item is in the hashtable, the 
     * first copy encountered is returned.
     *
     * @param item the item to search for in the hashtable
     * @return the item if it is found and null if not found
     **/
    public T lookup(T item) {
    	//compute the hash of the item
    	int itemHash = Math.abs(item.hashCode())%tableSize;
    	//get chain for that hash
    	ArrayList<T> hashIndexList = (ArrayList<T>) hash[itemHash];
    	//search through chain for item
    	for(int i = 0; i < hashIndexList.size(); i++){
    		//return item if found
    		if (hashIndexList.get(i) == item){
    			return hashIndexList.get(i);
    		}
    	}
    	//item not found, return null
    	return null;
    }
    
    
    /**
     * Inserts the given item into the hash table.  
     * 
     * If the load factor of the hashtable after the insert would exceed 
     * (not equal) the maximum load factor (given in the constructor), then the 
     * hashtable is resized.  
     * 
     * If the maximum chain length of the hashtable after insert would exceed
     * (not equal) the maximum chain length (given in the constructor), then the
     * hashtable is resized.
     * 
     * When resizing, to make sure the size of the table is good, the new size 
     * is always 2 x <i>old size</i> + 1.  For example, size 101 would become 
     * 203.  (This  guarantees that it will be an odd size.)
     * 
     * <p>Note that duplicates <b>are</b> allowed.</p>
     *
     * @param item the item to add to the hashtable
     **/
    public void insert(T item) {
    	//compute the hash of the item
    	int itemHash = Math.abs(item.hashCode())%tableSize;
    	//check load factor and chain length and resize if needed
    	if((double)(numItems + 1)/tableSize > maxLoadFactor || (maxChainLength != 0 && hash[itemHash].size() + 1 > maxChainLength)){
    		//resize the table
    		this.resize();
    		//recalculate item hash
    		itemHash = Math.abs(item.hashCode())%tableSize;
    	}
    	//add to end of chain
    	hash[itemHash].add(item);
    	numItems++;
    }
    
    
    /**
     * Removes and returns the given item from the hashtable.  If the item is 
     * not in the hashtable, <tt>null</tt> is returned.  If more than one copy 
     * of the item is in the hashtable, only the first copy encountered is 
     * removed and returned.
     *
     * @param item the item to delete in the hashtable
     * @return the removed item if it was found and null if not found
     **/
    public T delete(T item) {
    	//compute the hash of the item
    	int itemHash = Math.abs(item.hashCode())%tableSize;
    	//get chain for that hash
    	ArrayList<T> hashIndexList = (ArrayList<T>) hash[itemHash];
    	//search through chain for item
    	for(int i = 0; i < hashIndexList.size(); i++){
    		//delete and return item if found
    		if (hashIndexList.get(i) == item){
    			hashIndexList.remove(i);
    			return item;
    		}
    	}
    	//item not found, return null
    	return null; 
    }
    
    
    /**
     * Prints all the items in the hashtable to the PrintStream supplied.
     * The items are printed in the order determined by the index of the 
     * hashtable where they are stored (starting at 0 and going to (hashtable 
     * size - 1)).  The values at each index are printed according to the order 
     * in the ArrayList starting at index 0. 
     *
     * @param out the place to print all the output
     **/
    public void dump(PrintStream out) {
    	//loop through each index of table
    	for(int i = 0; i < hash.length; i++){
    		//print contents if index not empty
    		if(!hash[i].isEmpty())
    		out.println(i + ": " + hash[i]);
    	}
    }
    
  
    /**
     * Prints statistics about the hashtable to the PrintStream supplied.
     * The statistics displayed are: 
     * <ul>
     * <li>the current table size
     * <li>the number of items currently in the table 
     * <li>the current load factor
     * <li>the length of the largest chain
     * <li>the number of chains of length 0
     * <li>the average length of the chains of length > 0
     * </ul>
     *
     * @param out the place to print all the output
     **/
    public void displayStats(PrintStream out) {
    	out.println("Hashtable Statistics:");
    	//current table size
    	out.println("  Current table size: " + tableSize);
    	
    	//number of items in table
    	out.println("  # items in table: " + numItems);
    	
    	//current load factor
    	out.println("  Current load factor: " + (numItems/tableSize));
    	
    	//length of largest chain
    	int longestChain = 0;
    	//search for chain with longest length
    	for(int i = 0; i < hash.length; i++){
    		//list of items at the current index
    		ArrayList<T> hashList = (ArrayList<T>) hash[i];
        	//check if list is larger than number for longest chain
    		if(hashList.size() > longestChain){
    			longestChain = hashList.size();
    		}
    	}
    	out.println("  Longest Chain Length: " + longestChain);
    	
    	//number of chains of length 0
    	int emptyChains = 0;//number of empty chains in table
    	int nonZeroChains = 0;//number of non-empty chains in table
    	//count number of empty and non-empty chains in table
    	for(int i = 0; i < hash.length; i++){
    		if(hash[i].isEmpty()){
    			emptyChains ++;
    		}
    		else
    			nonZeroChains ++;
    	}
    	out.println("  # of empty chains: " + emptyChains);
    	
    	//average length of chains of length > 0
    	int chainSum = 0;//sum of the length of all chains in table
    	//add length of each chain in table to sum
    	for(int i = 0; i < hash.length; i++){
    		//add length of chain at current index to sum
    		chainSum += hash[i].size();
    	}
    	out.println("  Average chain length: " + (double) chainSum/nonZeroChains);
    	
    }
    
    /**
     * Resizes the table to create a new one of size 2N+1 where N is the
     * size of the table that is being resized. Copies the values from
     * hash into this new table and sets hash to the new table
     *
     **/
    private void resize(){
    	//create table that is of size 2(tablesize)+1
    	tableSize = tableSize*2+1;//size of the table
    	//create new hash table
    	ArrayList<T>[] oldHash = hash;//table that needs to be resized
    	hash = (ArrayList<T>[])(new ArrayList[tableSize]);//new hash table
    	//initialize each index of table to contain empty array
    	for(int i = 0; i < hash.length; i++){
    		hash[i] = new ArrayList<T>();
    	}    	
    	//copy values in old hash table to new one
    	for(int i = 0; i < oldHash.length; i++){
    		//list of items at the current index
    		ArrayList<T> hashList = (ArrayList<T>) oldHash[i];
        	//search through chain and add items to new hash
        	for(int j = 0; j < hashList.size(); j++){
        		//get hashcode of item
        		int itemHash = Math.abs((hashList.get(j)).hashCode())%tableSize;
        		//add item to new table
        		hash[itemHash].add(hashList.get(j));
        	}
    	}
    }
}