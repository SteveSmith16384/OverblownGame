package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.scs.splitscreenfps.Settings;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.systems.MapEditorSystem;
import com.scs.splitscreenfps.game.systems.ShootingSystem;

public class MapEditorLevel extends AbstractLevel {

	public MapEditorSystem mapBuilderSystem;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		Settings.USE_MAP_EDITOR = true;

		game.ecs.removeSystem(ShootingSystem.class);

		mapBuilderSystem = new MapEditorSystem(game.ecs, game);
	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_PHARTAH};
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		try {
			super.loadJsonFile("maps/map_editor.json", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		game.appendToLog("Map editor ready");

	}


	@Override
	public void update() {
		this.mapBuilderSystem.process();
	}


}
