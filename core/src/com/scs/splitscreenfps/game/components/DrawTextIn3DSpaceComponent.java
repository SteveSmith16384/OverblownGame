package com.scs.splitscreenfps.game.components;

public class DrawTextIn3DSpaceComponent {

	public float range;
	public String text;
	public int onlyDrawInViewId;
	public boolean rise;
	
	public DrawTextIn3DSpaceComponent(String _text, float _range, int _onlyDrawInViewId, boolean _rise) {
		text = _text;
		range = _range;
		onlyDrawInViewId = _onlyDrawInViewId;
		rise = _rise;
	}

}
