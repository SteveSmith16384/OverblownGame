package com.scs.splitscreenfps.game.levels;

import java.io.FileNotFoundException;

import com.badlogic.gdx.math.Vector3;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.systems.MapEditorSystem;
import com.scs.splitscreenfps.game.systems.ShootingSystem;

public class MapEditorLevel extends AbstractLevel {

	public static final String MAP_FILENAME = "maps/minecraft_house.json";
	
	public static final String STATIC_MODEL_FILENAME = "models/kenney/spacekit2/craft_speederB.obj";
	public static final String DYNAMIC_MODEL_FILENAME = "vox/veh_bus.obj";

	public MapEditorSystem mapBuilderSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		Settings.USE_MAP_EDITOR = true;

		game.ecs.removeSystem(ShootingSystem.class);

		mapBuilderSystem = new MapEditorSystem(game.ecs, game);
	}


	@Override
	public void load() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		super.loadJsonFile(MAP_FILENAME, true);

		this.game.ecs.addEntity(EntityFactory.createOriginMarker(game, new Vector3()));

		game.appendToLog("Loaded " + MAP_FILENAME);
		game.appendToLog("Map editor ready");

	}


	@Override
	public void update() {
		this.mapBuilderSystem.process();
	}


	@Override
	public String getName() {
		return "Map Editor";
	}


}
