//Valerie Cook, Jennifer Dai, Daniel Radding, Yangzi He

import java.util.*;

public class BlockCollection implements Iterable<Block>{
	/*
	 * A BlockCollection represents a collection of blocks
	 * on the current tray
	 */
	
	private ArrayList<Block> myBlocks;

	// A constructor for a new blockCollection
	public BlockCollection() {
		myBlocks = new ArrayList<Block>();
	}
	
	// Adds a block to the current BlockCollection
	public void add(Block b) {
		myBlocks.add(b);
	}

	// Returns the number of blocks in the current BlockCollection
	public int size(){
		return myBlocks.size();
	}

	// Returns an iterator that iterates through each block in the collection
	public Iterator<Block> iterator() {
		return myBlocks.iterator();
	}

	// Returns true if the current BlockCollection contains the given block
	public boolean contains (Block b) {
		return myBlocks.contains(b);
	}
		 
	// Return the block that has a corner with the given row and column
	// if there is such a block in the collection 
	// otherwise it returns null
	public Block getBlockWithCorner(int row, int column) {
		// check each block in the collection
		for (Block b : myBlocks) {
			// check if the block has a corner with the given row and column
			if ((b.getUpperRow()==row && b.getUpperColumn()==column) 
					|| (b.getLowerRow()==row && b.getLowerColumn()==column)) {
				// if it does, return the block
				return b;
			}
		}
		// if it doesn't, return null
		return null;
	}

	// Returns a clone of the current BlockCollection by cloning
	// each block in the collection and adding it to a new
	// BlockCollection
	public BlockCollection clone() {
		BlockCollection clone = new BlockCollection();
		for(Block b: myBlocks) {
			clone.add(b.clone());
		}
		return clone;
	}

	// Returns true if the current block collection contains all the 
	// blocks in the given block collection
	public boolean containsAll(BlockCollection bc) {
		return myBlocks.containsAll(bc.myBlocks);
	}

	// Returns false if the given blockCollection doesn't contain
	// every block the current blockCollection
	// precondition: both block collections will be the same size
	// because we only move blocks, never add or remove them while
	// solving a puzzle
	public boolean equals(Object obj) {
		BlockCollection bc = (BlockCollection) obj;
		for (Block b: myBlocks) {
			if ( !bc.contains(b)) return false;
		}
		return true;
	}

	// Returns a hashCode which is the sum of the hashCodes of each
	// block in the current BlockCollection
	public int hashCode() {
		int hash = 0;
		for (Block b : myBlocks) {
			hash += b.hashCode();
		}
		return hash;
	}

}
