package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class HasDecal {

	public Decal decal;
	public boolean faceCamera = true;
	public boolean dontLockYAxis = true; // Always look up/down to camera by default
	public float rotation = 0f;
	public boolean invisible = false;

}
