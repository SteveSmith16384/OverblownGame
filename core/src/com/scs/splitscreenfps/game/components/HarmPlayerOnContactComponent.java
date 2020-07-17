package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;

public class HarmPlayerOnContactComponent {

	public float damage;
	public String sfx;
	public AbstractEntity shooter;
	public boolean remove;
	
	public HarmPlayerOnContactComponent(AbstractEntity _shooter, String _sfx, float _damage, boolean _remove) {
		shooter = _shooter;
		sfx = _sfx;
		damage = _damage;
		remove = _remove;
	}
	
}
