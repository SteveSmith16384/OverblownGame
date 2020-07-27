package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;

public class PlayerMovementData {

	public Vector3 offset = new Vector3();
	public long frozenUntil = 0;
	public boolean jumpPressed = false;
	public long next_footstep_sound;
	public float speed;
	
	public PlayerMovementData(float _speed) {
		speed = _speed;
	}
}
