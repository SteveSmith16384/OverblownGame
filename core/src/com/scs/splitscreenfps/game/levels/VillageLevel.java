package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.gamemodes.ControlPointScoreSystem;

public class VillageLevel extends AbstractLevel {

	private ControlPointScoreSystem cps;
	
	public VillageLevel(Game _game) {
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
			super.loadJsonFile("maps/village.json", false);
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
