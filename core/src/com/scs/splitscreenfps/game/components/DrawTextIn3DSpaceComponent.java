package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.Color;

public class DrawTextIn3DSpaceComponent {

	public float range;
	public String text;
	public int onlyDrawInViewId;
	public boolean rise;
	public Color colour;
	
	public DrawTextIn3DSpaceComponent(String _text, float _range, int _onlyDrawInViewId, boolean _rise, Color _colour) {
		text = _text;
		range = _range;
		onlyDrawInViewId = _onlyDrawInViewId;
		rise = _rise;
		colour = _colour;
	}

}
