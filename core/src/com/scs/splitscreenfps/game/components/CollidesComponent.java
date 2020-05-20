package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.scs.basicecs.AbstractEntity;
import com.scs.splitscreenfps.Settings;

public class CollidesComponent {

	public AbstractEntity dont_collide_with; // We don't collide with shooter
	public boolean blocksMovement = true;
	public float rad;

	public CollidesComponent(boolean _blocks, float _rad) {
		this.blocksMovement = _blocks;
		rad = _rad;
		
		if (rad > .5f) {
			Settings.pe("WARNING: Rad is " + rad);
		}

	}


	public CollidesComponent(boolean _blocks, ModelInstance instance) {
		this.blocksMovement = _blocks;
		BoundingBox bbx = new BoundingBox();
		instance.calculateBoundingBox(bbx);
		bbx.mul(instance.transform); // Move bb to position
		rad = (bbx.getWidth() + bbx.getDepth())/4;
	}

}
