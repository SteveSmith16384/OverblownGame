package com.scs.splitscreenfps.game.components;

import com.scs.basicecs.AbstractEntity;

public class HarmPlayerOnContactComponent {

	public int damage;
	public String sfx;
	public AbstractEntity shooter;
	public boolean remove;
	public boolean show_explosion;
	
	public HarmPlayerOnContactComponent(AbstractEntity _shooter, String _sfx, int _damage, boolean _remove, boolean _show_explosion) {
		shooter = _shooter;
		sfx = _sfx;
		damage = _damage;
		remove = _remove;
		show_explosion =_show_explosion;
	}
	
}
