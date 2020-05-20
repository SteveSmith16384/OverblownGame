package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class HasModelComponent {

	public String name;
	public ModelInstance model;
	public Vector3 offset; // In case the origin of the 3D model isn't 0,0,0
	public int angleOffset;
	public float scale;
	public int dontDrawInViewId = -1; // Don't draw the player's own avatar!
	public int onlyDrawInViewId = -1; // Don't draw td build targetter!
	public BoundingBox bb; // For checking if in frustum  
	public boolean always_draw = false;
	
	public HasModelComponent(String name, ModelInstance _model) {
		this(name, _model, new Vector3(), 0, 1);
	}
	

	public HasModelComponent(String _name, ModelInstance _model, float yOffset, int _angleOffset, float _scale) {
		this(_name, _model);
		
		this.offset.y = yOffset;
		angleOffset = _angleOffset;
		scale = _scale;
	}

	
	public HasModelComponent(String _name, ModelInstance _model, Vector3 _offset, int _angleOffset, float _scale) {
		name = _name;
		model = _model;
		offset = _offset;
		angleOffset = _angleOffset;
		scale = _scale;
	}

	
	@Override
	public String toString() {
		return this.name;
	}
}
