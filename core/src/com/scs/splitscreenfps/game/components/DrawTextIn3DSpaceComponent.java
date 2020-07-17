package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;

public class DrawTextIn3DSpaceComponent {

	public float range;
	public Vector3 offset;
	public String text;
	public int dontDrawInViewId;
	
	public DrawTextIn3DSpaceComponent(String _text, Vector3 _offset, float _range, int _dontDrawInViewId) {
		text = _text;
		offset = new Vector3(_offset);
		range = _range;
		dontDrawInViewId = _dontDrawInViewId;
	}

}
