package com.scs.splitscreenfps.game.levels;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.scs.splitscreenfps.game.Game;

import ssmith.libgdx.GridPoint2Static;

public abstract class AbstractLevel implements ILevelInterface {

	public Game game;
	protected List<GridPoint2Static> startPositions = new ArrayList<GridPoint2Static>();
	
	public AbstractLevel(Game _game) {
		game = _game;
	}
	
	
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
	}
		
	public abstract void load();
	
	public abstract void update();

	public GridPoint2Static getPlayerStartMap(int idx) {
		return this.startPositions.get(idx);
	}
	
}
