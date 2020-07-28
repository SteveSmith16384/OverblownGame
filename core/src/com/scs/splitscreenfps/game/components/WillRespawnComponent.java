package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;

public class WillRespawnComponent {

	public static final int RESPAWN_TIME = 4000;
	
	public long respawn_time;
	public Vector3 respawnPoint;

	public WillRespawnComponent(Vector3 _respawnPoint) {
		this.respawn_time = System.currentTimeMillis() + RESPAWN_TIME;
		respawnPoint = _respawnPoint;
	}

}
