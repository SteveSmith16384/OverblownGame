package com.scs.splitscreenfps.game.components;

public class SecondaryAbilityComponent {

	public enum SecondaryAbilityType {PowerPunch, JumpForwards, JetPac, JumpUp, StickyMine, TracerJump};
		
	public long cooldown;
	public long lastShotTime;
	public SecondaryAbilityType type;

	public boolean requiresBuildUp = false;
	public boolean buildUpActivated = false;
	public float power = 0;
	public float max_charge_duration;
	
	public int count_available;
	public int max_count;
	
	public SecondaryAbilityComponent(SecondaryAbilityType _type, long _cooldown) {
		type = _type;
		cooldown = _cooldown;
	}
	

	/**
	 * Use this for multiple alloewd uses
	 * @param _type
	 * @param _cooldown
	 */
	public SecondaryAbilityComponent(SecondaryAbilityType _type, long _cooldown, int _count) {
		type = _type;
		cooldown = _cooldown;
		this.count_available = _count;
		this.max_count =this.count_available;
	}
	
	
	/**
	 * For power build-up abilities
	 */
	public SecondaryAbilityComponent(SecondaryAbilityType _type, long _cooldown, float _max_charge_duration) {
		this(_type, _cooldown);
		
		this.requiresBuildUp = true;
		max_charge_duration = _max_charge_duration;
	}

}
