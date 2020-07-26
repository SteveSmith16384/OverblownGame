package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HarmPlayerOnContactComponent;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.Wall;

import ssmith.lang.NumberFunctions;

public class AvoidTheBallsLevel extends AbstractLevel {

	private static final float FLOOR_SIZE = 20f;
	private static final long BALL_INTERVAL = 3000;
	
	private long last_ball_time;
	
	public AvoidTheBallsLevel(Game _game) {
		super(_game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(FLOOR_SIZE-2, 2f, FLOOR_SIZE-2));
		this.startPositions.add(new Vector3(1, 2f, FLOOR_SIZE-2));
		this.startPositions.add(new Vector3(FLOOR_SIZE-2, 2f, 1));

		Wall floor = new Wall(game, "Floor", "colours/white.png", FLOOR_SIZE/2, -0.1f, FLOOR_SIZE/2, 
				FLOOR_SIZE, .2f, FLOOR_SIZE, 
				0f, true, false);
		game.ecs.addEntity(floor);

		for (int i=0 ; i<20 ; i++) {
			int col = NumberFunctions.rnd(1,  10);
			int row = NumberFunctions.rnd(1,  10);
			AbstractEntity crate = EntityFactory.createCrate(game, "textures/crate.png", col, i+3, row, .4f, .4f, .4f);
			game.ecs.addEntity(crate);
		}

	}


	@Override
	public void update() {
		if (this.last_ball_time + BALL_INTERVAL < System.currentTimeMillis()) {
			this.last_ball_time = System.currentTimeMillis();
			
			//float z = NumberFunctions.rndFloat(2,  8);
			AbstractEntity ball = EntityFactory.createBall(game, "textures/neon/sun.jpg", FLOOR_SIZE/2, 10, FLOOR_SIZE/2, 1.5f, 100);
			ball.addComponent(new HarmPlayerOnContactComponent(null, null, "sfx/electric_explosion5.wav", 10, 0, 0, false, false));
			game.ecs.addEntity(ball);
		}
	}


}
