package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PhysicsComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.PlayerAvatar_Ball;
import com.scs.splitscreenfps.game.entities.SkyboxCube;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;

import ssmith.lang.NumberFunctions;
import ssmith.libgdx.ModelFunctions;

public class WhatTheBallLevel extends AbstractLevel {

	private static final long BALL_INTERVAL = 3000;

	private float floor_size = 15f;
	private long last_ball_time;
	private ISystem deathmatchSystem;
	private boolean setup = false;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, false);
		
		floor_size = 10 + (game.players.length * 2f);
	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_WHAT_THE_BALL};
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(floor_size-2, 2f, floor_size-2));
		this.startPositions.add(new Vector3(1, 2f, floor_size-2));
		this.startPositions.add(new Vector3(floor_size-2, 2f, 1));

		Wall floor = new Wall(game, "Floor", game.getTexture("colours/white.png"), floor_size/2, -0.1f, floor_size/2, 
				floor_size, .2f, floor_size, 
				0f, true, false);
		game.ecs.addEntity(floor);

		for (int i=0 ; i<20 ; i++) {
			this.createBall();
		}
		game.ecs.addEntity(new SkyboxCube(game, "Skybox", "textures/sky3.jpg", 90, 90, 90));

	}

	
	private void createBall() {
		float col = NumberFunctions.rndFloat(2, floor_size);
		float row = NumberFunctions.rndFloat(2, floor_size);
		AbstractEntity ball = EntityFactory.createBall(game, game.getTexture("textures/set3_example_1.png"), col, 10, row, PlayerAvatar_Ball.DIAM, 1);
		HasModelComponent hasModel = (HasModelComponent)ball.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		ModelFunctions.setColour(instance, Color.WHITE);
		
		PhysicsComponent pc = (PhysicsComponent)ball.getComponent(PhysicsComponent.class);
		pc.sound_on_collision = "sfx/clang1.wav";
		game.ecs.addEntity(ball);
	}

	
	@Override
	public void update() {
		if (setup == false) {
			setup = true;
			// Set colours
			for(AbstractPlayersAvatar avatar : game.players) {
				HasModelComponent hasModel = (HasModelComponent)avatar.getComponent(HasModelComponent.class);
				ModelInstance instance = hasModel.model;
				ModelFunctions.setColour(instance, Color.WHITE);
			}

		}

		if (this.last_ball_time + BALL_INTERVAL < System.currentTimeMillis()) {
			this.last_ball_time = System.currentTimeMillis();
			this.createBall();
		}

		deathmatchSystem.process();
	}


	@Override
	public String getName() {
		return "What the Ball?";
	}

}
