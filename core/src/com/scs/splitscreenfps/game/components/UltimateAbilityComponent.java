package com.scs.splitscreenfps.game.components;

public class UltimateAbilityComponent {
	
	public enum UltimateType {RocketBarrage, CraterStrike};
	
	public UltimateType type;
	public float power = 0;
	public float max_power;
	public boolean in_progress;
	public long end_time;
	public long next_shot;
	
	public UltimateAbilityComponent(UltimateType _type, float _max_power) {
		type = _type;
		max_power = _max_power;
	}

}
