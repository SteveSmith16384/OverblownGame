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
	
	public SecondaryAbilityComponent(SecondaryAbilityType _type, long _cooldown) {
		type = _type;
		cooldown = _cooldown;
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
