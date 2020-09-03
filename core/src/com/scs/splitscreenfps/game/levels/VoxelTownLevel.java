package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.SkyboxCube;

public class VoxelTownLevel extends AbstractLevel {

	//private ISystem deathmatchSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);
		//this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public void load() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		super.loadJsonFile("maps/voxel_town.json", false);
		game.ecs.addEntity(new SkyboxCube(game, "Skybox", "textures/sky3.jpg", 90, 90, 90));
	}


	@Override
	public void update() {
		//deathmatchSystem.process();
	}


	@Override
	public String getName() {
		return "Voxel Town Deathmatch";
	}


}
