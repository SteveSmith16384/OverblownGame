package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;

public class HarmOnContactComponent {

	public float damage;
	public String sfx;
	public AbstractEntity shooter;
	
	public HarmOnContactComponent(AbstractEntity _shooter, String _sfx, float _damage) {
		shooter = _shooter;
		sfx = _sfx;
		damage = _damage;
	}
	
}
