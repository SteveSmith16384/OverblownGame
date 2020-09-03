
package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class PhysicsComponent {

	public btCollisionObject body;
	public boolean disable_gravity = false;
	public Vector3 initial_force; // Applied only when entity is added to the game
	public boolean removeIfFallen = true; // False for players
	public boolean physicsControlsRotation = true; // False for players
	public boolean position_dirty = true; // Objects with mass=0 only need position setting once
	public String sound_on_collision;
	public boolean enable_physics = true;
	
	// Temp vars
	private Matrix4 mat = new Matrix4();

	public PhysicsComponent(btCollisionObject _body) {
		body = _body;
	}
	
	
	public void rotate(Vector3 axis, float degrees) {
		body.getWorldTransform(mat);
		mat.rotate(axis, degrees);
		
	}
	
	
	public btRigidBody getRigidBody() {
		return (btRigidBody)body;
	}
	
	
	public boolean isRigidBody() {
		return this.body instanceof btRigidBody;
	}
	
	
	public void getTranslation(Vector3 out) {
		mat.set(body.getWorldTransform());
		mat.getTranslation(out);
	}

}
