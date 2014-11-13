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
	private static final float MIN_ZOOM = 1f;
	private static final float MAX_ZOOM = 6f;
	
	public DefaultMap() {
		super(MIN_ZOOM, MAX_ZOOM);

		// k is a scaling factor designed to make Towers appear on the screen nicely.
		final float k = 180f;

		//{ADD_NEW_TOWER(x,y)       // logical pos  //index in towers list
		
		// center
		addNewTower(0, 0);							//0
		
		// inner-pentagon vertices
		addNewTower(0, 100/k);      // top			//1
		addNewTower(-95/k, 31/k);   // top-left     //2
		addNewTower(-59/k, -81/k);  // bottom-left  //3
		addNewTower(59/k, -81/k);   // bottom-right //4
		addNewTower(95/k, 31/k);    // top-right    //5
		
		// outer-star vertices
		addNewTower(-82/k, 113/k);  // top-left     //6
		addNewTower(-133/k, -43/k); // bottom-left  //7
		addNewTower(0, -140/k); 	// bottom		//8
		addNewTower(133/k, -43/k);  // bottom-right //9
		addNewTower(82/k, 113/k);   // top-right    //10
		

		
		for(int i=6; i<=10; i++) {
			// pentagon magic numbers... not really that important to understand
			final int innerI = (i % 5) + 1;
			final int innerJ = ((i+1) % 5) + 1;
			final int outer = innerI + 5;
			
			// center is adjacent to all inner pentagon vertices
			// ignoring innerJ because it'll get hit in another iteration
			addBidirectionalEdge(innerI, 0);
			
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