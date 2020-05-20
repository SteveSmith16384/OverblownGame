package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class HasModelCycle {

	public final float interval;
	public float animTimer;
	public int modelIdx = 0;
	public ModelInstance models[];
	
	public HasModelCycle(float _interval, int num) {
		interval = _interval;
		models = new ModelInstance[num];
	}

}
