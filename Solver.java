//Valerie Cook, Jennifer Dai, Daniel Radding, Yangzi He
import java.util.*;
/*
 * A Solver object is a mutable object that contains a BlockCollection goal,
 * an ArrayList of blocks that contains the goal configuration, 
 * a Tray, which contains a BlockCollection, and nodes that contain information
 * regarding how we traverse our tree. We also have a HashSet that is an
 * AlreadySeenCollection, and booleans and integers for debugging purposes. 
 */
public class Solver {
	
	private BlockCollection myGoals; // collection of blocks in goal
	private Tray myTray; // the current tray of the puzzle
	private Node myRoot; 
	private Node currentNode; 
	private boolean solved; // true when the puzzle has a solution
	private AlreadySeenCollection alreadySeen; // collection of previouslySeen blockCollections
	private boolean debugCount; // true when debug counting options are turned on
	private boolean debugTime; // true when debug timing options are turned on 
	private boolean debugEachMove; // true when highest level debugging is turned on 
	private int countAddPossibleMoves; // count of calls to addPossibleMove
	private int countMakeMove; // count of calls to makeMove
	private int countUndoMove; // count of calls to undoMove
	private int countGetNextNode; // count of calls to getNextNode
	private int countFoundAlreadySeen; // count of times we encountered a tray we'd already seen
	private int countSolution; // count of moves in the solution
	
	// Events that we are timing
	private enum DebugEvent {
		MakeMoves, UndoMoves, AddPossibleMoves, CheckAlreadySeen,
		AddToAlreadySeen, CheckMatchesGoal, GetNextNode, 
		FindSolution, PrintSolution;
	}
	
	// Lists of the timers we are keeping
	private long [] debugTimes = new long [DebugEvent.values().length];
	private long [] startTimes = new long [DebugEvent.values().length];
	
	
	public Solver( Tray tray, BlockCollection goals) {
		/*
		 * A constructor for Solver that takes a valid tray and
		 * a valid goal BlockCollection.
		 */
		if (SolverChecker.debugSolver) setDebugLevel(SolverChecker.debugLevel); 
		myTray = tray;
		myGoals = goals;
		myRoot = new Node();
		currentNode = myRoot;
		alreadySeen = new AlreadySeenCollection();
	}
	
	// Sets the debug options based on the specified level
	private void setDebugLevel(int level) {
		if (level==1||level>2) debugCount = true;
		if (level==2||level>2) debugTime = true;
		if (level==4) debugEachMove = true;
	}
	
	// Starts the timer of a specified event
	private void startTimer(DebugEvent de) {
		if (debugTime) startTimes[de.ordinal()] = System.currentTimeMillis();
	}
	
	// Stops the timer of a specified events
	private void stopTimer(DebugEvent de) {
		if (debugTime) {
			long stopTime = System.currentTimeMillis();
			debugTimes[de.ordinal()] += stopTime - startTimes[de.ordinal()];
		}
	}
	
	// Prints the debug results for the specified debugging options
	private void printDebugResults() {
		if(debugCount) {
			System.out.println("=========================================");
			System.out.println("Solver Debugging Counts:");
			System.out.println ("    MakeMove called " + countMakeMove + " times");
			System.out.println ("    UndoMove called " + countUndoMove + " times");
			System.out.println ("    AddPossibleMoves called " + countAddPossibleMoves + " times");
			System.out.println ("    GetNextNode called " + countGetNextNode + " times");
			System.out.println ("    Found an alreadySeen tray " + countFoundAlreadySeen + " times");
			System.out.println ("    AlreadySeen size is " + alreadySeen.size());
			System.out.println ("    The solution is " + countSolution + " moves");
			System.out.println("=========================================");
		}
		if (debugTime) {
			System.out.println("=========================================");
			System.out.println("Solver Debugging Times: ");
			for(DebugEvent de:DebugEvent.values()) {
				System.out.println("    " + de.name() + " took " + debugTimes[de.ordinal()] 
						+ " ms");
			}
			System.out.println("=========================================");
		}
	}

