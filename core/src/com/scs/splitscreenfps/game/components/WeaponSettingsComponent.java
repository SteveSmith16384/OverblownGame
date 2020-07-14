package com.scs.splitscreenfps.game.components;

import com.scs.splitscreenfps.game.data.ExplosionData;

public class WeaponSettingsComponent {

	// Weapon types
	public static final int WEAPON_RIFLE = 1;
	public static final int WEAPON_GRENADE_LAUNCHER = 2;
	public static final int WEAPON_ROCKET_LAUNCHER = 3;
	public static final int WEAPON_PUNCH = 4;
	public static final int WEAPON_CANNON = 5;
	
	public int weapon_type;
	public long shot_interval;
	public long reload_interval;
	public int max_ammo;
	public float range;
	public int damage;
	public float kickback_force = 0f;
	public final ExplosionData explData;
	
	public WeaponSettingsComponent(int type, long _shot_interval, long _reload_interval, int ammo, float _range, int _damage, ExplosionData _explData) {
		this.weapon_type = type;
		this.max_ammo = ammo;
		shot_interval = _shot_interval;
		reload_interval = _reload_interval;
		damage = _damage;
		range = _range;
		explData = _explData;
	}
	
}
