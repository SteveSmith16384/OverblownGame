package com.scs.splitscreenfps.game.levels;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.PlayersAvatar_Person;

import ssmith.libgdx.GridPoint2Static;

public abstract class AbstractLevel implements ILevelInterface {

	public Game game;
	protected int map_width;
	protected int map_height;
	protected List<GridPoint2Static> startPositions = new ArrayList<GridPoint2Static>();
	
	public AbstractLevel(Game _game) {
		game = _game;
	}
	
	
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
	}
		
	public abstract void load();
	
	public abstract void startGame();
	
	public void renderUI(SpriteBatch batch, int viewIndex) {}

	public GridPoint2Static getPlayerStartMap(int idx) {
		return this.startPositions.get(idx);
	}
	
}
