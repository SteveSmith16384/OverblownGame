package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.scs.splitscreenfps.game.Game;

public class AITestLevel extends AbstractLevel {

	//private ISystem deathmatchSystem;
	
	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		//this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/complex.json", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void update() {
		//deathmatchSystem.process();
	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{1, 2, 3, 4}; // todo
	}


	@Override
	public String getName() {
		return "AI Test Level";
	}


}
