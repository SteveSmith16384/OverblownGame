package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;

public class MovementData {

	public Vector3 offset = new Vector3();
	public long frozenUntil = 0;
	public boolean must_move_x_and_z = false;  // Movement is only successful if they can move on both axis
	
	public MovementData() {
	}

}
