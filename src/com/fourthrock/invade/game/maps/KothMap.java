package com.fourthrock.invade.game.maps;

import com.fourthrock.invade.game.tower.Tower;


/**
 * KOTH (King Of The Hill) Map
 * 
 * Everyone starts in a corner
 * and has incentive to explore.
 * 
 * @author Joseph
 *
 */
public class KothMap extends Map {
	
	public KothMap() {
		super();
		
		final float octagonDist = 190f / Tower.SPAWN_RADIUS;
		final float octagonDist2 = (float) (octagonDist / Math.sqrt(2));
		final float innerSquareDist  = 0.5f * octagonDist;
		final float gamma = (float) (Math.sqrt(2) - 1f);
		final float panelFistDist = 0.8f * octagonDist2 / gamma;
		final float panelFistDist2 = gamma * panelFistDist;
		final float panelElbowDist = panelFistDist;
		
		// middle octagon Towers
		addSquareNorthSide(octagonDist2);			    //0, 1, 2, 3
		addSquareNorthVertex(octagonDist);              //4, 5, 6, 7
		
		// inner square Towers
		addSquare(innerSquareDist, -112.5f);			//8, 9, 10, 11
		
		// center Tower
		addNewTower(0, 0);				   				//12
		
		// panel elbow Towers
		addSquareNorthSide(panelElbowDist); 			//13, 14, 15, 16
		
		// panel neck Towers
		addSquareNorthVertex(1.2f * panelElbowDist); 	//17, 18, 19, 20
		
		// outermost panel Towers
		addSquareNorthVertex(1.6f * panelElbowDist); 		//21, 22, 23, 24
		
		// panel fist Towers
		addNewTower(  panelFistDist,  panelFistDist2);   //25
		addNewTower( panelFistDist2,  -panelFistDist);	 //26
		addNewTower( -panelFistDist, -panelFistDist2);   //27
		addNewTower(-panelFistDist2,   panelFistDist);	 //28
		

		//each octagon Tower is connected to its neighbor
		for(int i=0; i<3; i++) {
			final int leftTower = i+4;
			final int rightTower = i+5;
			addEdge(i, leftTower);
			addEdge(i, rightTower);
		}
		addEdge(3, 7); addEdge(3, 4); //above loop does not account for edge case.
		
		//each inner square Tower is connected to its neighbor
		//and the center tower
		for(int i=0; i<4; i++) {
			final int tI = i + 8;
			final int tNext = (i+1) % 4 + 8;
			addEdge(tI, tNext);
			addEdge(tI, 12);
		}
		
		//some octagon Towers are connected to inner square
		for(int i=0; i<4; i++) {
			final int oct = i;
			final int octNext = oct + 4;
			final int inner1 = i + 8;
			final int inner2 = (i + 1) % 4 + 8;
			addEdge(oct, inner1);
			addEdge(oct, inner2);
			addEdge(octNext, inner1);
		}
		
		//connect the panels ~> octagon <-> fist <-> elbow <-> neck <-> outermost
		for(int i=0; i<4; i++) {
			final int oct = i;
			final int fist = oct + 25;
			final int elbow = (i+1) % 4 + 13;
			final int neck = elbow + 4;
			final int outer = neck + 4;
			addEdge(oct, fist);
			addEdge(fist, elbow);
			addEdge(elbow, neck);
			addEdge(neck, outer);
			if(i < 3) {
				final int octNext = fist - 20;
				addEdge(octNext, fist);
				addEdge(octNext, neck);
			}
		}
		addEdge(4, 28); addEdge(4, 17); //couldn't add edge case for panels
		
	}
	
	private void addSquareNorthVertex(final float radius) {
		addSquare(radius, -90f);
	}
	
	private void addSquareNorthSide(final float radius) {
		addNewTower( 1,  1, radius);
		addNewTower( 1, -1, radius);
		addNewTower(-1, -1, radius);
		addNewTower(-1,  1, radius);
	}
	
	private void addSquare(final float radius, final float angleOffset) {
		for(int i=0; i<4; i++) {
			final float angle = -((90f * i + angleOffset) % 360f) * (float)(Math.PI / 180f);
			final float x = (float)(Math.cos(angle));
			final float y = (float)(Math.sin(angle));
			addNewTower(x, y, radius);
		}
	}
	
	private Tower addNewTower(final float x, final float y, final float scale) {
		return addNewTower(scale * x, scale * y);
	}
	
	private void addEdge(final int i, final int j) {
		towers.get(i).setAdjacentTo(towers.get(j));
	}
}
