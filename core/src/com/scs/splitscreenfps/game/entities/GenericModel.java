package com.scs.splitscreenfps.game.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;
import com.scs.basicecs.BasicECS;
import com.scs.splitscreenfps.game.Game;
import com.scs.splitscreenfps.game.components.CollidesComponent;
import com.scs.splitscreenfps.game.components.HasModelComponent;
import com.scs.splitscreenfps.game.components.PositionComponent;

import ssmith.libgdx.ModelFunctions;

public class GenericModel extends AbstractEntity {

	public GenericModel(Game game, BasicECS ecs, String name, String path, float posX, float posY, float posZ, float height) {
		super(ecs, name);

		ModelInstance instance = ModelFunctions.loadModel(path, true);
		float scale = ModelFunctions.getScaleForHeight(instance, height);
		instance.transform.scl(scale);

		Vector3 offset = ModelFunctions.getOrigin(instance);
		//offset.y -= 0.2f; // Put wheels on floor
		HasModelComponent hasModel = new HasModelComponent(name = "_model", instance, offset, 0, scale);
		this.addComponent(hasModel);

		CollidesComponent cc = new CollidesComponent(true, instance);
		this.addComponent(cc);
		
		PositionComponent pos = new PositionComponent(posX, posY, posZ);
		this.addComponent(pos);
	}
	
}
