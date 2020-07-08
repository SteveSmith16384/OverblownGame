package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class HasGuiSpriteComponent {
	
	public static final int Z_FILTER = 99;
	public static final int Z_CARRIED = 50;
	public static final int Z_NORMAL = 0;

	public String name;
	public Sprite sprite;
	public boolean dirty = true;
	public Rectangle scale;
	public int onlyViewId = -1;
	public int zOrder;

	public HasGuiSpriteComponent(String _name, Sprite _sprite, int _zOrder, Rectangle _scale) {
		name = _name;
		sprite = _sprite;
		scale = _scale;
		zOrder = _zOrder;
	}

}
