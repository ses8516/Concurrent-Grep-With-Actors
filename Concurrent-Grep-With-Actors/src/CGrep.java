import static akka.actor.Actors.* ;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import akka.actor.* ;


/*
 * Class of messages
 */

/**
 * Each ScanActor expects to receive exactly one Configure message
 * Configure message contains the name of the file and a reference
 * to the CollectionActor
 */
class Configure{
	final String pattern;
	final String fileName;
	final ActorRef collectionActor;
	
	public Configure(String pattern, String fileName, ActorRef collectionActor){
		this.pattern = pattern;
		this.fileName = fileName;
		this.collectionActor = collectionActor;
	}
}

/**
 * Class of message that contains a count of the number of files being scanned
 * To be sent to the CollectionActor exactly once
 */
class FileCount{
	final int count;
	
	public FileCount(int count){
		this.count = count;
	}
}

/**
 * Contructed by the ScanActor and sent to the CollectionActor
 * Contains String of the name of the file and a list of strings, one entry
 * 		in the list for each matching line. Each string in the list consits
 * 		of the line number, a space, and the text of the line itself. The
 * 		list in in order of line number
 */
class Found{
	final String fileName;
	final List<String> lines;
	
	public Found(String fileName, List<String> lines){
		this.fileName = fileName;
		this.lines = lines;
	}
	
	@Override
	public String toString() {
		String rtn = "";
		if(fileName == null) {
			rtn += "-" + "\n";
		} else {
			rtn += fileName + "\n";
		}
		
		int i = 1;
		for(String line : lines) {
			i++;
			rtn += "  " + line;
			
			if(i < line.length()) {
				rtn += "\n";
			}
		}
		
		return rtn;
	}
}

/*
 * Class of Actors
 */

/**
 * Scans one file for occurrences of the pattern
 * Contructs a Found message and sends it to the CollectionActor
 */
class ScanActor extends UntypedActor{
	
	public void onReceive(Object message){
		if(message instanceof Configure){
			Configure v = (Configure) message ;
            ActorRef collectionActor = v.collectionActor;
            Pattern pattern = Pattern.compile(v.pattern);
            Scanner scanner = null;
            List<String> linesMatch = new ArrayList<String>();
            
            if(v.fileName == null) {
            	scanner = new Scanner(System.in);
            } else {
            	try {
					scanner = new Scanner(new File(v.fileName));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
            }
            
            while(scanner.hasNext()) {
            	Matcher matcher = pattern.matcher(scanner.next());
            	if(matcher.find()){
            		
            	}
            	
            }
            
            Found found = new Found(v.fileName, Collections.unmodifiableList(linesMatch));
            
            collectionActor.tell(found);
        } else {
            throw new IllegalArgumentException("Unknown message") ;
        }
	}
}

/**
 * Receives a FileCount message to know the number of files being scanned
 * Receives Found messages from all the ScanActors and upon receipt are
 * printed out in the format "0 Text".
 * 
 * When all Found messages have been processed, all actors are shut down
 */
class CollectionActor extends UntypedActor{
	private int fileCount;
	private int numFoundReceived = 0;
	
	public void onReceive(Object message) throws Exception{
		if(message instanceof FileCount){
			fileCount = ((FileCount) message).count;
		}
		else if(message instanceof Found){
			// TODO message stuff
			
			numFoundReceived++;
			if(numFoundReceived == fileCount){
				shutDown();
			}
		}
		else {
			throw new IllegalArgumentException("Unknown message") ;
		}
	}
	
	private void shutDown(){
		Actors.registry().shutdownAll();
	}
}

/**
 * Main driver
 * Create a CollectionActor, start it, and send the FileCount message to it.
 * Create one ScanActor per file, start it, and send the appropriate Configure
 * message to each such actor.
 */
public class CGrep {
	private static ActorRef collectionActor;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0){
			System.err.println("No Pattern Inputted. Exiting...");
			System.exit(0);
		}
		String pattern = args[0];
		collectionActor = actorOf(CollectionActor.class);
		collectionActor.start();
		collectionActor.tell(new FileCount(args.length - 1));
		if(args.length == 1){
			// Standard Input will be used

		}else{
			// Files will be used. Files are the rest of the elemetns in args
			for(int i = 1; i < args.length; i++){
				String fileName = args[i];
				ActorRef scanActor = actorOf(ScanActor.class);
				scanActor.start();
				
				scanActor.tell(new Configure(pattern, fileName, collectionActor));
			}
		}
	}

}
