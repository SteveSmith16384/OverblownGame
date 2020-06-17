package com.scs.splitscreenfps.game.levels;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.splitscreenfps.game.Game;

public abstract class AbstractLevel implements ILevelInterface {

	public Game game;
	protected List<Vector3> startPositions = new ArrayList<Vector3>();
	
	public AbstractLevel(Game _game) {
		game = _game;
	}
	
	
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
	}
		
	public abstract void load();
	
	public abstract void update();

	public Vector3 getPlayerStartPoint(int idx) {
		return this.startPositions.get(idx);
	}
	
}
