package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class MovementData { // Todo - Rename to player movement data

	public Vector3 offset = new Vector3();
	public long frozenUntil = 0;
	public boolean must_move_x_and_z = false;  // todo - remove
	public btRigidBody characterController;
	
	public MovementData() {
	}

}
