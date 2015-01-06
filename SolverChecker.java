//Valerie Cook, Jennifer Dai, Daniel Radding, Yangzi He

/*	 * A SolverChecker object contains two block collections, one that holds the initial
	 * block configuration and one that contains the goal configuration. It contains an IAmDebugging boolean
	 * , a length, and a width. It reads in command line arguments of the initial file and the goal file
	 * and translates them into these block collections that Solver's main then calls.
	 */

public class SolverChecker {

	protected BlockCollection blocks;	//block collection
	protected BlockCollection goals;
	protected int length; //number of rows
	protected int width;  //number of columns
	protected static int debugLevel;
	protected static boolean debugSolver;
	protected static boolean debugTray;

	public SolverChecker (String [ ] args) {
		/*
		 * Scans in files and or debugging options and converts
		 * information to data structures used in the project.
		 */
		// check for invalid input and act on it.
		actOnInvalidInput(args);
		int i=0; // index of the next argument
		if(args.length==1) {
			// there must be debugging if there is only one argument
			debuggingOptions(args[i]);
			return;
		}
		if(args.length==3) {
			// there must be debugging if there are three arguments
			debuggingOptions(args[i]);
			i++; // increment the index of the next argument
		}
		blocks = new BlockCollection ( );
		// the next argument is the initial tray
		InputSource initialTray=new InputSource(args[i]);
		i++; // increment the index of the next argument
		String firstLine=initialTray.readLine();
		String[] tokens = firstLine.split(" ");
		length=Integer.parseInt(tokens[0]); // the first int is length
		width=Integer.parseInt(tokens[1]); // the second int is width
		String initialBlocks=initialTray.readLine(); // the rest are blocks

		while(initialBlocks!=null) {
			// add blocks to the blocks blockCollection
			blocks.add (splitTokens(initialBlocks)); 
			initialBlocks=initialTray.readLine();
		}
		// the next argument is the the goal tray
		InputSource goalTray = new InputSource(args[i]);
		goals=new BlockCollection();
		String goalBlocks=goalTray.readLine();

		while(goalBlocks!=null) {
			// add blocks to the goals blockCollection
			goals.add(splitTokens(goalBlocks));
			goalBlocks = goalTray.readLine();
		}
	}
	
	private void actOnInvalidInput(String [] args) {
		/*
		 * Catches incorrect user input, prints a useful message, then exits.
		 */
		if(args.length==0) {
			System.out.println("You cannot run this program with zero arguments.");
			System.exit(1);
		}
		if(args.length==3 && !args[0].substring(0,2).equals("-o")) {
			System.out.println("The first argument must be the debugging option " +
					" which must begin with '-o'");
			System.exit(1);
		}
		if(args.length==1 && !args[0].substring(0,2).equals("-o")) {
			System.out.println("If there is only one argurment it must be " +
					" a debugging option which begins with '-o'");
			System.exit(1);
		}
		if(args.length>3) {
			System.out.println("You cannot have more than three arguments.");
			System.out.println("This program takes a max " +
					"of three arguments: optional debugging, initial, and goal configurations. ");
			System.exit(1);
		}
	}
	
	private void debuggingOptions(String s) {
		/*
		 * Changes our boolean flags to true when we call our debugging
		 * options and makes sure our debugging options do what they're supposed to.
		 * @precondition: s has to be a valid debugging option
		 * 
		 */
		if(s.equals("-ooptions")) {
			// print out all options
			printOptions();
			System.exit(1);
		} else if (s.length()>5 || s.length()<4) {
			System.out.println("The debugging option is an invalid length. ");
			System.out.println("It can only have " +
					"a min of one level followed by a class (Solver(s) or Tray(t)) and ");
			System.out.println("a max of one level followed by two classes (Solver(s) and Tray(t))");
			System.exit(1);
		} else if (s.charAt(2)!='1' && s.charAt(2)!='2' 
				&& s.charAt(2)!='3' && s.charAt(2)!='4') {
			System.out.println("The debugging option does not indicate an appropriate level");
			System.exit(1);
		} 
		else if (s.charAt(2)=='1') debugLevel=1;
		else if (s.charAt(2)=='2') debugLevel=2;
		else if (s.charAt(2)=='3') debugLevel=3;
		else if (s.charAt(2)=='4') debugLevel=4;
		if (s.charAt(3)=='t') {
			debugTray=true;
			if (s.length()>4 && s.charAt(4)=='s') {
				debugSolver=true;
			}
		} else if (s.charAt(3)=='s') {
			debugSolver=true;
			if (s.length()>4 && s.charAt(4)=='t') {
				debugTray=true;
			}
		} 
		else {
			System.out.println("The debugging option does not call appropriate classes.");
			System.exit(1);
		}
		if (s.length()>4 && (!debugSolver || !debugTray)) {
			//  if users calls the same class twice
			System.out.println( "The debugging option can only call a class once");
			System.exit(1);
		}
	}
	
	private void printOptions() {
		/*
		 * Prints the options when -ooptions is called
		 * @precondition: user calls -ooptions
		 * @postcondition: exits after it prints
		 */
		System.out.println("You can type the following: ");
		System.out.println("-ooptions, which will print all debugging options");
		System.out.println("-o1t, which will print " +
				"the counts of important events in Tray");
		System.out.println("-o2t, which will print " +
				"the times of important events in Tray");
		System.out.println("-o3t, which will print " +
				"both the counts and the times of important events in Tray");
		System.out.println("-o4t, which will print both the counts and times "
				+ "of important events in Tray and the blocks, spaces and " 
				+ "possible moves for each call to getPossibleMoves");
		System.out.println("-o1s, which will print " +
				"the counts of important events in Solver");
		System.out.println("-o2s, which will print " +
				"the times of important events in Solver");
		System.out.println("-o3s, which will print " +
				"both the counts and the times of important events in Solver");
		System.out.println("-o4s, which will print both the counts and " +
				"the times of important events in Solver "
				+" and the successful and unsucessful moves that Solver makes");
		System.out.println("-o1ts or -o1st, which will print  "+
				"the counts of important events in both Tray and Solver");
		System.out.println("-o2ts or -o2st, which will print  "+
				"the times of important events in both Tray and Solver");
		System.out.println("-o3ts or -o3st, which will print  "+
				"both the counts and the times of important events " +
				"in both Tray and Solver ");
		System.out.println("-o4ts or -o4st, which will print "+
				"both the counts and the times of important events " +
				"in both Tray and Solver and possible moves for each " +
				"call to getPossibleMoves and the successful and " +
				"unsucessful moves that Solver makes");
	}
	
	private Block splitTokens(String s) {
		/*
		 * Takes a string and splits the string up by spaces so that
		 * it can parse the different arguments
		 * @precondition: s is a line of 4 valid integers with spaces in between
		 * @postcondition: returns a block that fulfills these integers
		 */
		String [] tokens = s.split(" ");
		int x=Integer.parseInt(tokens[0]);
		int y=Integer.parseInt(tokens[1]);
		int z=Integer.parseInt(tokens[2]);
		int w=Integer.parseInt(tokens[3]);
		Block block = new Block (x,y,z,w);
		return block;
	}
}