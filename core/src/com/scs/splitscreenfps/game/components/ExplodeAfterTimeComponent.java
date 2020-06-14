package com.scs.splitscreenfps.game.components;

public class ExplodeAfterTimeComponent {

	public long explode_time;
	
	public ExplodeAfterTimeComponent(long time) {
		this.explode_time = System.currentTimeMillis() + time;
	}
}
