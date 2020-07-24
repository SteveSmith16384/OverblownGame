package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.Color;

public class ChangeColourComponent {

	public Color col1, col2, current;
	public long timeUntilChange, interval;
	
	public ChangeColourComponent(Color c1, Color c2, long _interval) {
		col1 = c1;
		col2 = c2;
		interval = _interval;
		//this.timeUntilChange = System.currentTimeMillis();
	}
}
