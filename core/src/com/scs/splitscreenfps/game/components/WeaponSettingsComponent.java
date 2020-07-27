package com.scs.splitscreenfps.game.components;

import com.scs.splitscreenfps.game.data.ExplosionData;

public class WeaponSettingsComponent {

	// Weapon types
	public static final int JUNKRAT_GRENADE_LAUNCHER = 2;
	public static final int WEAPON_ROCKET_LAUNCHER = 3;
	public static final int WEAPON_PUNCH = 4;
	public static final int BASTION_CANNON = 5;
	public static final int BOOMFIST_RIFLE = 6;
	public static final int BOWLINGBALL_GUN = 7;
	public static final int RACER_PISTOLS = 8;
	public static final int HYPERSPHERES = 9;
	public static final int BLOWPIPE = 10;
	public static final int PIGGY_GUN = 11;
	public static final int NONE = 12;
		
	public int weapon_type;
	public long shot_interval;
	public long reload_interval;
	public int max_ammo;
	public float range;
	public int damage;
	public float dropoff_per_metre;
	public float dropff_start;
	public float kickback_force = 0f;
	public float spread_degrees;
	public final ExplosionData explData;
	
	public WeaponSettingsComponent(int type, long _shot_interval, long _reload_interval, int ammo, float _range, 
			int _damage, float _dropff_start, float _dropoff_per_metre, ExplosionData _explData) {
		this.weapon_type = type;
		this.max_ammo = ammo;
		shot_interval = _shot_interval;
		reload_interval = _reload_interval;
		damage = _damage;
		dropff_start = _dropff_start;
		dropoff_per_metre = _dropoff_per_metre;
		range = _range;
		explData = _explData;
	}
	
}
