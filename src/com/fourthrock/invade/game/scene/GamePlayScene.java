package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.CIRCLE;
import static com.fourthrock.invade.draw.DrawEnum.SQUARE;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.AI;
import com.fourthrock.invade.game.Human;
import com.fourthrock.invade.game.Player;
import com.fourthrock.invade.game.PlayerUnit;
import com.fourthrock.invade.game.Tower;
import com.fourthrock.invade.game.maps.DefaultMap;
import com.fourthrock.invade.game.maps.Map;
import com.fourthrock.invade.game.physics.Collision;
import com.fourthrock.invade.game.physics.ColoredCircleCollider;

/**
 * Main scene for actual Invade gameplay.
 * Handles logical and graphical state updates.
 * 
 * @author Joseph
 *
 */
public class GamePlayScene extends ZoomAndPanScene {
	private static final float MIN_ZOOM = 1f;
	private static final float MAX_ZOOM = 10f;
	
	private final Human human;
	private final List<Player> players;
	private final List<Tower> towers;
	private final ColoredCircleCollider collider;
	
	public GamePlayScene(final Map map, final Color humanColor) {
		super(MIN_ZOOM, MAX_ZOOM, map.getBounds());
		
		this.towers = map.getTowers();
		
		players = new ArrayList<>();
		final List<Color> playerColors = map.getColorsNotEqual(humanColor);
		for(final Color c : playerColors) {
			players.add(new AI(c));
		}
		human = new Human(humanColor);
		players.add(human);

		collider = new ColoredCircleCollider(map.getBounds(), Tower.RADIUS);
	}

	public GamePlayScene() {
		this(new DefaultMap(), Color.GREEN);
	}

	@Override
	public void handleTap(final float screenX, final float screenY) {
		// TODO - convert screen coords to world space
		final float x = screenX;
		final float y = screenY;
		human.updateTarget(x, y);
		
		// TODO - add some graphical response
	}

	@Override
	public Scene step(final long dt) {
		if(!moreThanOnePlayerAlive()){
			final Player p = players.get(0);
			return new EndGameScene(p, p == human);
		} else {
			for(final Tower t : towers) {
				collider.placeCircle(t);
			}
			for(final Player p : players) {
				p.tryGenerateUnit(dt);
				p.decideTarget();
				p.moveUnits(collider, dt);
			}
			processCollisions(collider.getCollisions());
			return this;		
		}
	}

	private void processCollisions(final PriorityQueue<Collision> collisions) {
		// TODO - so much to do!
		// 1. move units off walls
		// 2. update unit states
	}

	private boolean moreThanOnePlayerAlive() {
		return players.size() > 1;
	}

	@Override
	public void render(CanvasRenderer renderer) {
		for(final Tower t : towers) {
			renderer.draw(SQUARE, t.getPosition(), t.getScale(), t.getColor());
		}
		for(final Player p : players) {
			for(final PlayerUnit u : p.getUnits()) {
				renderer.draw(CIRCLE, u.getPosition(),
							  PlayerUnit.SCALE, u.getRenderColor());
			}
		}
	}

}
