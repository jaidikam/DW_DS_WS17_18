package ass1;

public class Entry implements Comparable<Entry> {
	

	     
	     private long key;
	     private String value;
	     private int blockNr;

	     public Entry(long key, String value, int blockNr) {
	         this.key = key;
	         this.value = value;
	         this.blockNr = blockNr;//needed to save the blockNr that an entry is coming from
	     }

	     public int compareTo(Entry e) {
	         if(this.key > e.key) return 1;
	         if(this.key < e.key) return -1;
	         return 0;
	     
	     }
	     //needed to get correct output instead of some kind of internal string representation of the object
	     public String toString(){          
	          return value+key;    
	         } 
	     
	     public int blockNr(){          
	          return blockNr;    
	         } 
}
