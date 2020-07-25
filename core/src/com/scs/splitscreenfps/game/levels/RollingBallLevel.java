package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.Wall;

import ssmith.lang.NumberFunctions;

public class RollingBallLevel extends AbstractLevel {

	private static final long BALL_INTERVAL = 3000;
	
	private long last_ball_time;
	
	public RollingBallLevel(Game _game) {
		super(_game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(2, 2f, 2));
		this.startPositions.add(new Vector3(3, 2f, 3));
		this.startPositions.add(new Vector3(4, 2f, 4));

		Wall floor = new Wall(game, "Floor", "textures/set3_example_1.png", 5, -0.1f, 5, 
				10f, .2f, 10f, 
				0f, true, false);
		game.ecs.addEntity(floor);

		Wall tilt = new Wall(game, "Tilt", "textures/set3_example_1.png", 12.5f, 1.5f, 5, 
				6f, .2f, 10f, 
				0f, 
				0, 0, 25, true, false);
		//HasModelComponent model = (HasModelComponent)tilt.getComponent(HasModelComponent.class);
		//model.model.transform.rotate();
		//PhysicsComponent pc = (PhysicsComponent)tilt.getComponent(PhysicsComponent.class);
		//pc.rotate(Vector3.Z, 45);
		game.ecs.addEntity(tilt);
		/*
		for (int i=0 ; i<20 ; i++) {
			int col = NumberFunctions.rnd(1,  10);
			int row = NumberFunctions.rnd(1,  10);
			AbstractEntity crate = EntityFactory.createCrate(game.ecs, "textures/crate.png", col, i+3, row, .4f, .4f, .4f);
			game.ecs.addEntity(crate);
		}
*/
		//AbstractEntity doorway = EntityFactory.createDoorway(game.ecs, 0, 0, 0);
		//AbstractEntity doorway = EntityFactory.createDoorway(game.ecs, 8, -2f, 7);
		//game.ecs.addEntity(doorway);

		//AbstractEntity doorway = EntityFactory.createModel(game.ecs, "Doorway", "models/magicavoxel/doorway.obj", 8, -2f, 7, 0f, null);
		//game.ecs.addEntity(doorway);
	}


	@Override
	public void update() {
		if (this.last_ball_time + BALL_INTERVAL < System.currentTimeMillis()) {
			this.last_ball_time = System.currentTimeMillis();
			
			float z = NumberFunctions.rndFloat(2,  8);
			AbstractEntity ball = EntityFactory.createBall(game, "textures/set3_example_1.png", 13, 10, z, 1.5f, 100);
			game.ecs.addEntity(ball);
		}
	}



}
