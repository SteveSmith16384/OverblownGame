package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.Wall;

public class JsonMapLevel extends AbstractLevel {

	public JsonMapLevel(Game _game) {
		super(_game);
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

		Wall floor = new Wall(game.ecs, "Floor", "textures/set3_example_1.png", 5, -0.1f, 5, 
				10f, .2f, 10f, 
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


}
