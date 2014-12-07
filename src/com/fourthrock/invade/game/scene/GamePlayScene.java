package com.fourthrock.invade.game.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.draw.PixelScreen2D;
import com.fourthrock.invade.game.levels.Level;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.physics.collision.AttackTowerCollision;
import com.fourthrock.invade.game.physics.collision.AttackUnitCollision;
import com.fourthrock.invade.game.physics.collision.CollisionCollection;
import com.fourthrock.invade.game.physics.collision.MoveBackCollision;
import com.fourthrock.invade.game.physics.collision.TreeCollider;
import com.fourthrock.invade.game.player.Human;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.game.unit.PlayerUnit;
import com.fourthrock.invade.util.Allocator;
import com.fourthrock.invade.util.Index2D;
import com.fourthrock.invade.util.ObjectPool;

/**
 * Main scene for actual Invade gameplay.
 * Handles logical and graphical state updates.
 * 
 * @author Joseph
 *
 */
public class GamePlayScene extends WorldEyeScene {
	private final ObjectPool<PlayerUnit> units;
	private final TreeCollider<Tower> collider;
	private final PlayerUI playerUI;
	private final List<Tower> towers;
	private final List<Index2D> towerEdges;
	private final List<Player> players;
	private final Human human;
	private float angleOfTime;

	
	public GamePlayScene(final Level level, final Color humanColor) {
		super(level.getMinZoom(), level.getMaxZoom(), level.getHumanPosition(), level.getBounds(), humanColor);
		
		this.units = new ObjectPool<>(new Allocator<PlayerUnit>(){
			@Override public PlayerUnit allocate() {
				return new PlayerUnit();
			}
		});
		this.collider = new TreeCollider<>(level.getTowers());
		this.towers = level.getTowers();
		this.towerEdges = level.getAdjSet();
		this.players = new ArrayList<>();
		this.human = new Human(humanColor);
		this.playerUI = new PlayerUI(human);
		this.angleOfTime = 0f;
		
		players.add(human);
		final List<Player> addedPlayers = level.setupPlayers(human);
		players.addAll(addedPlayers);
		units.preAllocate(towers.size() * 15);
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
		
		if(!human.isAlive() || !moreThanOnePlayerAlive()) {
			final Scene loseGameScene = new LevelChooserScene();
			return new FadeToBlackScene(this, loseGameScene, 4000L);
		} else {
			for(final Tower t : towers) {
				t.regainHealth(dt);
			}
			for(final Player p : players) {
				p.tryGenerateUnit(units, dt);
				p.decideTarget();
				p.moveUnits(dt);
			}
			processCollisions(collider.withdrawCollisions(units), dt);
			for(final Player p : players) {
				p.fireUnits(dt);
			}
			for(final Player p : players) {
				p.removeDeadUnits(units);
			}
			return this;
		}
	}

	private void processCollisions(final CollisionCollection colls, final long dt) {
		// TODO - parallel all of these
		for(final MoveBackCollision mbc : colls.getMoveBackCollisions()) {
			mbc.process(dt);
		}
		for(final AttackTowerCollision atc : colls.getAttackTowerCollisions()) {
			atc.process(dt);
		}
		for(final AttackUnitCollision auc : colls.getAttackUnitCollisions()) {
			auc.process(dt);
		}
	}

	@Override
	public void render(final CanvasRenderer renderer) {
		final Set<Tower> humanAdjTowers = human.getAdjacentTowers();
		
		//Draw lines between Towers
		for(final Index2D adjIndex : towerEdges) {
			final Tower tI = towers.get(adjIndex.x);
			final Tower tJ = towers.get(adjIndex.y);
			final float alphaMultiplier =
				humanAdjTowers.contains(tI) && humanAdjTowers.contains(tJ)
					? 1f
					: 0.1f;
			drawTowerLine(renderer, tI, tJ, alphaMultiplier);
		}
		
		//Draw Tower health radius
		for(final Tower t : towers) {
			final float alpha = humanAdjTowers.contains(t) ? 1f : 0.2f;
			t.preRender(renderer, alpha);
		}
		
		//Draw PlayerUnit attacks
		for(final PlayerUnit u : units) {
			u.preRender(renderer);
		}
		
		//Draw PlayerUnits
		final Color shadowBlack = Color.BLACK.withAlpha(0.7f);
		for(final PlayerUnit u : units) {
			u.postRender(renderer, shadowBlack);
		}
		
		//Draw Tower centers
		for(final Tower t : towers) {
			final float alpha = humanAdjTowers.contains(t) ? 1f : 0.2f;
			final float orientation = (t.getOrientation() + angleOfTime) % 360f;
			t.postRender(renderer, alpha, orientation);
		}
		
		//Draw user taps
		super.render(renderer);
	}
	

	@Override
	public void renderScreen(final CanvasRenderer renderer) {
		playerUI.render(renderer);
	}

	/**
	 * Draws a thin line from Tower ts to Tower tt
	 */
	private void drawTowerLine(final CanvasRenderer renderer, final Tower ts, final Tower tt, final float alphaMultiplier) {
		final float phase = (float) ((ts.getOrientation() + tt.getOrientation() + angleOfTime) * Math.PI / 180f);
		final float alpha = (float)Math.abs(Math.sin(phase)) * alphaMultiplier;
		final Color color =
			ts.getColor().equals(tt.getColor())
				? ts.getColor().withAlpha(alpha)
				: Color.SNOW.withAlpha(alpha);
		
		Tower.drawLine(renderer, ts.getPosition(), ts.getActiveRadius(),
								 tt.getPosition(), tt.getActiveRadius(),
								 phase, color);
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
}
