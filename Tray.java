//Valerie Cook, Jennifer Dai, Daniel Radding, Yangzi He

import java.util.*;
/*
 * A Tray object is a mutable data structure that contains
 * a BlockCollection, a SpaceCollection, integers that denote
 * the size of the tray, and debugging flags.
 */
public class Tray {	

	private BlockCollection myBlocks; // collection of blocks in the tray
	private int myLength; // length of the tray
	private int myWidth; // width of the tray
	private SpaceCollection myEmptySpaces; // collection of spaces in the tray
	private boolean debugCount; // true when debugging counting is turned on
	private boolean debugTime; // true when debugging timing is turned on 
	private boolean debugEachMove; // true when highest debug level is turned on
	protected static int countGetPossibleMoves; // count of calls to getPossibleMoves
	protected static int countCanMove; // count of calls to canMove
	protected static int countMakeMove; // count of calls to makeMove
	
	// events we are timing for debugging purposes
	private enum DebugEvent {
		FindEmptySpaces, GetPossibleMoves, CanMove, MakeMoves,
		GetBlocksAdjacentToEmpty;
	}
	
	private long [] debugTimes = new long [DebugEvent.values().length];
	private long [] startTimes = new long [DebugEvent.values().length];
	
	// A constructor for tray which takes 
	// length, width, and a BlockCollection
	//@precondition: length and width are valid integers
	//BlockCollection bc is a valid block collection
	public Tray(int length, int width, BlockCollection bc) {
		myLength = length;
		myWidth = width;
		myBlocks = bc;
		findEmptySpaces(); // find empty spaces
		if (SolverChecker.debugTray) {
			// set debugging level and check if tray is ok
			// if debugging is turned on for the tray class
			setDebugLevel(SolverChecker.debugLevel);
			try {
				isOK();
			}
			catch(IllegalStateException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	// sets the debug level depending on what level the user chose
	//@precondition: level has to be a valid integer 1-4
	private void setDebugLevel(int level) {
		if (level==1||level>2) debugCount=true;
		if (level==2||level>2) debugTime = true;
		if (level==4) debugEachMove=true;
	}
	
	// starts a timer for a specified debugging event
	// if debugging time is true
	private void startTimer(DebugEvent de) {
		if (debugTime) startTimes[de.ordinal()] = System.currentTimeMillis();
	}
	
	// stops a timer for a specified debugging event
	// if debugging time is true
	private void stopTimer(DebugEvent de) {
		if (debugTime) {
			long stopTime = System.currentTimeMillis();
			debugTimes[de.ordinal()] += stopTime - startTimes[de.ordinal()];
		}
	}
	
	// prints the debugging results
	public void printDebugResults() {
		if (debugCount) {
			// if debugging count is true, print counts
			System.out.println("=========================================");
			System.out.println("Tray Debugging Counts:");
			System.out.println("    Tray has " + myBlocks.size() + " blocks");
			System.out.println("    Tray has " + myEmptySpaces.size() + " spaces");
			System.out.println("    GetPossibleMoves called " + countGetPossibleMoves + " times");
			System.out.println("    CanMove called " + countCanMove + " times");
			System.out.println("    MakeMove called " + countMakeMove + " times");
			System.out.println("=========================================");
			
		}
		if (debugTime) {
			// if debugging time is true, print times
			System.out.println("=========================================");
			System.out.println("Tray Debugging Times: ");
			for(DebugEvent de:DebugEvent.values()) {
				System.out.println("    " + de.name() + " took " + debugTimes[de.ordinal()] 
						+ " ms");
			}
			System.out.println("=========================================");
		}
	}
	
	public BlockCollection getMyBlocks(){
		/*
		 * Returns the current BlockCollection
		 */
		return myBlocks;
	}
	
	public void findEmptySpaces () {
		// find the empty spaces on the tray
		startTimer(DebugEvent.FindEmptySpaces);
		myEmptySpaces = new SpaceCollection();
		// make every space of the tray an empty space
		for (int i = 0; i < myLength; i++) {
			for (int j = 0; j < myWidth; j++) {
				myEmptySpaces.add(new Space(i, j));
			}
		}
		// remove spaces if there is a block in that space
		for (Block b: myBlocks) {
			for (int i=0; i<b.length(); i++) {
				for (int j = 0; j<b.width(); j++) {
					myEmptySpaces.remove(new Space(b.getUpperRow()+i, b.getUpperColumn()+j));
				}
			}
		}
		stopTimer(DebugEvent.FindEmptySpaces);
	}
	
	// Gets possible moves given the list of blocks
	public ArrayList<Move> getPossibleMoves() {
		// given a list of blocks it will return all possible moves
		if (debugCount) countGetPossibleMoves++;
		startTimer(DebugEvent.GetPossibleMoves);
		
		
		// list to be returned of all possible moves
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		// list of blocks with corners adjacent to empty squares
		// which are the only blocks that might be able to move
		ArrayList<Block> blocksAdjacentToEmpty = getBlocksAdjacentToEmpty();;
		// check if the blocks can move in any direction
		for(Block b: blocksAdjacentToEmpty) {
			// if the block can move in a direction it creates a move
			// object and adds it to the ArrayList of possible moves
			if (canMove(b, Move.Direction.Up)) {
				Move m =  new Move(b, Move.Direction.Up);
				possibleMoves.add(m);
			}
			if (canMove(b, Move.Direction.Down)) {
				Move m = new Move(b, Move.Direction.Down);
				possibleMoves.add(m);
			}
			if (canMove(b, Move.Direction.Left)) {
				Move m = new Move(b, Move.Direction.Left);
				possibleMoves.add(m);
			}
			if (canMove(b, Move.Direction.Right)) {
				Move m = new Move(b, Move.Direction.Right);
				possibleMoves.add(m);
			}
	}
		stopTimer(DebugEvent.GetPossibleMoves);
		if (debugEachMove) {
			System.out.println("=========================================");
			System.out.println( "          " + countMakeMove + " moves made so far");
			System.out.println("=========================================");
			for (Block b: myBlocks) {
				System.out.println("Blocks: " + b);
			}
			System.out.println();
			for (int i=0; i<myEmptySpaces.size(); i++) {
				System.out.println("Spaces: " + myEmptySpaces.get(i).toString());
			}
			System.out.println();
			for (int i=0; i<possibleMoves.size(); i++) {
				System.out.println("Possible moves: " + possibleMoves.get(i));
			}
			System.out.println();
			System.out.println();
		}
		
		return possibleMoves;
	}
		
	// return an arrayList of Blocks that have either the upper left
	// or lower right corner that is adjacent to an empty square
	private ArrayList<Block> getBlocksAdjacentToEmpty() {
		startTimer(DebugEvent.GetBlocksAdjacentToEmpty);
		
		// list to be returned of blocks with corners adjacent to an empty square
		ArrayList<Block> blocksAdjacentToEmpty = new ArrayList<Block>();
		
		// check each space in empty spaces
		for (Space s: myEmptySpaces) {
			int row = s.getRow();
			int column = s.getColumn();
			// check for a block with a corner above the empty space
			Block aboveEmpty = myBlocks.getBlockWithCorner(row-1, column);
			// if there is such a block, add it to the list
			if (aboveEmpty!=null && !blocksAdjacentToEmpty.contains(aboveEmpty))
				blocksAdjacentToEmpty.add(aboveEmpty);
			
			// check for a block with a corner below the empty space
			Block belowEmpty = myBlocks.getBlockWithCorner(row+1, column);
			// if there is such a block, add it to the list
			if (belowEmpty!=null && !blocksAdjacentToEmpty.contains(belowEmpty))
				blocksAdjacentToEmpty.add(belowEmpty);
			
			// check for a block with a corner to the left of the empty space
			Block leftOfEmpty = myBlocks.getBlockWithCorner(row, column-1);
			// if there is such a block, add it to the list
			if (leftOfEmpty!=null && !blocksAdjacentToEmpty.contains(leftOfEmpty))
				blocksAdjacentToEmpty.add(leftOfEmpty);
			
			// check for a block with a corner to the right of the empty space
			Block rightOfEmpty = myBlocks.getBlockWithCorner(row, column+1);
			// if there is such a block, add it to the list
			if (rightOfEmpty!=null && !blocksAdjacentToEmpty.contains(rightOfEmpty))
				blocksAdjacentToEmpty.add(rightOfEmpty);
		}
		stopTimer(DebugEvent.GetBlocksAdjacentToEmpty);
		return blocksAdjacentToEmpty;
	}
	
	// Returns true if a given block can be moved in the given direction
	public boolean canMove(Block b, Move.Direction dir) {
		if (debugCount) countCanMove++; startTimer(DebugEvent.CanMove);
		if (dir == Move.Direction.Up) {
			// return false if it is in the top row
			if(b.getUpperRow()==0) { 
				stopTimer(DebugEvent.CanMove);
				return false;
				}
				// find squares it will be moving into
				for(int i=0; i<b.width(); i++) {
					Space e = new Space(b.getUpperRow()-1,b.getUpperColumn()+i);
					// return false if emptySpaces doesn't contain all of them
					if ( ! myEmptySpaces.contains(e) ) {
						stopTimer(DebugEvent.CanMove);
						return false;
					}		
				}
		} else if (dir == Move.Direction.Down) {
			 // return false if it is in the bottom row
			if(b.getUpperRow()==myLength-1) {
				stopTimer(DebugEvent.CanMove);
				return false;
			}
			// find squares it will be moving into
			for(int i=0; i<b.width(); i++) {
			Space e = new Space(b.getLowerRow()+1,b.getUpperColumn()+i);
			// return false if emptySpaces doesn't contain all of them
			if ( ! myEmptySpaces.contains(e) ) {
				stopTimer(DebugEvent.CanMove);
				return false;
			}
		}
		} else if (dir == Move.Direction.Left) {
			// return false if it is in the leftmost column
			if(b.getUpperColumn()==0) {
				stopTimer(DebugEvent.CanMove);
				return false;
			}
			// find squares it will be moving into
			for(int i=0; i<b.length(); i++) {
			Space e = new Space(b.getUpperRow()+i,b.getUpperColumn()-1);
			// return false if emptySpaces doesn't contain all of them
			if ( ! myEmptySpaces.contains(e) ) {
				stopTimer(DebugEvent.CanMove);
				return false;
			}
		}
		} else {
			// return false if it is in the rightmost column
			if(b.getLowerColumn()==myWidth-1) {
				stopTimer(DebugEvent.CanMove);
				return false;
			}
			// find squares it will be moving into
			for(int i=0; i<b.length(); i++) {
			Space e = new Space(b.getUpperRow()+i,b.getLowerColumn()+1);
			// return false if emptySpaces doesn't contain all of them
			if ( ! myEmptySpaces.contains(e) ) {
				stopTimer(DebugEvent.CanMove);
				return false;
			}
		}
		}
		stopTimer(DebugEvent.CanMove);
		return true;
	}
	
	// move a specific block in a given direction
	public void makeMove(Block b, Move.Direction dir) {
		if (debugCount) countMakeMove++;
		startTimer(DebugEvent.MakeMoves);
		if(dir == Move.Direction.Up){
			for(int i=b.getUpperColumn(); i<=b.getLowerColumn(); i++){
				// take old empty spaces out of tray
				myEmptySpaces.remove(new Space(b.getUpperRow()-1, i));
				// put new empty spaces into tray
				myEmptySpaces.add(new Space(b.getLowerRow(), i));
			}
			// change values of the block
			b.setUpperRow(b.getUpperRow()-1, myLength);
			b.setLowerRow(b.getLowerRow()-1, myLength);
			
		} else if(dir== Move.Direction.Down){
			for(int i=b.getUpperColumn(); i<=b.getLowerColumn(); i++){
				// take old empty spaces out of tray
				myEmptySpaces.remove(new Space(b.getLowerRow()+1, i));
				// put new empty spaces into tray
				myEmptySpaces.add(new Space(b.getUpperRow(), i));
			}
			// change values of the block
			b.setUpperRow(b.getUpperRow()+1, myLength);
			b.setLowerRow(b.getLowerRow()+1, myLength);
			
		} else if(dir==Move.Direction.Right){
			for(int i=b.getUpperRow(); i<=b.getLowerRow(); i++){
				// take old empty spaces out of tray
				myEmptySpaces.remove(new Space(i, b.getLowerColumn()+1));
				// put new empty spaces into tray
				myEmptySpaces.add(new Space(i, b.getUpperColumn()));
			}
			// change values of the block
			b.setUpperColumn(b.getUpperColumn()+1, myWidth);
			b.setLowerColumn(b.getLowerColumn()+1, myWidth);
		} else{
			for(int i=b.getUpperRow(); i<=b.getLowerRow(); i++){
				// take old empty spaces out of tray
				myEmptySpaces.remove(new Space(i, b.getUpperColumn()-1));
				// put new empty spaces into tray
				myEmptySpaces.add(new Space(i, b.getLowerColumn()));
			}
			// change values of the block
			b.setUpperColumn(b.getUpperColumn()-1, myWidth);
			b.setLowerColumn(b.getLowerColumn()-1, myWidth);
		}
		stopTimer(DebugEvent.MakeMoves);
		// check that the tray is still valid after the move was made
		// if debugging for tray is turned on
		if (SolverChecker.debugTray) isOK(); 
	}
	
	public void isOK() throws IllegalStateException{
		/*
		 * Checks whether the tray satisfies all the invariants
		 */
		if (myEmptySpaces.size() >= myWidth * myLength) {
			throw new IllegalStateException("Empty spaces exceed the maximum size of the Tray.");
		}
		for (Block b : myBlocks) {
			HashSet<Space> spacedBlock=makeBlockIntoSpaces(b);
			//checks to make sure that dimensions are correct relative to one another
			if (b.getUpperRow() > b.getLowerRow() 
						|| b.getUpperColumn() > b.getLowerColumn()){
					throw new IllegalStateException("Upper row and upper column should never be greater than "
							+ "lower row or lower column.");
				}
			//checks for overlapping blocks
			for (Block c: myBlocks) {
				if (b!=c) {
					isOverlapping(c,spacedBlock);
				}
				//checks to make sure block is not out of bounds
				int [] dimensions= new int [] {b.getUpperRow(), b.getUpperColumn(), b.getLowerRow(), b.getLowerColumn()};
				for(int i=0;i<dimensions.length;i++){
					if(dimensions[i]>myWidth || dimensions[i]>myLength || dimensions[i]<0)
					    throw new IllegalStateException("Block is out of the bounds of the Tray.");
			    }
			    //checks to see if any block spaces are in empty spaces
//				boolean [][] totalBoard=createBoard();
//				for(Space space : spacedBlock) {
//					if(totalBoard[space.getRow()][space.getColumn()]==false)
//						throw new IllegalStateException("Spaces and blocks overlap.");
//				}
			}
		}
	}
	public HashSet<Space> makeBlockIntoSpaces(Block b) {
		/*
		 * Creates a HashSet of the spaces within the blocks
		 * @precondition: the block exists and is valid
		 * @postcondition: returns a hashset of spaces
		 */
		HashSet<Space> toCompare = new HashSet<Space>();
		for (int i=0; i<b.length(); i++) {
			for (int j = 0; j<b.width(); j++) {
				toCompare.add(new Space(b.getUpperRow()+i, b.getUpperColumn()+j));
			}
		}
		return toCompare;
	}
	
	public void isOverlapping(Block b, HashSet<Space> toCompare) {
		/*
		 * Checks whether any of the spaces within
		 * block overlap with any of the spaces in the space hashset
		 * @precondition: valid block, valid hashset of spaces
		 * @postcondition: throws an exception if isn't valid
		 */
		for (int i=0; i<b.length(); i++) {
			for (int j = 0; j<b.width(); j++) {
				if (toCompare.contains(new Space(b.getUpperRow()+i, b.getUpperColumn()+j))) {
					throw new IllegalStateException("There are overlapping blocks on the tray.");
				}
			}
		}
	}
	
//	public boolean[][] createBoard() {
	/*
	 * In order to check whether our empty spaces overlap
	 * with blocks, create a 2D boolean array with the blocks
	 * and spaces on the board. 
	 * @precondition: blocks and spaces don't overlap
	 * @postcondition: returns a 2D boolean array
	 */
//		boolean [] [] totalBoard=new boolean[myLength][myWidth];
//		for(int i=0;i<myLength;i++) {
//			for(int j=0;j<myWidth;j++) {
//				if(myEmptySpaces.contains(new Space(i,j))) {
//					totalBoard[i][j]=false;
//				}
//				else {
//					totalBoard[i][j]=true;
//				}
//			}
//		}
//		return totalBoard;
//	}
	
}
