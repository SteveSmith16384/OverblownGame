package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.splitscreenfps.game.Game;

public class IncineratorLevel extends AbstractLevel {

	public IncineratorLevel(Game _game) {
		super(_game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/incinerator.json");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public void update() {
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {

	}


}
