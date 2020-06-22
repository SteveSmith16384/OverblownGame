package com.scs.splitscreenfps.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.EntityFactory;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.entities.Wall;
import com.scs.splitscreenfps.game.gamemodes.IScoreSystem;
import com.scs.splitscreenfps.game.gamemodes.LastPlayerOnPointScoreSystem;

public class GangBeastsLevel1 extends AbstractLevel { // todo - delete this?

	public IScoreSystem scoreSystem;
	protected int map_width;
	protected int map_height;

	public GangBeastsLevel1(Game _game) {
		super(_game);

		scoreSystem = new LastPlayerOnPointScoreSystem(game);
	}


	@Override
	public void setBackgroundColour() {
		Gdx.gl.glClearColor(.7f, .3f, .3f, 1);
	}


	@Override
	public void load() {
		// Random crates
		for (int i=0 ; i<10 ; i++) {
			int col = 5;//NumberFunctions.rnd(1,  10);
			int row = 5;//NumberFunctions.rnd(1,  10);
			AbstractEntity crate = EntityFactory.createCrate(game.ecs, "textures/crate.png", col, i+3, row, .4f, .4f, .4f);
			game.ecs.addEntity(crate);
		}

		//loadMapFromFile("map1.csv");
		
		Wall floor = new Wall(game.ecs, "Floor", "textures/set3_example_1.png", 10, -0.1f, 10, 20f, .2f, 20f, 0f, true);
		game.ecs.addEntity(floor);

		// Add platform
		Wall wall1 = new Wall(game.ecs, "Wall", "textures/set3_example_1.png", 1, 1.1f, 1, .3f, 2f, .3f, 2f, true);
		game.ecs.addEntity(wall1);
		Wall wall2 = new Wall(game.ecs, "Wall", "textures/set3_example_1.png", 3f, 1.1f, 1f, .3f, 2f, .3f, 2f, true);
		game.ecs.addEntity(wall2);
		Wall wall3 = new Wall(game.ecs, "Wall", "textures/set3_example_1.png", 1f, 1.1f, 3, .3f, 2f, .3f, 2f, true);
		game.ecs.addEntity(wall3);
		Wall wall4 = new Wall(game.ecs, "Wall", "textures/set3_example_1.png", 3, 1.1f, 3, .3f, 2f, .3f, 2f, true);
		game.ecs.addEntity(wall4);		
		Wall top = new Wall(game.ecs, "Top", "textures/set3_example_1.png", 2f, 2.1f, 2f, 3f, .2f, 3f, 2f, true);
		game.ecs.addEntity(top);
	}

	
	@Override
	public void update() {
		// Do nothing
		
	}


	@Override
	public void renderUI(SpriteBatch batch2d, int currentViewId) {
		
	}

/*
	public static void setAvatarColour(AbstractEntity e, boolean alive) {
		// Reset player colours
		HasModelComponent hasModel = (HasModelComponent)e.getComponent(HasModelComponent.class);
		ModelInstance instance = hasModel.model;
		for (int i=0 ; i<instance.materials.size ; i++) {
			if (alive) {
				instance.materials.get(i).set(ColorAttribute.createDiffuse(Color.BLACK));
				instance.materials.get(i).set(ColorAttribute.createAmbient(Color.BLACK));
			} else {
				instance.materials.get(i).set(ColorAttribute.createDiffuse(Color.WHITE));
				instance.materials.get(i).set(ColorAttribute.createAmbient(Color.WHITE));
			}
		}
	}
*/
}