package ass1;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Sorted_merge {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("work in progress");
		makeSortedBlocks(path+"file1.txt");
		mergeSortedBlocks(NrBlocks,"file1.txt");//NrBlocks
		cleanup(NrBlocks);
		System.out.println("Halfway there!");
		makeSortedBlocks(path+"file2.txt");
		mergeSortedBlocks(NrBlocks,"file2.txt");
		cleanup(NrBlocks);
		System.out.println("Done!!");
	}

	private static void makeSortedBlocks(String path) throws IOException {
		FileInputStream inputStream = null;
		Scanner sc = null;
		
		try {
			
		    inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
		    long i =0;
		    int blockcount =0;
		    
		    List<Entry> entries = new ArrayList<Entry>();
		   
		    while (sc.hasNextLine() ) {
		        String line = sc.nextLine();
		       // System.out.println(line);
		        //System.out.println(line.substring(1));
		       
		        entries.add(new Entry(Long.parseLong(line.substring(1)), line.substring(0, 1),0)); // crash after 28643239(=i+1) lines
		        i++;		      		        
		        //
		        //instead of arbitrary number of items we should go for something like write to file if certain percentage of ram is full
		        if (i == 20000000) {
		        	  
				       // System.out.println(i);
				       // long allocatedMemory = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
					 //   printOutMemoryUsage();
					    Collections.sort(entries);					    
					    printOutMemoryUsage();					   
					    createFile("src\\ass1\\"+String.valueOf(blockcount),entries);
					    System.out.println("Block "+String.valueOf(blockcount)+" created.Blocksize = "+String.valueOf(i)+" lines");
					    entries.clear(); 
					    i=0;					    
					    blockcount++;					    					   
		        }		
		        
		    }
		    if (entries.size()>0) {
			    Collections.sort(entries);
			    createFile("src\\ass1\\"+String.valueOf(blockcount),entries);
			    System.out.println("Block "+String.valueOf(blockcount)+" created.Blocksize = "+String.valueOf(i)+" lines");
			    blockcount++;
			    entries.clear(); 
		    }
		    	    
	        NrBlocks = blockcount;
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
	}
	// in parts taken from https://stackoverflow.com/questions/6548157/how-to-write-an-arraylist-of-strings-into-a-text-file
	private static void createFile(String file, List<Entry> entries)
            throws IOException {
        FileWriter writer = new FileWriter(file + ".txt");
        int size = entries.size();
        for (int i=0;i<size;i++) {
            String str = entries.get(i).toString();
            writer.write(str);
            if(i < size-1)//**//This prevents creating a blank line at the end of the file**
                writer.write(System.getProperty("line.separator"));
        }
        writer.close();
    }
	
	private static void writeToFinalFile(String filename, ArrayList<String> list){
		try(FileWriter fw = new FileWriter(filename, true);
			    BufferedWriter bw = new BufferedWriter(fw);
				)
			{
			int size = list.size();
	        for (int i=0;i<size;i++) {
	        	
	        	bw.write(list.get(i)+System.getProperty("line.separator"));
	        	//if(i < size-1)//**//This prevents creating a blank line at the end of the file**
	            //    bw.write("\n");
	        }	      			
			   			  
			} catch (IOException e) {
			   System.out.println("Something went wrong");
			}
	}

	

	
	private static void mergeSortedBlocks(int NrBlocks, String outpath) throws IOException{
		
		FileInputStream[] inputStreamArr = new FileInputStream[NrBlocks];
		Scanner[] scArr = new Scanner[NrBlocks];
		List<Entry> entries = new ArrayList<Entry>();
		ArrayList<String> writeBuffer = new ArrayList<String>();
		int blockNrOflowest =0;
		int writeCounter =0;
		//1.for each block open a new scanner and write the first element into a list
		try{
		for(int i=0;i<NrBlocks;i++){
			inputStreamArr[i] = new FileInputStream(path+String.valueOf(i)+".txt");
			scArr[i] = new Scanner(inputStreamArr[i], "UTF-8");	
			String line = scArr[i].nextLine();//nextLine()
		  //  System.out.println(line);	   		    
		   // System.out.println(path+String.valueOf(i)+".txt");
		    entries.add(new Entry(Long.parseLong(line.substring(1)), line.substring(0, 1),i));	
		    
		}
		//2.sort list, write lowest number L to file, remove L from list
		//3.if list.hasNext() ==true take next line from block that contained L and write to list
		//4.goto 2. repeat until list is emtpy
	
		while (entries.size() != 0 ) {			
			Collections.sort(entries);
			blockNrOflowest = entries.get(0).blockNr();
	//		System.out.println(blockNrOflowest);		
			writeBuffer.add(entries.get(0).toString()); 
			entries.remove(0);
			writeCounter++;
			
			
			if(writeCounter == 20000000){
				writeToFinalFile(path+"sorted_"+outpath,writeBuffer);	
				writeBuffer.clear();
				writeCounter =0;
				System.out.println("writing to file "+ path+"sorted_"+outpath);
			}	
			
			String line ="";
				if(scArr[blockNrOflowest].hasNextLine()){
				 line = scArr[blockNrOflowest].nextLine();//can return empty depending on line break character. note: CR = \r; LF = \n; CRLF = \r\n 
				 entries.add(new Entry(Long.parseLong(line.substring(1)), line.substring(0, 1),blockNrOflowest));//substring() will fail if line = "". duh...	
				}
				
		}
		writeToFinalFile(path+"sorted_"+outpath,writeBuffer);
		System.out.println("writing to file "+ path+"sorted_"+outpath);
		}
		
		finally{
			for(int i=0;i<scArr.length;i++){
				scArr[i].close();
			}
			for(int i=0;i<inputStreamArr.length;i++){
				inputStreamArr[i].close();
			}
		}
		
	}
	
	
	private static void cleanup(int NrBlocks){
		for(int i=0;i<NrBlocks;i++){
			File file = new File(path+String.valueOf(i)+".txt");
			file.delete();
		}
		
	}
	private static String path = System.getProperty("user.dir")+"\\src\\ass1\\";
	private static int NrBlocks;
	
	//not mine, found here:https://stackoverflow.com/questions/30491253/filewriter-out-of-memory
	//kudos to OP
	private static void printOutMemoryUsage() {
	    Runtime runtime = Runtime.getRuntime();

	    NumberFormat format = NumberFormat.getInstance();

	    StringBuilder sb = new StringBuilder();
	    long maxMemory = runtime.maxMemory();
	    long allocatedMemory = runtime.totalMemory();
	    long freeMemory = runtime.freeMemory();

	    sb.append("free memory: " + format.format(freeMemory / 1024) + " ");
	    sb.append("allocated memory: " + format.format(allocatedMemory / 1024)
	            + " ");
	    sb.append("max memory: " + format.format(maxMemory / 1024) + " ");
	    sb.append("total free memory: "
	            + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024)
	            + "<br/>");
	    System.out.println(sb);
	}
	
	
	
}