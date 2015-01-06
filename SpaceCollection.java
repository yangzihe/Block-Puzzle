//Valerie Cook, Jennifer Dai, Daniel Radding, Yangzi He

import java.util.*;
/*
 * A SpaceCollection object is a mutable ArrayList that stores
 * all the spaces throughout the board.
 */
public class SpaceCollection implements Iterable<Space> {
	
	ArrayList<Space> myEmptySpaces;

	public SpaceCollection() {
		/*
		 * Initializes a space collection.
		 */
		myEmptySpaces = new ArrayList<Space>();
	}
	
	public Iterator<Space> iterator(){
		/*
		 * Calls the ArrayList iterator.
		 */
		return myEmptySpaces.iterator();
	}
	
	public void add(Space s) {
		/*
		 * Adds the space that is passed in to the space collection.
		 * @precondition: s has to be a valid space.
		 */
		myEmptySpaces.add(s);
	}
	
	public boolean contains(Space s) {
		/*
		 * Sees if the space collection contains the space
		 * @precondition: s is a valid space
		 */
		return myEmptySpaces.contains(s);
	}
	
	public void remove(Space s) {
		/*
		 * Removes the space from the space collection
		 * @precondition: the space is in the space collection
		 */
		myEmptySpaces.remove(s);
	}
	
	public int size(){
		/*
		 * Calls ArrayList's size method
		 * @precondition: the ArrayList exists
		 * Returns the ArrayList's size method.
		 */
		return myEmptySpaces.size();
	}
	
	public Space get(int index) {
		/*
		 * Gets the space at the given index
		 * Returns a Space object
		 */
		return myEmptySpaces.get(index);
	}
}
