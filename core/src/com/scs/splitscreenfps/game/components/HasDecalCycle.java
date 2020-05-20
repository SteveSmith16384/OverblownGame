package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class HasDecalCycle {

	public final float interval;
	public float animTimer;
	public int decalIdx = 0;
	public Decal decals[];
	public boolean remove_at_end_of_cycle = false;
	
	public HasDecalCycle(float _interval, int num) {
		interval = _interval;
		decals = new Decal[num];
	}

}
