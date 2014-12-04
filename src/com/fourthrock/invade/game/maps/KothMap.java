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
		
		final float octagonDist = 200f / Tower.SPAWN_RADIUS;
		final float octagonDist2 = (float) (octagonDist / Math.sqrt(2));
		final float innerSquareDist  = 0.5f * octagonDist;
		final float gamma = (float) (Math.sqrt(2) - 1f);
		final float cornerDist = 0.8f * octagonDist2 / gamma;
		final float cornerDist2 = gamma * cornerDist;
		
		// middle octagon Towers
		addSquareNorthSide(octagonDist2);		  //0, 1, 2, 3
		addSquareNorthVertex(octagonDist);        //4, 5, 6, 7
		
		// inner square Towers
		addSquare(innerSquareDist, -112.5f);	  //8, 9, 10, 11
		
		// center Heavy Tower
		addNewHeavyTower(0, 0);				   	  //12
		
		// corner Towers
		addNewLightTower(  cornerDist,  cornerDist2);  //13
		addNewLightTower( cornerDist2,  -cornerDist);  //14
		addNewLightTower( -cornerDist, -cornerDist2);  //15
		addNewLightTower(-cornerDist2,   cornerDist);  //16
		

		//each octagon Tower is connected to its neighbor
		for(int i=0; i<3; i++) {
			final int oct = i;
			final int leftOct = i+4;
			final int rightOct = i+5;
			addEdge(oct, leftOct);
			addEdge(oct, rightOct);
		}
		addEdge(3, 7); addEdge(3, 4); //above loop does not account for edge case.
		
		//each inner square Tower is connected to its neighbor
		//and the center tower
		for(int i=0; i<4; i++) {
			final int inner = i + 8;
			final int innerNext = (i+1) % 4 + 8;
			addEdge(inner, innerNext);
			addEdge(inner, 12);
		}
		
		//octagon Towers have connections to inner square
		for(int i=0; i<4; i++) {
			final int oct = i;
			final int octNext = oct + 4;
			final int inner1 = i + 8;
			final int inner2 = (i + 1) % 4 + 8;
			addEdge(oct, inner1);
			addEdge(oct, inner2);
			addEdge(octNext, inner1);
		}
		
		//octagon Towers have connections to corners
		for(int i=0; i<4; i++) {
			final int oct = i;
			final int octNext = oct + 5;
			final int corner = oct + 13;
			addEdge(oct, corner);
			if(i < 3) {
				addEdge(octNext, corner);
			} else {
				addEdge(4, 16);
			}
		}
		
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
