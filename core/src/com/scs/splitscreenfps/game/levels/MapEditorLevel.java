package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.systems.MapEditorSystem;
import com.scs.splitscreenfps.game.systems.ShootingSystem;

public class MapEditorLevel extends AbstractLevel {
	
	private static final int FLOOR_SIZE = 20;
	
	public MapEditorSystem mapBuilderSystem;
	
	public MapEditorLevel(Game _game) {
		super(_game);
		
		game.ecs.removeSystem(ShootingSystem.class);
		
		mapBuilderSystem = new MapEditorSystem(game.ecs, game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1f, .3f, .3f, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(2, 2f, 2));
		this.startPositions.add(new Vector3(3, 2f, 3));
		this.startPositions.add(new Vector3(4, 2f, 4));

		Wall floor = new Wall(game.ecs, "Floor", "textures/set3_example_1.png", FLOOR_SIZE/2, -0.1f, FLOOR_SIZE/2, 
				FLOOR_SIZE, .2f, FLOOR_SIZE, 
				0f);
		game.ecs.addEntity(floor);

		try {
			super.loadJsonFile("maps/testmap1.json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void update() {
		this.mapBuilderSystem.process();
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {
		float yOff = 200-(game.font_med.getLineHeight() * 1.2f);
		game.font_med.setColor(1, 1, 1, 1);
		game.font_med.draw(batch2d, mapBuilderSystem.mode_text, 10, (yOff*4));
		
	}


}
