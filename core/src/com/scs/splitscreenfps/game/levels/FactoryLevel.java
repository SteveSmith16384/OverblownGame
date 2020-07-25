package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractSystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;

public class FactoryLevel extends AbstractLevel {

	private AbstractSystem cps;
	
	public FactoryLevel(Game _game) {
		super(_game);
		
		//this.cps = new ControlPointScoreSystem(game);
		this.cps = new DeathmatchSystem(game, game.ecs);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/factory.json", false);
			//super.loadJsonFile("maps/map_editor.json", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public void update() {
		cps.process();
	}


}
