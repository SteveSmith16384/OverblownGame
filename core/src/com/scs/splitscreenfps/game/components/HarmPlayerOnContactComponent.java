package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;

public class HarmPlayerOnContactComponent {

	public int damage;
	public float dropoff_per_metre;
	public float dropoff_start;
	
	public Vector3 start_pos; // For calculating dropoff
	public String sfx;
	public AbstractEntity shooter;
	public boolean remove;
	public boolean show_explosion;
	
	public HarmPlayerOnContactComponent(AbstractEntity _shooter, Vector3 _start_pos, String _sfx, int _damage, float _dropoff_start, float _dropoff_per_metre, boolean _remove, boolean _show_explosion) {
		shooter = _shooter;
		if (_start_pos != null) {
			start_pos = new Vector3(_start_pos);
		}
		sfx = _sfx;
		damage = _damage;
		dropoff_start = _dropoff_start;
		dropoff_per_metre = _dropoff_per_metre;
		remove = _remove;
		show_explosion =_show_explosion;
	}
	
}