	// Finds the solution of the puzzle
	private void findSolution() {
		startTimer(DebugEvent.FindSolution);
		// check if the start tray matches the goal
		solved = matchesGoal();
		if ( !solved ) {
			// if the puzzle isn't solved, add current tray to alreadySeen
			alreadySeen.add(myTray.getMyBlocks());
			// add possible moves to the tree
			addPossibleMoves();
			// go to the next node
			getNextNode();
			while (currentNode != myRoot && !solved) {
				// if we've made an invalid move, we go to the next node
				if (! makeMove()) getNextNode();
				else {
					// otherwise, see if the move solved the puzzle
					if(matchesGoal()) solved = true;
					else {
					// if it doesn't, we add the new possible moves	to the tree
					addPossibleMoves();
					// then go to the next node
					getNextNode();
					}
				}
			}
		}
		stopTimer(DebugEvent.FindSolution);
	}
	
	// Returns true if current tray matches goal
	private boolean matchesGoal() {
		startTimer(DebugEvent.CheckMatchesGoal);
		// true if the current blocks contains all the goals
		boolean matches =  myTray.getMyBlocks().containsAll(myGoals); 
		stopTimer(DebugEvent.CheckMatchesGoal);
		return matches;
	}
	
	// Adds possible moves to the tree
	private void addPossibleMoves() {
		if (debugCount) countAddPossibleMoves++;
		startTimer(DebugEvent.AddPossibleMoves);
		// make an array list of possible moves for the current tray
		ArrayList<Move> moves=myTray.getPossibleMoves();
		// for each possible move, make a node and add it to the tree
		for (Move m: moves) { 
			Node newNode = new Node (m, currentNode);
			// if the block we are moving isn't in the goal file
			if (!myGoals.contains(m.myBlock)) {
					// put it at the beginning
					currentNode.myChildren.add(0, newNode);  
			} else
			currentNode.myChildren.add(newNode);  				
		}
		stopTimer(DebugEvent.AddPossibleMoves);
	}
	
	// Changes current node to the next possible move
	private void getNextNode() {
		if (debugCount) countGetNextNode++;
		startTimer(DebugEvent.GetNextNode);
		// if there are no possible moves
		if(currentNode.myChildren.size()==0) {
			// return if it is the root (puzzle is impossible)
			if (currentNode == myRoot) {
				stopTimer(DebugEvent.GetNextNode);
				return;
			}
			// otherwise undo the current move
			undoMove();
			stopTimer(DebugEvent.GetNextNode);
			// go to the next node
			getNextNode();
			// if there are possible moves
			} else {
				// check if the next move is the reverse of the move we just made
			if(isReverseMove(currentNode.myChildren.get(0).myMove)) {
				// if it is then remove it from the tree
				currentNode.myChildren.remove(0);
				// if there are no more possible moves
				if (currentNode.myChildren.size()==0) {
					stopTimer(DebugEvent.GetNextNode);
					// get the next node
					getNextNode();
					stopTimer(DebugEvent.GetNextNode);
					return;
				}
			} 
			// otherwise the change the current node to the first child
			currentNode = currentNode.myChildren.get(0);
		}
		stopTimer(DebugEvent.GetNextNode);
	}
	
