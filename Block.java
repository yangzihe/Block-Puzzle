//Valerie Cook, Jennifer Dai, Daniel Radding, Yangzi He

public class Block {
	/*
	 * A block represents a set of non-negative integers, an UpperRow, 
	 * UpperColumn, LowerRow, and LowerColumn, which correspond to the
	 * block's position on the tray. The UpperRow and UpperColumn is the 
	 * block's upper left corner and the LowerRow, LowerColumn is the
	 * block's lower right corner's position on the tray 
	 */
	
	private int myUpperRow; // position of the upper left corner row
	private int myUpperColumn; // position of the upper left corner column
	private int myLowerRow; // position of the lower right corner row
	private int myLowerColumn; // position of the lower right corner column
	
	// A constructor of a new Block given the block's position on the tray
	public Block(int upperRow, int upperColumn, int lowerRow, int lowerColumn) {
		myUpperRow = upperRow;
		myUpperColumn = upperColumn;
		myLowerRow = lowerRow;
		myLowerColumn = lowerColumn;
	}
	
	// Returns the length of the Block
	public int length() {
		return myLowerRow - myUpperRow + 1;
	}
	
	// Returns the width of the Block
	public int width() {
		return myLowerColumn - myUpperColumn + 1;
	}
	
	// Returns the position of the block's upper left corner's row
	public int getUpperRow() {
		return myUpperRow;
	}
	
	// Returns the position of the block's lower right corner's row
	public int getLowerRow() {
		return myLowerRow;
	}
	
	// Returns the position of the block's upper left corner's column
	public int getUpperColumn() {
		return myUpperColumn;
	}
	
	// Returns the position of the block's lower right corner's column
	public int getLowerColumn() {
		return myLowerColumn;
	}
	
	// Changes the value of the Block's upper left corner's row
	// if the value is legal considering the size of given tray
	public void setUpperRow(int coord, int limit){
		if(isValid(coord, limit)){
			myUpperRow = coord;
		}
	}
	
	// Changes the value of the Block's lower right corner's row
	// if the value is legal considering the size of given tray
	public void setLowerRow(int coord, int limit){
		if(isValid(coord, limit)){
			myLowerRow = coord;
		}
	}
	
	// Changes the value of the Block's upper left corner's column
	// if the value is legal considering the size of given tray
	public void setUpperColumn(int coord, int limit){
		if(isValid(coord, limit)){
			myUpperColumn = coord;
		}
	}
	
	// Changes the value of the Block's lower right corner's column
	// if the value is legal considering the size of given tray
	public void setLowerColumn(int coord, int limit){
		if(isValid(coord, limit)){
			myLowerColumn = coord;
		}
	}
	
	// Returns true if the coord is within limit of the tray
	// the limit passed in will either be the width or length
	// of the current tray and the coord will be the position 
	// we are trying to move the block into 
	private boolean isValid(int coord, int limit){
		return coord>=0 && coord<=limit;
	}
	
	// Returns a clone of the current block by making a new block
	// with the same upper left corner's row and column and lower  
	// right corner's row and column
	public Block clone() {
		return new Block(myUpperRow, myUpperColumn, myLowerRow, myLowerColumn);
	}
	
	// Returns true if the given block has the same upper left corner coords
	// and same lower right corner coords
	public boolean equals(Object obj) {
		Block b = (Block) obj;
		return myUpperRow == b.getUpperRow() && myUpperColumn == b.getUpperColumn()
				&& myLowerRow == b.getLowerRow() && myLowerColumn == b.getLowerColumn();
	}
	
	// Returns a hashCode based on the block's current position 
	public int hashCode(){
		int hash = 0;
		if (myLowerRow < 16 && myUpperColumn < 7) {
			hash = (int) Math.pow(2, myLowerRow) + (int) Math.pow(31, myUpperColumn);
		}
		else {
			hash = (int)(myUpperRow*1.5) + 31 * (int)(myLowerColumn*2.34) + (int)(width()*3.13)+(int)(length()*9.087129);
		}
		 return hash;
	}
	
	// Returns the block's upper left corner's row and column and lower 
	// right corner's row and column as a string with a space
	// between each number
	public String toString() {
		return myUpperRow + " " + myUpperColumn + " " + myLowerRow + " " + myLowerColumn;
	}	
}