package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;

public class DrawTextIn3DSpaceComponent {

	public float range;
	public String text;
	public int onlyDrawInViewId;
	public boolean rise;
	
	public DrawTextIn3DSpaceComponent(String _text, float _range, int _onlyDrawInViewId, boolean _rise) {
		text = _text;
		//offset = new Vector3(_offset);
		range = _range;
		onlyDrawInViewId = _onlyDrawInViewId;
		rise = _rise;
	}

}
