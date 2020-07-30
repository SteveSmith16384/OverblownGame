package com.scs.splitscreenfps.game.components;

public class AddComponentAfterTimeComponent {

	public long time_to_add;
	public Object component;
	
	public AddComponentAfterTimeComponent(Object _component, long time_millis) {
		this.component = _component;
		this.time_to_add = System.currentTimeMillis() + time_millis;
	}
}
