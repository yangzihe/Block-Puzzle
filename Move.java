//Valerie Cook, Jennifer Dai, Daniel Radding, Yangzi He

public class Move {
	/*
	 * A Move represents a set of non-negative integers. 
	 * The integers are the old row and old column 
	 * of the block's upper left corner and the 
	 * new row and new column of the block's upper left
	 * corner after it has moved.
	 * It also contains the direction the block moved.
	 */
	
	public int oldUpperRow; // old upper left corner row of the block
	public int oldUpperColumn; //  old upper left corner column of the block
	public int newUpperRow; //  new upper left corner row of the block
	public int newUpperColumn; //  new upper left corner column of the block
	public Block myBlock; // the Block that was moved
	public Direction myDir; // the direction it moved
	
	// Directions that a block can move
	protected static enum Direction {
		Up,Down,Left,Right;
	}
	
	public Move(Block b, Direction dir) {
		/*
		 * Creates a move object given a block and a direction.
		 * @precondition: Block has to be a valid block and direction
		 * has to be up, down, left, and right.
		 * @postcondition: Constructs a move
		 */
		myBlock = b;
		myDir = dir;
		oldUpperRow = b.getUpperRow();
		oldUpperColumn = b.getUpperColumn();
		
		// adjust new row and column based off of direction
		if (dir == Direction.Up) {
			newUpperRow = b.getUpperRow()-1;
			newUpperColumn = b.getUpperColumn();
		}
		if (dir== Direction.Down) {
			newUpperRow = b.getUpperRow()+1;
			newUpperColumn = b.getUpperColumn();
		}
		if (dir==Direction.Left) {
			newUpperRow = b.getUpperRow();
			newUpperColumn = b.getUpperColumn()-1;
		}
		if (dir==Direction.Right) {
			newUpperRow = b.getUpperRow();
			newUpperColumn = b.getUpperColumn()+1;
		}
	}
	
	public String toString() {
		/*
		 * Changes the move integers to a string
		 * @precondition: Valid integers for oldUpperRow, oldUpperColumn,;
		 * newUpperRow, newUpperColumn
		 * @postcondition: returns a string
		 */
		return Integer.toString(oldUpperRow) + " " + Integer.toString(oldUpperColumn) 
		+ " " + Integer.toString(newUpperRow) + " " + Integer.toString(newUpperColumn);
	}
}
