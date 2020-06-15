package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.EntityFactory;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.entities.Wall;

import ssmith.libgdx.GridPoint2Static;

public class RollingBallLevel extends AbstractLevel {

	private static final long BALL_INTERVAL = 3000;
	
	private long last_ball_time;
	
	public RollingBallLevel(Game _game) {
		super(_game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(1f, .3f, .3f, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new GridPoint2Static(1, 1));
		this.startPositions.add(new GridPoint2Static(2, 2));
		this.startPositions.add(new GridPoint2Static(3, 3));
		this.startPositions.add(new GridPoint2Static(4, 4));

		Wall floor = new Wall(game.ecs, "Floor", "textures/set3_example_1.png", 5, -0.1f, 5, 10f, .2f, 10f, 0f);
		game.ecs.addEntity(floor);

		Wall tilt = new Wall(game.ecs, "Tilt", "textures/set3_example_1.png", 12.5f, 1.5f, 5, 
				6f, .2f, 10f, 0f, Vector3.Z, 25);
		//HasModelComponent model = (HasModelComponent)tilt.getComponent(HasModelComponent.class);
		//model.model.transform.rotate();
		//PhysicsComponent pc = (PhysicsComponent)tilt.getComponent(PhysicsComponent.class);
		//pc.rotate(Vector3.Z, 45);
		game.ecs.addEntity(tilt);

	}


	@Override
	public void update() {
		if (this.last_ball_time + BALL_INTERVAL < System.currentTimeMillis()) {
			this.last_ball_time = System.currentTimeMillis();
			
			AbstractEntity ball = EntityFactory.createBall(game.ecs, "textures/set3_example_1.png", 13, 5, 3, 1.5f, 10);
			game.ecs.addEntity(ball);
		}
	}



}
