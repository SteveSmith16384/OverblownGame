package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.EntityFactory;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.Wall;

import ssmith.lang.NumberFunctions;

public class IliosLevel extends AbstractLevel {
	
	private static final float MAP_SIZE = 20;
	private static final float START_WIDTH = 3;
	private static final float THICKNESS = .2f;
	private static final float PILLAR_HEIGHT = 1.5f;
	

	public IliosLevel(Game _game) {
		super(_game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(2, 2f, 2));
		this.startPositions.add(new Vector3(MAP_SIZE-3, 2f, 2));
		this.startPositions.add(new Vector3(2, 2f, MAP_SIZE-3));
		this.startPositions.add(new Vector3(MAP_SIZE-3, 2f, MAP_SIZE-3));

		// Top
		Wall t1 = new Wall(game.ecs, "Floor", "textures/set3_example_1.png", MAP_SIZE/2, -THICKNESS/2, START_WIDTH/2, 
				MAP_SIZE, THICKNESS, START_WIDTH, 
				0f, true);
		game.ecs.addEntity(t1);
		
		// Left
		Wall t2 = new Wall(game.ecs, "Floor", "textures/set3_example_1.png", START_WIDTH/2, -THICKNESS/2, MAP_SIZE/2, 
				START_WIDTH, THICKNESS, MAP_SIZE, 
				0f, true);
		game.ecs.addEntity(t2);
		
		// Right
		Wall t3 = new Wall(game.ecs, "Floor", "textures/set3_example_1.png", MAP_SIZE-(START_WIDTH/2), -THICKNESS/2, MAP_SIZE/2, 
				START_WIDTH, THICKNESS, MAP_SIZE, 
				0f, true);
		game.ecs.addEntity(t3);
		
		// Bottom
		Wall t4 = new Wall(game.ecs, "Floor", "textures/set3_example_1.png", MAP_SIZE/2, -THICKNESS/2, MAP_SIZE-(START_WIDTH/2), 
				MAP_SIZE, THICKNESS, START_WIDTH, 
				0f, true);
		game.ecs.addEntity(t4);
		
		// Cyl TL
		AbstractEntity pillar = EntityFactory.createPillar(game.ecs, "textures/set3_example_1.png", 1, PILLAR_HEIGHT/2, 1, 1f, PILLAR_HEIGHT);
		game.ecs.addEntity(pillar);
		// Cyl TR
		AbstractEntity pillar2 = EntityFactory.createPillar(game.ecs, "textures/set3_example_1.png", MAP_SIZE-2, PILLAR_HEIGHT/2, 1, 1f, PILLAR_HEIGHT);
		game.ecs.addEntity(pillar2);
		// Cyl BL
		//todo
		AbstractEntity pillar3 = EntityFactory.createPillar(game.ecs, "textures/set3_example_1.png", 1, PILLAR_HEIGHT/2, 1, 1f, PILLAR_HEIGHT);
		game.ecs.addEntity(pillar3);



	}


	@Override
	public void update() {
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {
		
	}


}
