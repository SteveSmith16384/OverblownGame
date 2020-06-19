package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.Wall;

public class LoadMapLevel extends AbstractLevel {
	
	private static final int FLOOR_SIZE = 20;
	
	public LoadMapLevel(Game _game) {
		super(_game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1f, .3f, .3f, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(15, 1f, 5));
		this.startPositions.add(new Vector3(5, 1f, 5));
		this.startPositions.add(new Vector3(5, 1f, 15));

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
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {
		
	}


}
