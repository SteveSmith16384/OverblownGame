package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class DrawTextComponent {

	public BitmapFont font;
	public float x_pcent, y_pcent;
	public String text; // todo - set dirty = true if this changes
	public boolean centre_x;
	public Color colour;
	public int drawOnViewId; // todo - x/y pcent needs to be relative to the viewport, unless viewportid is -1
	public boolean dirty = true;
	
	public DrawTextComponent(BitmapFont _font, int _drawOnViewId) {
		font = _font;
		drawOnViewId = _drawOnViewId;
	}

}
