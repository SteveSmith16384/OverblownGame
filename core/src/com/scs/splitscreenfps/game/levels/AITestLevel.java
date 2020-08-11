package com.scs.splitscreenfps.game.levels;

import com.scs.splitscreenfps.game.Game;

public class AITestLevel extends AbstractLevel {

	//private ISystem deathmatchSystem;
	
	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		//this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/complex.json", false);
		} catch (Exception e) {
			throw new RuntimeException("Error loading map file", e);
		}
	}


	@Override
	public void update() {
		//deathmatchSystem.process();
	}


	@Override
	public String getName() {
		return "AI Test Level";
	}


}
