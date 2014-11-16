package com.fourthrock.invade.game.maps;


/**
 *	Map shaped like this (sorry for lack of ASCII symmetry):
 *
 *        O     O
 *       /  \ /  \
 *      | ___O___ |    
 *      O/___|___\O  
 *     / \   O   / \
 *    O  \  / \ /   O      
 *     \___O---O___/
 *          \ /
 *           O
 *    
 *  There is a center vertex, an inner pentagon, and an outer pentagon.
 *    
 *  Supports up to 11 players.
 *  
 *  @author Joseph
 */
public class DefaultMap extends Map {
	
	public DefaultMap() {
		super();

		// k is a scaling factor designed to make Towers spaced nicely
		final float k = 2.7f;

		//{ADD_NEW_TOWER(x,y)       // logical pos  //index in towers list
		
		// inner-pentagon vertices
		addNewTower(0, 100/k);      // top			//0
		addNewTower(-95/k, 31/k);   // top-left     //1
		addNewTower(-59/k, -81/k);  // bottom-left  //2
		addNewTower(59/k, -81/k);   // bottom-right //3
		addNewTower(95/k, 31/k);    // top-right    //4
		
		// outer-star vertices
		addNewTower(-82/k, 113/k);  // top-left     //5
		addNewTower(-133/k, -43/k); // bottom-left  //6
		addNewTower(0, -140/k); 	// bottom		//7
		addNewTower(133/k, -43/k);  // bottom-right //8
		addNewTower(82/k, 113/k);   // top-right    //9
		
		// center
		addNewTower(0, 0);							//10
		

		
		for(int i=0; i<5; i++) {
			// pentagon magic numbers... not really that important to understand
			final int innerI = i;
			final int innerJ = ((i+1) % 5);
			final int outer = innerI + 5;
			
			// center is adjacent to all inner pentagon vertices
			// ignoring innerJ because it'll get hit in another iteration
			addBidirectionalEdge(innerI, 10);
			
			// inner pentagon vertices that share a side are adjacent
			addBidirectionalEdge(innerI, innerJ);
			
			// inner-pentagon vertices are adjacent to closest outer vertices
			addBidirectionalEdge(innerI, outer);
			addBidirectionalEdge(innerJ, outer);
		}
	}
	
	private void addBidirectionalEdge(final int index1, final int index2) {
		towers.get(index1).setAdjacentTo(towers.get(index2));
	}

}