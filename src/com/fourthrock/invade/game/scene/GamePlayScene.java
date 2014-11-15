package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.CIRCLE;
import static com.fourthrock.invade.draw.DrawEnum.SQUARE;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.draw.Screen2D;
import com.fourthrock.invade.game.maps.DefaultMap;
import com.fourthrock.invade.game.maps.Map;
import com.fourthrock.invade.game.physics.ColoredCircleCollider;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.TowerCollision;
import com.fourthrock.invade.game.physics.UnitCollision;
import com.fourthrock.invade.game.physics.Vector2D;
import com.fourthrock.invade.game.player.AI;
import com.fourthrock.invade.game.player.Human;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;
import com.fourthrock.invade.util.Index2D;

/**
 * Main scene for actual Invade gameplay.
 * Handles logical and graphical state updates.
 * 
 * @author Joseph
 *
 */
public class GamePlayScene extends WorldEyeScene {
	private final ColoredCircleCollider collider;
	private final List<Tower> towers;
	private final List<Index2D> towerEdges;
	private final List<Player> players;
	private final Human human;
	
	public GamePlayScene(final Map map, final Color humanColor) {
		super(map.getMinZoom(), map.getMaxZoom(), map.getTowers().get(0).getPosition(), map.getBounds());
		
		this.collider = new ColoredCircleCollider(map.getBounds());
		this.towers = map.getTowers();
		this.towerEdges = map.getAdjSet();
		this.players = new ArrayList<>();
		this.human = new Human(humanColor, collider);
		
		setupPlayersFromMap(map);
	}

	public GamePlayScene() {
		this(new DefaultMap(), Color.GREEN);
	}

	@Override
	public void handleTap(final Screen2D screenCoords) {
		super.handleTap(screenCoords);
		final Position2D tapPos = getPositionFromScreen(screenCoords);
		final Tower t = ColoredCircleCollider.findTower(human.getColor(), tapPos, towers);
		human.updateTarget(tapPos, t);
		
		// TODO - add some graphical response
	}

	@Override
	public Scene step(final long dt) {
		super.step(dt);
		
		if(!moreThanOnePlayerAlive()){
			// TODO - fix bad logic.
			final Player p = players.get(0);
			return new EndGameScene(p, p == human);
		} else {
			for(final Tower t : towers) {
				collider.placeCircle(t);
				t.regainHealth(dt);
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
			for(final Player p : players) {
				p.removeDeadUnits();
			}
			return this;
		}
	}

	private void processCollisions(final List<TowerCollision> towerColls, final List<UnitCollision> unitColls) {
		for(final TowerCollision tc : towerColls) {
			if(!tc.t.getColor().equals(tc.u.getColor())) {
				tc.u.setAttackingTower(tc.t);
			}
			tc.u.moveOffTower(tc.t);
		}
		for(final UnitCollision uc : unitColls) {
			// TODO - stop brushing shoulders with brothers/sister units, come on guys.
			uc.u.setAttackingUnit(uc.u2);
		}

		// TODO - add graphical triggers
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		for(final Tower t : towers) {
			renderer.draw(SQUARE, t.getPosition(), Tower.SCALE, t.getRenderColor());
		}
		for(final Index2D adjIndex : towerEdges) {
			final Tower tI = towers.get(adjIndex.x);
			final Tower tJ = towers.get(adjIndex.y);
			drawTowerLine(renderer, tI, tJ);
		}
		for(final Player p : players) {
			for(final PlayerUnit u : p.getUnits()) {
				renderer.draw(CIRCLE, u.getPosition(), PlayerUnit.SCALE, u.getRenderColor());
			}
		}
		super.render(renderer);
		
		
		//TODO - more rendering
	}

	/**
	 * Draws a thin line from Tower ts to Tower tt with Color c
	 */
	private static void drawTowerLine(final CanvasRenderer renderer, final Tower ts, final Tower tt) {
		final Position2D s = ts.getPosition();
		final Position2D t = tt.getPosition();
		final Position2D midpoint = s.add(t).scale(0.5f).asPosition();
		final Vector2D displacement = t.minus(s);
		final float length = displacement.magnitude() - 2 * Tower.RADIUS;
		final float height = PlayerUnit.RADIUS / 2;
		final float angle = displacement.theta();
		renderer.draw(SQUARE, midpoint, new ScaleVec(length, height, 1f), angle, new Color(0.9f, 0.9f, 0.9f, 1f));
	}

	private void setupPlayersFromMap(final Map map) {
		players.add(human);
		
		final Color[] colors = {Color.GREEN, Color.ORANGE, Color.PURPLE, Color.RED, Color.BLUE};
		for(int i=0; players.size() < map.getNumberOfPlayers() && i < colors.length; i++) {
			final Color c = colors[i];
			if(!c.equals(human.getColor())) {
				players.add(new AI(c, collider));
			}
		}

		map.assignPlayers(players, collider);
	}

	private boolean moreThanOnePlayerAlive() {
		return players.size() > 1;
	}
}
