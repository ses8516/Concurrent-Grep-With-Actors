import static akka.actor.Actors.* ;

import java.util.List;
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
	final String fileName;
	final ActorRef collectionActor;
	
	public Configure(String fileName, ActorRef collectionActor){
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
}

/*
 * Class of Actors
 */

/**
 * Scans one file for occurrences of the pattern
 * Contructs a Found message and sends it to the CollectionActor
 */
class ScanActor extends UntypedActor{
	
	private final String pattern;
	
	public ScanActor(String pattern){
		this.pattern = pattern;
	}
	
	public void onReceive(Object message){
		if(message instanceof Configure){

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
	private FileCount fileCount;
	
	public void onReceive(Object message){
		if(message instanceof FileCount){
			fileCount = (FileCount) message;
		}
		else if(message instanceof Found){

		}
	}
}

/**
 * Main driver
 * Create a CollectionActor, start it, and send the FileCount message to it.
 * Create one ScanActor per file, start it, and send the appropriate Configure
 * message to each such actor.
 */
public class CGrep {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0){
			System.err.println("No Pattern Inputted. Exiting...");
			System.exit(0);
		}
		String pattern = args[0];
		if(args.length == 1){
			// Standard Input will be used

		}else{
			// Files will be used. Files are the rest of the elemetns in args
			for(int i = 1; i < args.length; i++){
				String filename = args[i];

			}
		}
	}

}
