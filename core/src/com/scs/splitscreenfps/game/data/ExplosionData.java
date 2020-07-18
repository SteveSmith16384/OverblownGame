package com.scs.splitscreenfps.game.data;

public class ExplosionData {

	public final int damage;
	public final float range, force;
	
	public ExplosionData(float _range, int _damage, float _force) {
		range = _range;
		damage = _damage;
		force = _force;
	}

}
