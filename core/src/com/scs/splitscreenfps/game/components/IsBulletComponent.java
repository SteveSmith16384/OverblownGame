package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;
import com.scs.basicecs.AbstractEntity;

public class IsBulletComponent {

	public int side;
	public AbstractEntity shooter;
	public float range;
	public Vector3 start;
	
	public IsBulletComponent(AbstractEntity _shooter, int _side, float _range, Vector3 _start) {
		shooter = _shooter;
		side = _side;
		range = _range;
		start = new Vector3(_start);
	}
	
}
