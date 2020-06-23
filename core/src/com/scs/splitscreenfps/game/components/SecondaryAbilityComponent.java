package com.scs.splitscreenfps.game.components;

public class SecondaryAbilityComponent {

	public enum Type {Boost, Jump};
		
	public long interval;
	public long lastShotTime;
	public Type type;
	
	public SecondaryAbilityComponent(Type _type, long _interval) {
		type = _type;
		interval =_interval;
	}
	
}
