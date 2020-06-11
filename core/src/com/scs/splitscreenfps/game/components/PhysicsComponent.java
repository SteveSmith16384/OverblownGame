package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class PhysicsComponent {

	public btRigidBody body;
	public Vector3 force;
	
	public PhysicsComponent(btRigidBody _body) {
		body = _body;
	}
	
}
