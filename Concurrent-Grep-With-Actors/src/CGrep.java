
public class CGrep {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length == 0){
			System.err.println("No Pattern Inputted. Exiting...");
			System.exit(0);
		}
		String pattern = args[0];
		if(args.length == 1){
			// Standard Input will be used
		}else{
			// Files will be used. Files are the rest of the elemetns in args
		}
	}

}
