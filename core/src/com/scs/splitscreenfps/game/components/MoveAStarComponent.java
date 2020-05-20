package com.scs.splitscreenfps.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.GridPoint2;

public class MoveAStarComponent {

	public int destX, destY;
	public ArrayList<GridPoint2> route;
	public boolean rotate;
	public float speed;
	
	public MoveAStarComponent(float _speed, boolean _rotate) {
		speed = _speed;
		rotate = _rotate;
	}
	
}
