package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.scs.splitscreenfps.game.Game;

public class AITestLevel extends AbstractLevel {

	//private ISystem deathmatchSystem;
	
	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		//this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public void load() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		super.loadJsonFile("maps/complex.json", false);
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
