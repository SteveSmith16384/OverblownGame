package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.HarmPlayerOnContactComponent;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.entities.AbstractPlayersAvatar;
import com.scs.splitscreenfps.game.entities.AvatarFactory;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.PlayerAvatar_Ball;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.gamemodes.DeathmatchSystem;

import ssmith.lang.NumberFunctions;
import ssmith.libgdx.ModelFunctions;

public class WhatTheBallLevel extends AbstractLevel {

	private static final float FLOOR_SIZE = 15f;
	private static final long BALL_INTERVAL = 3000;

	private long last_ball_time;
	private ISystem deathmatchSystem;
	private boolean setup = false;

	public void getReadyForGame(Game game) {
		super.getReadyForGame(game);

		this.deathmatchSystem = new DeathmatchSystem(game, game.ecs, true);
	}


	@Override
	public int[] getHeroSelection() {
		return new int[]{AvatarFactory.CHAR_WHAT_THE_BALL};
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		try {
			//todo super.loadJsonFile("maps/whattheball.json", false);
			//super.loadJsonFile("maps/map_editor.json", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.startPositions.add(new Vector3(1, 2f, 1));
		this.startPositions.add(new Vector3(FLOOR_SIZE-2, 2f, FLOOR_SIZE-2));
		this.startPositions.add(new Vector3(1, 2f, FLOOR_SIZE-2));
		this.startPositions.add(new Vector3(FLOOR_SIZE-2, 2f, 1));

		Wall floor = new Wall(game, "Floor", "colours/white.png", FLOOR_SIZE/2, -0.1f, FLOOR_SIZE/2, 
				FLOOR_SIZE, .2f, FLOOR_SIZE, 
				0f, true, false);
		game.ecs.addEntity(floor);

		for (int i=0 ; i<20 ; i++) {
			this.createBall();
		}

	}

	
	private void createBall() {
		float col = NumberFunctions.rndFloat(2, FLOOR_SIZE);
		float row = NumberFunctions.rndFloat(2, FLOOR_SIZE);
		AbstractEntity ball = EntityFactory.createBall(game, "textures/set3_example_1.png", col, 10, row, PlayerAvatar_Ball.DIAM, 1);
		HasModelComponent hasModel = (HasModelComponent)ball.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		ModelFunctions.setColour(instance, Color.WHITE);
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


}
