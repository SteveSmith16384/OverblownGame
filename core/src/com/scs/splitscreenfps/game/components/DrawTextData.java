package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.Color;

public class DrawTextData {

	public float x_pcent, y_pcent;
	public String text;
	//public boolean centre_x;
	public Color colour;
	public int drawOnViewId = -1;
	public int size; // 1, 2, 3 as in H1, H2 and H3
	
	public DrawTextData(int _size) {
		size = _size;
	}

}
