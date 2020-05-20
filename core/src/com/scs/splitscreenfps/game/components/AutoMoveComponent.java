package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;

/**
 * For bullets and things that don't do complicated movement.
 * @author StephenCS
 *
 */
public class AutoMoveComponent {

	public Vector3 dir;

	public AutoMoveComponent(Vector3 _dir) {
		dir = _dir.cpy();
	}

}
