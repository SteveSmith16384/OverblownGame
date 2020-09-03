package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.SkyboxCube;
import com.scs.splitscreenfps.game.gamemodes.ControlPointScoreSystem;

public class VillageLevel extends AbstractLevel {

	private ControlPointScoreSystem cps;
	
	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.cps = new ControlPointScoreSystem(game);
	}


	@Override
	public void load() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		super.loadJsonFile("maps/village.json", false);
		game.ecs.addEntity(new SkyboxCube(game, "Skybox", "textures/sky3.jpg", 90, 90, 90));
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
