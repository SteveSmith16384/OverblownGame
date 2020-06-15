package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class PhysicsComponent {

	public btRigidBody body;
	public boolean disable_gravity = false;
	public Vector3 force;

	// Temp vars
	private Matrix4 mat = new Matrix4();
	private Vector3 vec = new Vector3();

	public PhysicsComponent(btRigidBody _body) {
		body = _body;
	}
	
	
	/*
	 * Helper method to get position
	 */
	public Vector3 getTranslation() {
		body.getWorldTransform(mat);
		mat.getTranslation(vec);
		return vec;
	}
	
	
	public void rotate(Vector3 axis, float degrees) {
		body.getWorldTransform(mat);
		mat.rotate(axis, degrees);
		
	}
	
}
