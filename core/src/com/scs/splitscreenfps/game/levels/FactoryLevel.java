package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.gamemodes.ControlPointScoreSystem;

public class FactoryLevel extends AbstractLevel {

	private ControlPointScoreSystem cps;
	
	public FactoryLevel(Game _game) {
		super(_game);
		
		this.cps = new ControlPointScoreSystem(game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/factory.json", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public void update() {
		cps.process();
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {

	}


}
