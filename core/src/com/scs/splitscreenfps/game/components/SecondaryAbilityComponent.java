package com.scs.splitscreenfps.game.components;

public class SecondaryAbilityComponent {

	public enum Type {Boost, Jump};
		
	public long cooldown;
	public long lastShotTime;
	public Type type;

	public boolean requiresBuildUp = false;
	public boolean buildUpActivated = false;
	public float power = 0;
	public float max_power;
	
	public SecondaryAbilityComponent(Type _type, long _interval) {
		type = _type;
		cooldown =_interval;
	}
	
	
	/**
	 * For power build-up abilities
	 */
	public SecondaryAbilityComponent(Type _type, long _interval, float _max_power) {
		this(_type, _interval);
		
		this.requiresBuildUp = true;
		max_power = _max_power;
	}

}
