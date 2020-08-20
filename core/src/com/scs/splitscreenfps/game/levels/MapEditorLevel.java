package com.scs.splitscreenfps.game.levels;

import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.systems.MapEditorSystem;
import com.scs.splitscreenfps.game.systems.ShootingSystem;

public class MapEditorLevel extends AbstractLevel {

	public static final String filename = "maps/minecraft_church.json";
	
	public MapEditorSystem mapBuilderSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		Settings.USE_MAP_EDITOR = true;

		game.ecs.removeSystem(ShootingSystem.class);

		mapBuilderSystem = new MapEditorSystem(game.ecs, game);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile(filename, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.game.ecs.addEntity(EntityFactory.createOriginMarker(game));
		
		game.appendToLog("Loaded " + filename);
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
