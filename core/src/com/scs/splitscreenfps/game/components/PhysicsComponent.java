package com.scs.splitscreenfps.game.components;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class PhysicsComponent {

	public btRigidBody body;
	
	public PhysicsComponent(btRigidBody _body) {
		body = _body;
	}
	
}
