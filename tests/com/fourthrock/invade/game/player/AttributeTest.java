package com.fourthrock.invade.game.player;

public class AttributeTest {
	public static void main(String[] args) {
		for(int i=1; i<=12; i++) {
			System.out.println(i + ": " + lvl(i));
		}
		System.out.println("Hello");
	}

	private static long lvl(final int lvl) {
		// O(n^3 / logn) function for level growth.
		return 4*(long)((lvl * lvl * lvl) / Math.log(lvl + 1));
	}
}
