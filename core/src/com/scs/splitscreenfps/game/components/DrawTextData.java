package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class DrawTextData {

	public BitmapFont font;
	public float x_pcent, y_pcent;
	public String text;
	//public boolean centre_x;
	public Color colour;
	public int drawOnViewId = -1;
	//public int size; // 1, 2, 3 as in H1, H2 and H3
	
	public DrawTextData(BitmapFont _font, int _drawOnViewId) {
		font = _font;
		drawOnViewId = _drawOnViewId;
	}

}
