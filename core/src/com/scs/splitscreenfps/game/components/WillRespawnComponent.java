package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;

public class WillRespawnComponent {

	public long respawn_time;
	public Vector3 respawnPoint;

	public WillRespawnComponent(Vector3 _respawnPoint) {
		this.respawn_time = System.currentTimeMillis() + 3000;
		respawnPoint = _respawnPoint;
	}

}
