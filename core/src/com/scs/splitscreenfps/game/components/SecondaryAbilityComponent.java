package com.scs.splitscreenfps.game.components;

public class SecondaryAbilityComponent {

	public enum SecondaryAbilityType {PowerPunch, JumpForwards, JetPac, JumpUp, StickyMine, TracerJump};
		
	public final long cooldown_duration;
	public float current_cooldown;
	public SecondaryAbilityType type;

	public boolean requiresBuildUp = false;
	public boolean buildUpActivated = false;
	public float power = 0;
	public float max_power_duration;
	
	public int count_available;
	public int max_count;
	
	public SecondaryAbilityComponent(SecondaryAbilityType _type, long _cooldown) {
		type = _type;
		cooldown_duration = _cooldown;
		current_cooldown = cooldown_duration;
		this.count_available = 1;
		this.max_count = 1;
	}
	

	/**
	 * Use this for multiple alloewd uses
	 * @param _type
	 * @param _cooldown
	 */
	public SecondaryAbilityComponent(SecondaryAbilityType _type, long _cooldown, int _count) {
		this(_type, _cooldown);
		
		this.count_available = _count;
		this.max_count = this.count_available;
	}
	
	
	/**
	 * For power build-up abilities
	 */
	public SecondaryAbilityComponent(SecondaryAbilityType _type, long _cooldown, float _max_charge_duration) {
		this(_type, _cooldown);
		
		this.requiresBuildUp = true;
		max_power_duration = _max_charge_duration;
	}

}
