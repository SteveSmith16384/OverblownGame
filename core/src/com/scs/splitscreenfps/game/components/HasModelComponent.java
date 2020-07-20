package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.scs.splitscreenfps.Settings;

public class HasModelComponent {

	public ModelInstance model;
	
	public float yOff;
	public int angleYOffsetToFwds; // In case model at 0 degrees isn't facing fwds
	public float scale = 1f; // Need this since scale is not stored in physics body.
	
	public int dontDrawInViewId = -1; // Don't draw the player's own avatar!
	public int onlyDrawInViewId = -1; // Don't draw the targetter for other players!
	public Vector3 dimensions; // For checking if in frustum
	public boolean always_draw = false; // Mainly used for player's weapon
	public boolean cast_shadow = true;
	public boolean keep_player_in_centre = false; // For skybox
	public boolean invisible = false;
	
	public HasModelComponent(ModelInstance _model, float scale, boolean cast_shadow) {
		this(_model, 0f, 0, scale, cast_shadow);
	}
	
	
	public HasModelComponent(ModelInstance _model, float yOffset, int _angleOffset, float _scale, boolean _cast_shadow) {
		model = _model;
		yOff = yOffset;
		//positionOffsetToOrigin = _offset;
		angleYOffsetToFwds = _angleOffset;
		scale = _scale;
		cast_shadow = _cast_shadow;
		
		if (Settings.STRICT) {
			if (_scale <= 0) {
				throw new RuntimeException("Zero scale!");
			}
		}
	}

	
}
