package com.fourthrock.invade.game.scene;

import static com.fourthrock.invade.draw.DrawEnum.SQUARE;
import static com.fourthrock.invade.draw.DrawEnum.TRIANGLE;

import java.util.ArrayList;
import java.util.List;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.PixelScreen2D;
import com.fourthrock.invade.draw.ScaleVec;
import com.fourthrock.invade.game.maps.DefaultMap;
import com.fourthrock.invade.game.maps.Map;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.Vector2D;
import com.fourthrock.invade.game.physics.collision.AttackTowerCollision;
import com.fourthrock.invade.game.physics.collision.AttackUnitCollision;
import com.fourthrock.invade.game.physics.collision.CollisionCollection;
import com.fourthrock.invade.game.physics.collision.ColoredCircleCollider;
import com.fourthrock.invade.game.physics.collision.MoveBackCollision;
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
	private final PlayerUI playerUI;
	private final List<Tower> towers;
	private final List<Index2D> towerEdges;
	private final List<Player> players;
	private final Human human;
	private float angleOfTime;

	
	public GamePlayScene(final Map map, final Color humanColor) {
		super(map.getMinZoom(), map.getMaxZoom(), map.getTowers().get(0).getPosition(), map.getBounds());
		
		this.collider = new ColoredCircleCollider(map.getBounds());
		this.towers = map.getTowers();
		this.towerEdges = map.getAdjSet();
		this.players = new ArrayList<>();
		this.human = new Human(humanColor, collider);
		this.playerUI = new PlayerUI(human);
		this.angleOfTime = 0f;
		
		setupPlayersFromMap(map);
	}

	public GamePlayScene() {
		this(new DefaultMap(), Color.GREEN);
	}

	@Override
	public void handleTap(final PixelScreen2D screenCoords) {
		if(!playerUI.handleTap(screenCoords)) {
			final Position2D tapPos = getPositionFromScreen(screenCoords);
			super.handleTap(screenCoords);
			human.updateTarget(tapPos);
		}
	}

	@Override
	public Scene step(final long dt) {
		super.step(dt);
		angleOfTime = (angleOfTime + dt/16.7f) % 360f;
		
		if(!moreThanOnePlayerAlive()){
			final Player p = findLivingPlayer();
			final Scene endGameScene = new EndGameScene(p.getColor(), human.getColor());
			return new FadeToBlackScene(this, endGameScene, 4000L);
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
			processCollisions(collider.withdrawCollisions(), dt);
			for(final Player p : players) {
				p.fireUnits(dt);
			}
			for(final Player p : players) {
				p.removeDeadUnits();
			}
			return this;
		}
	}

	private void processCollisions(final CollisionCollection colls, final long dt) {
		for(final MoveBackCollision mbc : colls.getMoveBackCollisions()) {
			mbc.process(dt);
		}
		for(final AttackTowerCollision atc : colls.getAttackTowerCollisions()) {
			atc.process(dt);
		}
		for(final AttackUnitCollision auc : colls.getAttackUnitCollisions()) {
			auc.process(dt);
		}

		// TODO - add graphical triggers
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		for(final Tower t : towers) {
			final float orientation = (t.getOrientation() + angleOfTime) % 360f;
			final ScaleVec adjustedScale = Tower.SCALE.scale((float)Math.max(0.5f, Math.sin(Math.PI*orientation/180f)));
			renderer.draw(SQUARE, t.getPosition(), adjustedScale, orientation, t.getRenderColor());
			renderer.draw(SQUARE, t.getPosition(), adjustedScale.scale(0.5f), orientation, new Color(0f, 0f, 0f, 1f));
		}
		for(final Index2D adjIndex : towerEdges) {
			final Tower tI = towers.get(adjIndex.x);
			final Tower tJ = towers.get(adjIndex.y);
			drawTowerLine(renderer, tI, tJ);
		}
		for(final Player p : players) {
			for(final PlayerUnit u : p.getUnits()) {
				renderer.draw(TRIANGLE, u.getPosition(), PlayerUnit.SCALE, u.getOrientation(), u.getRenderColor());
			}
		}
		super.render(renderer);
	}
	

	@Override
	public void renderScreen(CanvasRenderer renderer) {
		playerUI.render(renderer);
	}

	/**
	 * Draws a thin line from Tower ts to Tower tt with Color c
	 */
	private void drawTowerLine(final CanvasRenderer renderer, final Tower ts, final Tower tt) {
		final Position2D s = ts.getPosition();
		final Position2D t = tt.getPosition();
		final Vector2D displacement = t.minus(s);
		final float phase = (float) ((ts.getOrientation() + tt.getOrientation() + angleOfTime) * Math.PI / 180f);
		final float length = displacement.magnitude() - 2 * Tower.SPAWN_RADIUS;
		final float height = PlayerUnit.BORDER_RADIUS / 2f * (float)(Math.abs(Math.cos(phase)));
		final float angle = displacement.theta();
		
		final float alpha = (float)Math.abs(Math.sin(phase));
		final Color color = (ts.getColor().equals(tt.getColor())) ? ts.getColor().withAlpha(alpha) : new Color(0.9f, 0.9f, 0.9f, alpha);
		
		final Position2D midpoint = s.add(t).scale(0.5f).asPosition();
		renderer.draw(SQUARE, midpoint, new ScaleVec(length, height, 1f), angle, color);
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
		int count = 0;
		for(final Player p : players) {
			if(p.isAlive()) {
				count++;
				if(count > 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	private Player findLivingPlayer() {
		for(final Player p : players) {
			if(p.isAlive()) {
				return p;
			}
		}
		return null;
	}
}
