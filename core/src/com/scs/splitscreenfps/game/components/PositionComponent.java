package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;

/**
 * Not required for physics entities
 *
 */
public class PositionComponent {

	public Vector3 position;
	public float angle_degs;

	public PositionComponent() {
		this.position = new Vector3();
	}
	
	
	public PositionComponent(float x, float z) {
		this(x, 0, z);
	}
	
	
	public PositionComponent(float x, float y, float z) {
		this.position = new Vector3(x, y, z);
	}
	
	
	public PositionComponent(Vector3 v) {
		this(v.x, v.y, v.z);
	}
	
	
	public GridPoint2 getMapPos() {
		float x = (position.x);
		float y = position.z;
		
		return new GridPoint2((int)x, (int)y) ;

	}
	
}
