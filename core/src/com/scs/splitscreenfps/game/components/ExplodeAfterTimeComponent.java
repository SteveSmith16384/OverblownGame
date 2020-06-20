package com.scs.splitscreenfps.game.components;

public class ExplodeAfterTimeComponent {

	public long explode_time;
	public float force;
	
	public ExplodeAfterTimeComponent(long time, float _force) {
		this.explode_time = System.currentTimeMillis() + time;
		force = _force;
	}
	
}
