package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;

public class HasRangeComponent {

	public Vector3 start;
	public float range;
	
	public HasRangeComponent(Vector3 _start, float _range) {
		start = new Vector3(_start);
		range = _range;
	}

}
