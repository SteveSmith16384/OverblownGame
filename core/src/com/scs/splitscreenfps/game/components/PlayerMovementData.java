package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class PlayerMovementData {

	public Vector3 offset = new Vector3();
	public long frozenUntil = 0;
	public boolean jumpPressed = false;
	
}
