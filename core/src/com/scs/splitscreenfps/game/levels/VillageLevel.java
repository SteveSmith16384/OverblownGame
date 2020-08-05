package com.scs.splitscreenfps.game.levels;

import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.gamemodes.ControlPointScoreSystem;

public class VillageLevel extends AbstractLevel {

	private ControlPointScoreSystem cps;
	
	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.cps = new ControlPointScoreSystem(game);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/village.json", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public void update() {
		cps.process();
	}


	@Override
	public String getName() {
		return "Village Control Point";
	}


}
