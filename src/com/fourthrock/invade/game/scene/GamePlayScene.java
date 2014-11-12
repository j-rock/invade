package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.CIRCLE;
import static com.fourthrock.invade.draw.DrawEnum.SQUARE;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.maps.DefaultMap;
import com.fourthrock.invade.game.maps.Map;
import com.fourthrock.invade.game.physics.ColoredCircleCollider;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.TowerCollision;
import com.fourthrock.invade.game.physics.UnitCollision;
import com.fourthrock.invade.game.player.AI;
import com.fourthrock.invade.game.player.Human;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;

/**
 * Main scene for actual Invade gameplay.
 * Handles logical and graphical state updates.
 * 
 * @author Joseph
 *
 */
public class GamePlayScene extends ZoomAndPanScene {
	private static final float MIN_ZOOM = 1f;  // TODO - determine values
	private static final float MAX_ZOOM = 10f; // ^
	
	private final Human human;
	private final List<Player> players;
	private final List<Tower> towers;
	private final ColoredCircleCollider collider;
	
	public GamePlayScene(final Map map, final Color humanColor) {
		super(MIN_ZOOM, MAX_ZOOM, map.getBounds());
		
		this.collider = new ColoredCircleCollider(map.getBounds(), Tower.RADIUS);
		this.towers = map.getTowers();
		
		this.players = new ArrayList<>();
		final List<Color> playerColors = map.getColorsNotEqual(humanColor);
		for(final Color c : playerColors) {
			players.add(new AI(c, collider));
		}
		human = new Human(humanColor, collider);
		players.add(human);
	}

	public GamePlayScene() {
		this(new DefaultMap(), Color.GREEN);
	}

	@Override
	public void handleTap(final Screen2D screenCoords) {
		final Position2D tapPos = convertScreenToWorld(screenCoords);
		human.updateTarget(tapPos);
		
		// TODO - add some graphical response
	}

	private Position2D convertScreenToWorld(final Screen2D screenCoords) {
		//TODO - implement
		return null;
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
				p.moveUnits(dt);
			}
			final Pair<List<TowerCollision>, List<UnitCollision>> colls = collider.withdrawCollisions();
			processCollisions(colls.first, colls.second);
			for(final Player p : players) {
				p.fireUnits(dt);
			}
			return this;
		}
	}

	private void processCollisions(final List<TowerCollision> towerColls, final List<UnitCollision> unitColls) {
		for(final TowerCollision tc : towerColls) {
			tc.u.setAttackingTower(tc.t);
			tc.u.moveOffTower(tc.t);
		}
		for(final UnitCollision uc : unitColls) {
			uc.u.setAttackingUnit(uc.u2);
		}

		// TODO - add graphical triggers
	}

	private boolean moreThanOnePlayerAlive() {
		return players.size() > 1;
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		for(final Tower t : towers) {
			renderer.draw(SQUARE, t.getPosition(), Tower.SCALE, t.getColor());
		}
		for(final Player p : players) {
			for(final PlayerUnit u : p.getUnits()) {
				renderer.draw(CIRCLE, u.getPosition(), PlayerUnit.SCALE, u.getRenderColor());
			}
		}
		//TODO - more rendering
	}

}
