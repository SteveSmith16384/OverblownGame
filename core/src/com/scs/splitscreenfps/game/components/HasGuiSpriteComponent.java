package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class HasGuiSpriteComponent {
	
	public static final int Z_FILTER = 99;
	public static final int Z_CARRIED = 50;
	public static final int Z_NORMAL = 0;

	public Sprite sprite;
	public boolean dirty = true;
	public Rectangle scale;
	public int onlyViewId = -1;
	public int zOrder;
	public boolean proportional;

	public HasGuiSpriteComponent(Sprite _sprite, int _zOrder, Rectangle _scale, boolean _proportional) {
		sprite = _sprite;
		scale = _scale;
		zOrder = _zOrder;
		proportional = _proportional;
	}

}
