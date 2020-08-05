package com.scs.splitscreenfps.game.components;

public class UltimateAbilityComponent {
	
	public enum UltimateType {RocketBarrage, CraterStrike, Minefield, TraceyBomb, SprayLava};
	
	public UltimateType type;
	public float power = 0;
	public float charge_duration;
	public boolean button_released = true; // Prevent using all abilities in one go
	
	public boolean in_progress;
	public long end_time;
	public long next_shot;
	
	public UltimateAbilityComponent(UltimateType _type, float _max_power) {
		type = _type;
		charge_duration = _max_power;
	}

}
