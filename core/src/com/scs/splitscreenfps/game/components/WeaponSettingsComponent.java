package com.scs.splitscreenfps.game.components;

public class WeaponSettingsComponent {

	// Weapon types
	public static final int WEAPON_BULLET = 1;
	public static final int WEAPON_GRENADE = 2;
	public static final int WEAPON_ROCKET = 3;
	
	public long shot_interval;
	public long reload_interval;
	public int max_ammo;
	public int weapon_type;
	
}
