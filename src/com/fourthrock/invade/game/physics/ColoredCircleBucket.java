package com.fourthrock.invade.game.physics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Since Java does not allow arrays of generic types (such as List<T>[])
 * this class will wrap an ArrayList<ColoredCircle>
 * 
 * @author Joseph
 *
 */
public class ColoredCircleBucket implements Iterable<ColoredCircle> {
	private final List<ColoredCircle> circles;
	
	public ColoredCircleBucket() {
		circles = new ArrayList<>();
	}

	@Override
	public Iterator<ColoredCircle> iterator() {
		return circles.iterator();
	}
	
	public void clear() {
		circles.clear();
	}

	public void add(final ColoredCircle c) {
		circles.add(c);
	}
	
}
