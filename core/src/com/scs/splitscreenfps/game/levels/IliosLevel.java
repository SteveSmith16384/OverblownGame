package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.Wall;

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
		Gdx.gl.glClearColor(0, .6f, .8f, 1);
	}


	@Override
	public void load() {
		this.startPositions.add(new Vector3(2, 2f, 2));
		this.startPositions.add(new Vector3(MAP_SIZE-3, 2f, 2));
		this.startPositions.add(new Vector3(2, 2f, MAP_SIZE-3));
		this.startPositions.add(new Vector3(MAP_SIZE-3, 2f, MAP_SIZE-3));

		//String tex = "textures/set3_example_1.png";
		String tex = "colours/white.png";

		// Level 1
		{
			// Top
			Wall t1 = new Wall(game, "Floor", tex, MAP_SIZE/2, -THICKNESS/2, START_WIDTH/2, 
					MAP_SIZE, THICKNESS, START_WIDTH, 
					0f, true, false);
			game.ecs.addEntity(t1);

			// Left
			Wall t2 = new Wall(game, "Floor", tex, START_WIDTH/2, -THICKNESS/2, MAP_SIZE/2, 
					START_WIDTH, THICKNESS, MAP_SIZE-(START_WIDTH*2), 
					0f, true, false);
			game.ecs.addEntity(t2);

			// Right
			Wall t3 = new Wall(game, "Floor", tex, MAP_SIZE-(START_WIDTH/2), -THICKNESS/2, MAP_SIZE/2, 
					START_WIDTH, THICKNESS, MAP_SIZE-(START_WIDTH*2), 
					0f, true, false);
			game.ecs.addEntity(t3);

			// Bottom
			Wall t4 = new Wall(game, "Floor", tex, MAP_SIZE/2, -THICKNESS/2, MAP_SIZE-(START_WIDTH/2), 
					MAP_SIZE, THICKNESS, START_WIDTH, 
					0f, true, false);
			game.ecs.addEntity(t4);

			// Cyl TL
			addPillar(1f, 0f, 1f);
			// Cyl TR
			addPillar(MAP_SIZE-1, 0f, 1f);
			// Cyl BL
			addPillar(1f, 0f, MAP_SIZE-1);
			// Cyl BR
			addPillar(MAP_SIZE-1, 0f, MAP_SIZE-1);
		}


	}


	private void addPillar(float x, float y, float z) {
		//String tex = "textures/set3_example_1.png";
		String tex = "colours/white.png";
		AbstractEntity pillar = EntityFactory.createCylinder(game, tex, x, y+PILLAR_HEIGHT/2, z, 1f, PILLAR_HEIGHT, 1f);
		game.ecs.addEntity(pillar);
	}


	@Override
	public void update() {
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {

	}


}
