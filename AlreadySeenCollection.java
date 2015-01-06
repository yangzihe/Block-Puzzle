//Valerie Cook, Jennifer Dai, Daniel Radding, Yangzi He
import java.util.*;

public class AlreadySeenCollection {
	/* 
	 * An AlreadySeenCollection is a collection of all of the 
	 * block collections we have already tried while solving
	 * the puzzle
	 */

	HashSet<BlockCollection> alreadySeen;	
	
	// A constructor that creates a new collection
	public AlreadySeenCollection() {
		alreadySeen = new HashSet<BlockCollection>();
	}
	
	// Adds a block collection to the current alreadySeenCollection
	public void add(BlockCollection bc) {
		BlockCollection bc2 = bc.clone();
		alreadySeen.add(bc2);
	}
	
	// Returns true if the alreadySeenCollection contains
	// the given blockCollection
	public boolean contains(BlockCollection bc) {
		return alreadySeen.contains(bc);
	}
	
	// Returns the number of blockCollections in the alreadySeenCollection
	public int size() {
		return alreadySeen.size();
	}
}
