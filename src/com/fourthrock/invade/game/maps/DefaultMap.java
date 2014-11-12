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
 *    There is a center vertex, an inner pentagon, and an outer pentagon.
 */
public class DefaultMap extends Map {
	
	public DefaultMap() {
		super();

		final float k = 180f;

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
			// center is adjacent to all inner pentagon vertices
			addBidirectionalEdge(i, 10);
			
			// inner pentagon vertices that share a side are adjacent
			addBidirectionalEdge(i, (i+1) % 5);
			
			// inner-pentagon vertices are adjacent to closest outer vertices
			addBidirectionalEdge(      i, i+5);
			addBidirectionalEdge((i+1)%5, i+5);
		}
	}
	
	private void addBidirectionalEdge(final int index1, final int index2) {
		towers.get(index1).setAdjacentTo(towers.get(index2));
		towers.get(index2).setAdjacentTo(towers.get(index1));
	}

}