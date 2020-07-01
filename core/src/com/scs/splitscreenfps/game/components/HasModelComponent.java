package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.scs.splitscreenfps.Settings;

public class HasModelComponent {

	public ModelInstance model;
	
	public Vector3 positionOffsetToOrigin; // In case the origin of the 3D model isn't 0,0,0 // todo - remove!
	public int angleYOffsetToFwds; // In case model at 0 degrees isn't facing fwds
	public float scale = 1f; // Need this since scale is not stored in physics body.  Todo - remove, since we scale the model
	
	public int dontDrawInViewId = -1; // Don't draw the player's own avatar!
	public int onlyDrawInViewId = -1; // Don't draw the targetter for other players!
	public BoundingBox bb; // For checking if in frustum  
	public boolean always_draw = false;

	
	public HasModelComponent(ModelInstance _model, float scale) {
		this(_model, new Vector3(), 0, scale);
	}
	

	public HasModelComponent(ModelInstance _model, float yOffset, int _angleOffset, float _scale) {
		this(_model, _scale);
		
		this.positionOffsetToOrigin.y = yOffset;
		angleYOffsetToFwds = _angleOffset;
	}

	
	public HasModelComponent(ModelInstance _model, Vector3 _offset, int _angleOffset, float _scale) {
		model = _model;
		positionOffsetToOrigin = _offset;
		angleYOffsetToFwds = _angleOffset;
		scale = _scale;
		
		if (Settings.STRICT) {
			if (_scale <= 0) {
				throw new RuntimeException("Zero scale!");
			}
		}
	}

	
}
