package com.scs.splitscreenfps.game.levels;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.game.entities.EntityFactory;
import com.scs.splitscreenfps.game.entities.SkyboxCube;
import com.scs.splitscreenfps.game.entities.Wall;

public class GraveyardLevel extends AbstractLevel {

	private float floor_size = 35f;

	@Override
	public void load() throws JsonSyntaxException, JsonIOException, IOException {
		this.startPositions.add(new Vector3(2, 2f, 2));
		this.startPositions.add(new Vector3(2, 2f, 2));

		Wall floor = new Wall(game, "Floor", game.getTexture("colours/black.png"), floor_size/2, -0.1f, floor_size/2, 
				floor_size, .2f, floor_size, 
				0f, true, false);
		game.ecs.addEntity(floor);

		Vector3 model_pos = new Vector3(2f, 0, 2f);

		this.game.ecs.addEntity(EntityFactory.createOriginMarker(game, model_pos));

		//loadVox("vox/graveyard.vox", 0, new Vector3(5, .1f, 5), .2f, true, false);

		//AbstractEntity model = EntityFactory.createStaticModel(game.ecs, "Castle", "vox/graveyard.obj", 5, 0, 5, 0, true);
		//game.ecs.addEntity(model);

		//String filename = "vox/block"; // works for both
		//String filename = "vox/skyscraper1"; // works for both
		//String filename = "vox/blocks2"; // works for both
		//String filename = "vox/skyscraper1"; // works for both
		//String filename = "vox/skyscraper_offset"; // works for both
		//String filename = "vox/monu1"; // works for both
		//String filename = "vox/blocks3"; // works for both
		
		//String filename = "vox/castle"; // works for both
		//String filename = "vox/monu10"; // works for both
		
		String filename = "vox/graveyard"; // doesn't work yet

		super.createCollisionShapesFromVox(filename + ".vox", model_pos, .1f);

		AbstractEntity model = EntityFactory.createOnlyModel(game.ecs, "Castle", filename + ".obj", model_pos);
		game.ecs.addEntity(model);

		//PositionComponent posData = (PositionComponent)model.getComponent(PositionComponent.class);
		//Settings.p("Created model at " + posData.position.x + "," + posData.position.y + "," + posData.position.z);

		// todo - night sky
		//game.ecs.addEntity(new SkyboxCube(game, "Skybox", "textures/sky3.jpg", 90, 90, 90));
	}


	public void setBackgroundColour() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}


	@Override
	public void update() {
	}


	@Override
	public String getName() {
		return "Graveyard Test";
	}


}
