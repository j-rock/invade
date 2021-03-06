package com.fourthrock.invade.game.levels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fourthrock.invade.draw.CanvasRenderer;
import com.fourthrock.invade.draw.Color;
import com.fourthrock.invade.game.maps.Map;
import com.fourthrock.invade.game.physics.BoundingBox2D;
import com.fourthrock.invade.game.physics.BoundingCircle2D;
import com.fourthrock.invade.game.physics.Position2D;
import com.fourthrock.invade.game.player.AI;
import com.fourthrock.invade.game.player.EmptyPlayer;
import com.fourthrock.invade.game.player.Human;
import com.fourthrock.invade.game.player.Player;
import com.fourthrock.invade.game.player.WhiteAI;
import com.fourthrock.invade.game.tower.Tower;
import com.fourthrock.invade.util.Index2D;


public class Level {
	private final Map map;
	private final int levelID;
	private final int humanAssignment;
	
	public Level(final Map map, final int levelID) {
		this.map = map;
		this.levelID = levelID;
		this.humanAssignment = getHumanTowerAssignment();
	}
	
	public int getLevelID() {
		return levelID;
	}
	
	public float getMinZoom() {
		return map.getMinZoom();
	}
	
	public float getMaxZoom() {
		return map.getMaxZoom();
	}
	
	public BoundingBox2D getBounds() {
		return map.getBounds();
	}
	
	public Position2D getHumanPosition() {
		return getTowers().get(humanAssignment).getPosition();
	}
	
	public List<Tower> getTowers() {
		return map.getTowers();
	}
	
	public List<Index2D> getAdjSet() {
		return map.getAdjSet();
	}
	
	public List<Player> setupPlayers(final Human human) {
		final List<Color> colors = Color.allPlayableColors();
		colors.remove(human.getColor());
		Collections.shuffle(colors);
		
		final int numAddedPlayers = Math.min(map.getNumberOfPlayers()-2, colors.size());
		final List<Player> addedPlayers = new ArrayList<>(map.getNumberOfPlayers());
		for(int i=0; i < numAddedPlayers; i++) {
			addedPlayers.add(new AI(colors.get(i)));
		}
		
		assignPlayersToTowers(human, addedPlayers);
		assignWhites(addedPlayers);
		return addedPlayers;
	}
	
	/**
	 * For each Player in players, we assign one Tower.
	 * Also, we assign the Human to its designated tower.
	 * The remainder get their own unique WhiteAI protector,
	 * which is added to the list of Players.
	 */
	protected void assignPlayersToTowers(final Human human, final List<Player> players) {
		getTowers().get(humanAssignment).adoptNewPlayer(human);
		
		int assignedPlayersCount = 0;
		for(int i=0;
				i<humanAssignment && assignedPlayersCount < players.size();
				i++) {
			getTowers().get(i).adoptNewPlayer(players.get(assignedPlayersCount++));
		}
		
		for(int i=humanAssignment+1;
				i<map.getNumberOfPlayers() && assignedPlayersCount < players.size();
				i++) {
			getTowers().get(i).adoptNewPlayer(players.get(assignedPlayersCount++));
		}
	}
	
	protected void assignWhites(final List<Player> players) {
		if(humanAssignment < players.size()) {
			for(int i=players.size()+1; i<map.getNumberOfPlayers(); i++) {
				addWhitePlayer(players, i);
			}
		} else {
			for(int i=players.size();
					i<map.getNumberOfPlayers()
					&& i != humanAssignment;
					i++) {
				addWhitePlayer(players, i);
			}
			for(int i=humanAssignment+1; i<map.getNumberOfPlayers(); i++) {
				addWhitePlayer(players, i);
			}
		}
	}
	
	protected void addWhitePlayer(final List<Player> players, final int index) {
		addAssignPlayer(players, index, new WhiteAI());
	}
	
	protected void addEmptyPlayer(final List<Player> players, final int index) {
		addAssignPlayer(players, index, new EmptyPlayer());
	}
	
	private void addAssignPlayer(final List<Player> players, final int index, final Player p) {
		getTowers().get(index).adoptNewPlayer(p);
		players.add(p);
	}
	
	protected int getHumanTowerAssignment() {
		return 0;
	}
	
	public void render(final CanvasRenderer renderer, final Position2D lPos, final float angleOfTime) {		
		final float phase = (Math.abs(lPos.x + lPos.y) + angleOfTime) % 360f;
		final Color rimColor = colorOfPhaseTime(phase, angleOfTime);
		Tower.preRender(renderer, lPos, Tower.SPAWN_RADIUS, rimColor);
		
		final BoundingCircle2D bounds = map.getBounds().toCircleBounds();
		final Position2D center = bounds.getCenter();
		final float maxRadius = bounds.getRadius();
		final float miniScale = Tower.BORDER_RADIUS / maxRadius;
		for(final Tower t : getTowers()) {
			final Position2D tMapPos =
				t.getPosition()
				.minus(center)
				.scale(miniScale)
				.add(lPos).asPosition();
			
			final float tMapPhase = (tMapPos.x + tMapPos.y + phase) % 360f;
			
			Tower.preRender(renderer, tMapPos, Tower.SPAWN_RADIUS * miniScale, rimColor);
			Tower.postRender(renderer, tMapPos, Tower.BORDER_RADIUS * miniScale, tMapPhase, rimColor);
		}
	}

	private static Color colorOfPhaseTime(final float phase, final float time) {
		final Color[] choices = Color.LIGHT_COLORS;
		final float indexRatio = (phase / 50f) % choices.length;
		final int c = (int)(Math.floor(indexRatio));
		final int cNext = (c + 1) % choices.length;
		final float rem = indexRatio - c;
		final float alpha = (float)(Math.max(0.5f, Math.abs(Math.cos(time * Math.PI / 180f / 3))));
		return choices[c].withAlpha(1 - rem).blend(choices[cNext]).withAlpha(alpha);
	}
}
