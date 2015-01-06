//Valerie Cook, Jennifer Dai, Daniel Radding, Yangzi He

/*	 * A Space object is a mutable object that has a row and column integer.
	 * It contains all the coordinates of the spaces in the board.
	 */
public class Space {
	
	private int myRow;
	private int myColumn;
	
	public Space(int row, int column) {
		/*
		 * Constructs a tray object
		 * @precondition: row and column integers have to be valid
		 * within the board
		 */
		myRow = row;
		myColumn = column;
	}
	
	public int [] getPosition() {
		/*
		 * Gets the position of the space and returns
		 * an int array of the space coordinates.
		 */
		int [] rtn = new int [2];
		rtn[0] = myRow;
		rtn[1] = myColumn;
		return rtn;
	}
	
	public int getRow() {
		/*
		 * Gets the row of the space.
		 * Returns an integer.
		 */
		return myRow;
	}
	
	public int getColumn() {
		/*
		 * Gets the column of the space.
		 * Returns an integer.
		 */
		return myColumn;
	}
	
	public void setColumn(int column) {
		/*
		 * Sets the column of the space.
		 * @precondition: column has to be a valid integer in
		 * the confines of the board.
		 */
		myColumn = column;
	}
	
	public void setRow(int row) {
		/*
		 * Sets the row of the space.
		 * @precondition: row has to be a valid integer in
		 * the confines of the board.
		 */
		myRow = row ;
	}
	
	public boolean equals(Object obj) {
		/*
		 * Checks whether "this" space object equals the space object
		 * that is passed in.
		 * @precondition: obj is a valid space object
		 */
		Space s = (Space) obj;
		return myRow==s.myRow && myColumn ==s.myColumn;
	}
	
	public String toString() {
		/*
		 * Returns a string that contains the row and column
		 * instance variable in the space class.
		 */
		return myRow + " " + myColumn;
	}
	
}