	// Returns true when the given move is the reverse of the current node's move
	private boolean isReverseMove(Move m) {
		// cannot be the reverse of the root (root has no move)
		if (currentNode==myRoot) return false;
		Move currentMove = currentNode.myMove;
		return m.newUpperColumn==currentMove.oldUpperColumn
				&& m.newUpperRow==currentMove.oldUpperRow
				&& m.oldUpperColumn==currentMove.newUpperColumn
				&& m.oldUpperRow==currentMove.newUpperRow;
	}
	
	
	// Return true when we make a successful move
	private boolean makeMove() {
		if (debugCount) countMakeMove++;
		if (currentNode.myMove == null) return false;
		startTimer(DebugEvent.MakeMoves);
		// tell tray to make a move
		myTray.makeMove(currentNode.myMove.myBlock, currentNode.myMove.myDir);
		// check if this tray has been seen before
		startTimer(DebugEvent.CheckAlreadySeen);
		if (alreadySeen.contains(myTray.getMyBlocks())) {
			if (debugCount) countFoundAlreadySeen++;
			stopTimer(DebugEvent.CheckAlreadySeen);
			// if we have seen this tray before, then undo the move
			undoMove();
			stopTimer(DebugEvent.MakeMoves);
			// return false to say it was a bad move and we undid it
			return false;
		}
		else { //otherwise if we haven't seen the tray before
			stopTimer(DebugEvent.CheckAlreadySeen);
			startTimer(DebugEvent.AddToAlreadySeen);
			// add current tray to alreadySeen
			alreadySeen.add(myTray.getMyBlocks());
			stopTimer(DebugEvent.AddToAlreadySeen);
			stopTimer(DebugEvent.MakeMoves);
			// prints when highest debugging is on to show which successful move we made
			if (debugEachMove) {
				System.out.println("!!!!!!!!!!!!!!Successful Move!!!!!!!!!!!!");
				System.out.println("Block     : " + currentNode.myMove.myBlock);
				System.out.println("Direction : " + currentNode.myMove.myDir);
				System.out.println("Move      : " + currentNode.myMove);
				System.out.println();
				System.out.println();
			}
			// return true to say we made a successful move
			return true;
		}
	}
	
	// Undoes a move
	private void undoMove() {
		if (debugCount) countUndoMove++;
		startTimer(DebugEvent.UndoMoves);
		Move.Direction dir = currentNode.myMove.myDir; // current node's direction
		// get opposite direction of the current node's direction
		if (dir == Move.Direction.Up) dir = Move.Direction.Down;
		else if (dir == Move.Direction.Down) dir = Move.Direction.Up;
		else if (dir == Move.Direction.Right) dir = Move.Direction.Left;
		else if (dir == Move.Direction.Left) dir = Move.Direction.Right;
		
		// tell tray to make the a move with the same block in the opposite direction
		myTray.makeMove(currentNode.myMove.myBlock, dir);
		
		// prints when highest debugging is on to show which unsuccessful move we tried to make
		if (debugEachMove) {
			System.out.println("~~~~~~~~~~~~~Unsuccessful Move~~~~~~~~~~~~");
			System.out.println("Block     : " + currentNode.myMove.myBlock);
			System.out.println("Direction : " + currentNode.myMove.myDir);
			System.out.println("Move      : " + currentNode.myMove);
			System.out.println();
			System.out.println();
		}
		// set current node to its parent
		currentNode = currentNode.myParent;
		// remove the bad move from the tree
		currentNode.myChildren.remove(0);
		stopTimer(DebugEvent.UndoMoves);
	}
	
	// Prints Solution
	private void printSolution() {
		// exit if there is no solution
		if(solved==false) System.exit(1);
		else {
			startTimer(DebugEvent.PrintSolution);
			// print moves of the solution from the beginning
			currentNode = myRoot;
			while ( !currentNode.myChildren.isEmpty()) {
				currentNode=currentNode.myChildren.get(0);
				System.out.println (currentNode.myMove.toString());
				if (debugCount) countSolution++;
			}
		}
		stopTimer(DebugEvent.PrintSolution);
	}
	
	/*
	 * An node object in which we store our moves, a parent Node, and
	 * an ArrayList of children.
	 */
	private class Node {
			
			private Move myMove; // current move
			private Node myParent; // previous move
			private ArrayList<Node> myChildren; // all possible moves given the current tray
			
			// A constructor that creates a new node
			private Node(Move m, Node previousMove) {
				myMove = m;
				myParent = previousMove;
				myChildren = new ArrayList<Node>();
			}
			
			// A constructor for the root node
			private Node() {
				myMove = null;
				myParent = null;
				myChildren = new ArrayList<Node>();
			}
	}
	
	
    public static void main (String [ ] args) {
        SolverChecker checker = new SolverChecker (args);
        Tray tray=new Tray(checker.length, checker.width, checker.blocks);
        Solver solver = new Solver (tray,checker.goals);
    	solver.findSolution();
		solver.printSolution();
		solver.myTray.printDebugResults();
		solver.printDebugResults();
    }
}